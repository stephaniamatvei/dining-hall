package md.utm.dininghall.service;

import com.utm.dininghall.core.constant.RestaurantTableStatusCode;
import com.utm.dininghall.core.entity.RestaurantTable;
import com.utm.dininghall.core.entity.RestaurantTableStatus;
import com.utm.dininghall.core.repository.RestaurantTableRepository;
import com.utm.dininghall.core.repository.RestaurantTableStatusRepository;
import com.utm.dininghall.core.repository.WaiterRepository;
import liquibase.repackaged.org.apache.commons.lang3.time.DurationFormatUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.utm.dininghall.core.constant.RestaurantTableStatusCode.WAITING_FOR_ORDER;
import static com.utm.dininghall.core.constant.RestaurantTableStatusCode.WAITING_FOR_WAITER;
import static com.utm.dininghall.service.utils.TransactionUtils.registerPostCommit;

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

    @Transactional
    @EventListener(ApplicationReadyEvent.class)
    public void invoke() {
        log.info("Starting simulation...");
        simulationDataService.invoke();

        final var startTime = System.currentTimeMillis();
        simulate();

        final var endTime = System.currentTimeMillis();
        final var duration = getFormattedPreparationDuration(endTime - startTime);

        log.info("Finishing taking orders from all tables in {}", duration);
    }

    private void simulate() {
        final var tables = getTablesForServing();
        final var waiters = waiterRepository.findAll();

        final var lastWaiterIndex = waiters.size() - 1;
        var nextWaiterIndex = 0;

        for (var t : tables) {
            final var waiter = waiters.get(nextWaiterIndex);
            log.info("Waiter '{}' serves table '{}'", waiter.getId(), t.getId());

            t.setWaiterLockId(waiter.getId());
            t.setStatus(getTableStatus(WAITING_FOR_ORDER));

            final var order = orderGenerateService.invoke(t, waiter);
            registerPostCommit(() -> sendCustomerOrderToKitchenService.invoke(order));

            nextWaiterIndex = lastWaiterIndex == nextWaiterIndex
                    ? 0 : nextWaiterIndex + 1;
        }
    }

    private List<RestaurantTable> getTablesForServing() {
        final var tableStatus = getTableStatus(WAITING_FOR_WAITER);
        return tableRepository.findUnlockedByStatus(tableStatus.getId());
    }

    private RestaurantTableStatus getTableStatus(RestaurantTableStatusCode code) {
        return tableStatusRepository.findByCode(code);
    }

    private String getFormattedPreparationDuration(long millis) {
        return DurationFormatUtils.formatDurationWords(millis, true, true);
    }
}
