package md.utm.dininghall.service.dto;

import lombok.*;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Getter
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class RestaurantTableDto {

    @NonNull
    @EqualsAndHashCode.Include
    private Long id;

    @NonNull
    @Setter
    private RestaurantTableStatusCode status;

    private final Lock lock = new ReentrantLock();

    public enum RestaurantTableStatusCode {
        WAITING_FOR_WAITER,
        WAITING_FOR_ORDER,
        ORDER_SERVED
    }
}
