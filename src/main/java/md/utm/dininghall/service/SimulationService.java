package md.utm.dininghall.service;

import md.utm.dininghall.core.constant.RestaurantTableStatusCode;
import md.utm.dininghall.core.entity.RestaurantTableStatus;
import md.utm.dininghall.core.entity.Waiter;
import md.utm.dininghall.core.repository.RestaurantTableRepository;
import md.utm.dininghall.core.repository.RestaurantTableStatusRepository;
import md.utm.dininghall.core.repository.WaiterRepository;
import liquibase.repackaged.org.apache.commons.lang3.time.DurationFormatUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import md.utm.dininghall.service.utils.TransactionUtils;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SimulationService {
    private final RestaurantTableStatusRepository tableStatusRepository;
    private final RestaurantTableRepository tableRepository;
    private final WaiterRepository waiterRepository;
    private final SimulationDataService simulationDataService;
    private final CustomerOrderGenerateService orderGenerateService;
    private final SendCustomerOrderToKitchenService sendCustomerOrderToKitchenService;
    private final ApplicationContext applicationContext;

    @Transactional
    @EventListener(ApplicationReadyEvent.class)
    public void invoke() {
        log.info("Starting simulation...");
        simulationDataService.invoke();

        final var startTime = System.currentTimeMillis();
        final var proxy = applicationContext.getBean(getClass());

        waiterRepository.findAll().forEach(proxy::startWaiterWorkThread);

        final var endTime = System.currentTimeMillis();
        final var duration = getFormattedPreparationDuration(endTime - startTime);

        log.info("Finishing serving all tables in {}", duration);
    }

    @Transactional
    void startWaiterWorkThread(Waiter waiter) {
        final var tableStatusId = getTableStatus(RestaurantTableStatusCode.WAITING_FOR_WAITER).getId();

        tableRepository.findUnlockedByStatus(tableStatusId).ifPresent((table) -> {
            table.setWaiterLockId(waiter.getId());
            table.setStatus(getTableStatus(RestaurantTableStatusCode.WAITING_FOR_ORDER));

            log.info("Waiter '{}' serves table '{}'", waiter.getId(), table.getId());
            final var order = orderGenerateService.invoke(table, waiter);

            TransactionUtils.registerPostCommit(() -> sendCustomerOrderToKitchenService.invoke(order));
        });
    }

    private RestaurantTableStatus getTableStatus(RestaurantTableStatusCode code) {
        return tableStatusRepository.findByCode(code);
    }

    private String getFormattedPreparationDuration(long millis) {
        return DurationFormatUtils.formatDurationWords(millis, true, true);
    }
}
