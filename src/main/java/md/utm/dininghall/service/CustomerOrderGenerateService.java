package md.utm.dininghall.service;

import md.utm.dininghall.core.entity.CustomerOrder;
import md.utm.dininghall.core.entity.Dish;
import md.utm.dininghall.core.entity.RestaurantTable;
import md.utm.dininghall.core.entity.Waiter;
import md.utm.dininghall.core.repository.CustomerOrderRepository;
import md.utm.dininghall.core.repository.DishRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import static md.utm.dininghall.service.utils.NumberUtils.getRandomNumber;
import static java.util.stream.Collectors.toList;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerOrderGenerateService {
    private final DishRepository dishRepository;
    private final CustomerOrderRepository orderRepository;
    private final SendCustomerOrderToKitchenService sendCustomerOrderToKitchenService;

    @Async
    @Transactional
    public void invoke(RestaurantTable table, Waiter waiter) {
        log.info("Generating order for table '{}' and waiter '{}'...", table.getId(), waiter.getId());

        final var dishes = generateDishes();
        final var order = new CustomerOrder();

        order.setCode(UUID.randomUUID().toString());
        order.setPriority(generatePriority());
        order.setPickUpTime(Instant.now());
        order.setTable(table);
        order.setWaiter(waiter);
        order.setDishes(dishes);
        order.setMaxWait(getMaxWait(dishes));

        final var savedOrder = orderRepository.save(order);
        sendCustomerOrderToKitchenService.invoke(savedOrder);
    }

    private List<Dish> generateDishes() {
        final var dishes = (List<Dish>) dishRepository.findAll();
        Collections.shuffle(dishes);
        return dishes.stream().limit(getRandomNumber(1, 10)).collect(toList());
    }

    private int generatePriority() {
        return getRandomNumber(1, 5);
    }

    private double getMaxWait(List<Dish> dishes) {
        return dishes.stream()
                .max(Comparator.comparingDouble(Dish::getWaitTime))
                .orElseThrow()
                .getWaitTime() * 1.3;
    }
}
