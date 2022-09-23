package md.utm.dininghall.service;

import md.utm.dininghall.core.entity.BaseIdCodeDrivenEntity;
import md.utm.dininghall.core.repository.CustomerOrderRepository;
import md.utm.dininghall.core.repository.RestaurantTableStatusRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static md.utm.dininghall.core.constant.RestaurantTableStatusCode.ORDER_SERVED;
import static java.util.stream.Collectors.toList;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerOrderDistributionService {
    private final CustomerOrderRepository orderRepository;
    private final RestaurantTableStatusRepository restaurantTableStatusRepository;

    @Transactional
    public void invoke(Long orderId, List<Long> cookedDishIds) {
        log.info("Distributing order '{}'...", orderId);

        final var order = orderRepository.findById(orderId).orElseThrow();
        final var orderDishIds = order.getDishes().stream()
                .map(BaseIdCodeDrivenEntity::getId)
                .sorted()
                .collect(toList());

        final var sortedCookedDishIds = cookedDishIds.stream()
                .sorted()
                .collect(toList());

        if (orderDishIds.equals(sortedCookedDishIds)) {
            final var newTableStatus = restaurantTableStatusRepository.findByCode(ORDER_SERVED);
            order.getTable().setStatus(newTableStatus);

            log.info("Successfully distributed order '{}'...", orderId);
        }
    }
}
