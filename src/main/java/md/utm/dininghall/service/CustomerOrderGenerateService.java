package md.utm.dininghall.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import md.utm.dininghall.service.dto.CustomerOrderDto;
import md.utm.dininghall.service.dto.DishDto;
import md.utm.dininghall.service.dto.RestaurantTableDto;
import md.utm.dininghall.service.dto.WaiterDto;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import static java.util.stream.Collectors.toList;
import static md.utm.dininghall.service.utils.NumberUtils.getRandomNumber;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerOrderGenerateService {
    private static final List<DishDto> DISHES = List.of(
            new DishDto(1L, "PIZZA", 20),
            new DishDto(2L, "SALAD", 10),
            new DishDto(3L, "ZEAMA", 7),
            new DishDto(4L, "SCALLOP_SASHIMI_MEYER_LEMON", 32),
            new DishDto(5L, "ISLAND_DUCK_MUSTARD", 35),
            new DishDto(6L, "WAFFLES", 10),
            new DishDto(7L, "AUBERGINE", 20),
            new DishDto(8L, "LASAGNA", 30),
            new DishDto(9L, "BURGER", 15),
            new DishDto(10L, "GYROS", 15),
            new DishDto(11L, "KEBAB", 15),
            new DishDto(12L, "UNAGI_MAKI", 20),
            new DishDto(13L, "TOBACCO_CHICKEN", 30)
    );

    private final SendCustomerOrderToKitchenService sendCustomerOrderToKitchenService;
    private final AtomicLong orderId = new AtomicLong(1L);

    public CustomerOrderDto invoke(RestaurantTableDto table, WaiterDto waiter) {
        final var newOrderId = orderId.getAndIncrement();
        log.info("Generating order '{}' for table '{}' and waiter '{}'...", newOrderId, table.getId(), waiter.getId());

        final var dishes = generateDishes();
        final var order = new CustomerOrderDto();

        order.setId(newOrderId);
        order.setPriority(generatePriority());
        order.setPickUpTime(Instant.now());
        order.setTable(table);
        order.setWaiter(waiter);
        order.setDishes(dishes);
        order.setMaxWait(getMaxWait(dishes));

        sendCustomerOrderToKitchenService.invoke(order);
        return order;
    }

    private List<DishDto> generateDishes() {
        final var clone = new ArrayList<>(DISHES);
        Collections.shuffle(clone);
        return clone.stream().limit(getRandomNumber(1, 10)).collect(toList());
    }

    private int generatePriority() {
        return getRandomNumber(1, 5);
    }

    private double getMaxWait(List<DishDto> dishes) {
        return dishes.stream()
                .max(Comparator.comparingDouble(DishDto::getWaitTime))
                .orElseThrow()
                .getWaitTime() * 1.3;
    }
}
