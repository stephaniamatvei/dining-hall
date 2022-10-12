package md.utm.dininghall.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import md.utm.dininghall.service.dto.CustomerOrderDto;
import md.utm.dininghall.service.dto.DishDto;
import md.utm.dininghall.service.dto.RestaurantTableDto;
import md.utm.dininghall.service.dto.WaiterDto;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

import static java.util.stream.Collectors.toList;
import static md.utm.dininghall.service.dto.RestaurantTableDto.RestaurantTableStatusCode.*;


@Slf4j
@Service
@RequiredArgsConstructor
public class SimulationService {
    private final SimulationDataService simulationDataService;
    private final CustomerOrderGenerateService orderGenerateService;
    private List<RestaurantTableDto> restaurantTables;
    private List<CustomerOrderDto> customerOrders;

    @EventListener(ApplicationReadyEvent.class)
    public void invokeSimulation() {
        log.info("Starting simulation...");

        restaurantTables = simulationDataService.generateRestaurantTables();
        customerOrders = Collections.synchronizedList(new ArrayList<>());
        simulationDataService.generateWaiters().forEach(w -> new Thread(() -> startWaiterThread(w)).start());
    }

    public void distributeOrder(Long orderId, List<Long> cookedDishIds) {
        log.info("Distributing order '{}'...", orderId);

        final var order = customerOrders.stream()
                .filter((i) -> i.getId().equals(orderId))
                .findAny()
                .orElseThrow();

        final var rating = calculateRating(order.getMaxWait(), order.getPickUpTime());

        final var orderDishIds = order.getDishes().stream()
                .map(DishDto::getId)
                .sorted()
                .collect(toList());

        final var sortedCookedDishIds = cookedDishIds.stream()
                .sorted()
                .collect(toList());

        if (orderDishIds.equals(sortedCookedDishIds)) {
            final var table = order.getTable();
            final var lock = table.getLock();

            lock.lock();
            table.setStatus(ORDER_SERVED);
            lock.unlock();

            log.info("Successfully distributed order '{}' with rating '{}'...", orderId, rating);
        }
    }

    private void startWaiterThread(WaiterDto waiter) {
        final Predicate<RestaurantTableDto> predicate =
                (t) -> t.getStatus().equals(WAITING_FOR_WAITER) && t.getLock().tryLock();

        restaurantTables.stream().filter(predicate).findAny().ifPresent((t) -> {
            log.info("Waiter '{}' serves table '{}'", waiter.getId(), t.getId());
            t.setStatus(WAITING_FOR_ORDER);
            t.getLock().unlock();

            final var order = orderGenerateService.invoke(t, waiter);
            customerOrders.add(order);
            startWaiterThread(waiter);
        });
    }

    private int calculateRating(double maxWait, Instant pickupTs) {
        final var actualWait = ChronoUnit.SECONDS.between(pickupTs, Instant.now());

        if (actualWait <= maxWait) {
            return 5;
        }

        if (actualWait <= maxWait * 1.1) {
            return 4;
        }

        if (actualWait <= maxWait * 1.2) {
            return 3;
        }

        if (actualWait <= maxWait * 1.3) {
            return 2;
        }

        if (actualWait <= maxWait * 1.4) {
            return 1;
        }

        return 0;
    }
}
