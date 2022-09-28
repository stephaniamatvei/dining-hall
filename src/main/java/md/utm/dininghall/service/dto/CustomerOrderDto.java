package md.utm.dininghall.service.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Setter
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CustomerOrderDto {
    @EqualsAndHashCode.Include
    private Long id;
    private int priority;
    private double maxWait;
    private Instant pickUpTime;
    private RestaurantTableDto table;
    private WaiterDto waiter;
    private List<DishDto> dishes;
}
