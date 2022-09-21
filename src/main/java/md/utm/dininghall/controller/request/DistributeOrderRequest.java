package md.utm.dininghall.controller.request;

import lombok.Value;

import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.List;

@Value
public class DistributeOrderRequest {
    @NotNull
    Long orderId;

    @NotNull
    Long tableId;

    @NotNull
    Long waiterId;

    @NotNull
    List<Long> items;

    @NotNull
    Integer priority;

    @NotNull
    Integer maxWait;

    @NotNull
    Instant pickupTime;

    @NotNull
    Integer cookingTime;

    @NotNull
    List<CookingDetailsItem> cookingDetails;

    @Value
    public static class CookingDetailsItem {
        Long foodId;
        Long cookId;
    }
}
