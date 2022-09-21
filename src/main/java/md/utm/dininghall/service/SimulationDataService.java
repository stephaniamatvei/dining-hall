package md.utm.dininghall.service;

import md.utm.dininghall.core.entity.RestaurantTable;
import md.utm.dininghall.core.entity.Waiter;
import md.utm.dininghall.core.repository.CustomerOrderRepository;
import md.utm.dininghall.core.repository.RestaurantTableRepository;
import md.utm.dininghall.core.repository.RestaurantTableStatusRepository;
import md.utm.dininghall.core.repository.WaiterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import md.utm.dininghall.core.constant.RestaurantTableStatusCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

@Slf4j
@Service
@RequiredArgsConstructor
public class SimulationDataService {
    private final CustomerOrderRepository customerOrderRepository;
    private final RestaurantTableStatusRepository tableStatusRepository;
    private final RestaurantTableRepository tableRepository;
    private final WaiterRepository waiterRepository;

    @Value("${app.waitersNum}")
    private int waitersNum;

    @Value("${app.tablesNum}")
    private int tablesNum;

    @Transactional
    public void invoke() {
        customerOrderRepository.deleteAll(customerOrderRepository.findAll());
        tableRepository.deleteAll(tableRepository.findAll());
        waiterRepository.deleteAll(waiterRepository.findAll());

        generateTablesData();
        generateWaitersData();
    }

    private void generateTablesData() {
        log.info("Generating restaurant tables data...");
        final var tableStatus = tableStatusRepository.findByCode(RestaurantTableStatusCode.WAITING_FOR_WAITER);

        final var tables = IntStream.range(1, tablesNum + 1).mapToObj((i) -> {
            final var table = new RestaurantTable();
            table.setCode(UUID.randomUUID().toString());
            table.setStatus(tableStatus);
            return table;
        }).collect(toList());

        tableRepository.saveAll(tables);
    }

    private void generateWaitersData() {
        log.info("Generating restaurant waiters data...");
        final var waiters = IntStream.range(1, waitersNum + 1).mapToObj((i) -> {
            final var waiter = new Waiter();
            waiter.setCode(UUID.randomUUID().toString());
            return waiter;
        }).collect(toList());

        waiterRepository.saveAll(waiters);
    }
}
