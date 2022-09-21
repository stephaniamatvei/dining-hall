package md.utm.dininghall.service;

import md.utm.dininghall.core.entity.CustomerOrder;
import md.utm.dininghall.core.entity.Dish;
import md.utm.dininghall.core.entity.RestaurantTable;
import md.utm.dininghall.core.entity.Waiter;
import md.utm.dininghall.core.repository.CustomerOrderRepository;
import md.utm.dininghall.core.repository.DishRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerOrderGenerateService {
    private final DishRepository dishRepository;
    private final CustomerOrderRepository orderRepository;

    @Transactional
    public CustomerOrder invoke(RestaurantTable table, Waiter waiter) {
        log.info("Generating order for table '{}' and waiter '{}'...", table.getId(), waiter.getId());

        final var order = new CustomerOrder();
        order.setCode(UUID.randomUUID().toString());
        order.setPriority(generatePriority());
        order.setMaxWait(generateMaxWait());
        order.setPickUpTime(Instant.now());
        order.setTable(table);
        order.setWaiter(waiter);
        order.setDishes(generateDishes());

        return orderRepository.save(order);
    }

    private List<Dish> generateDishes() {
        return ((List<Dish>) dishRepository.findAll()).stream()
                .limit(3).collect(Collectors.toList()); // TODO: hardcoded
    }

    private int generatePriority() {
        return 1; // TODO: hardcoded
    }

    private int generateMaxWait() {
        return 45; // TODO: hardcoded
    }
}
