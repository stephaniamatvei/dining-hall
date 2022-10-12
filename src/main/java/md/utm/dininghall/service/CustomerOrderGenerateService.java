package md.utm.dininghall.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import md.utm.dininghall.service.dto.CustomerOrderDto;
import md.utm.dininghall.service.dto.DishDto;
import md.utm.dininghall.service.dto.RestaurantTableDto;
import md.utm.dininghall.service.dto.WaiterDto;
import org.springframework.stereotype.Service;

import java.nio.file.Paths;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

import static java.util.stream.Collectors.toList;
import static md.utm.dininghall.service.utils.NumberUtils.getRandomNumber;

@Slf4j
@Service
public class CustomerOrderGenerateService {
    private final SendCustomerOrderToKitchenService sendCustomerOrderToKitchenService;
    private final AtomicLong orderId = new AtomicLong(1L);
    private final List<DishDto> dishes;

    @SneakyThrows
    public CustomerOrderGenerateService(SendCustomerOrderToKitchenService sendCustomerOrderToKitchenService, ObjectMapper objectMapper) {
        this.sendCustomerOrderToKitchenService = sendCustomerOrderToKitchenService;
        dishes = Arrays.asList(objectMapper.readValue(Paths.get("src/main/resources/dishes.json").toFile(), DishDto[].class));;
    }

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
        final var clone = new ArrayList<>(dishes);
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
