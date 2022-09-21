package md.utm.dininghall.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.utm.dininghall.core.entity.BaseIdCodeDrivenEntity;
import com.utm.dininghall.core.entity.CustomerOrder;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import javax.annotation.PostConstruct;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SendCustomerOrderToKitchenService {
    private final ObjectMapper objectMapper;

    @Value("${app.kitchenOrderEndpointUrl}")
    private String kitchenOrderEndpointUrl;

    private WebClient client;

    @PostConstruct
    public void postConstruct() {
        client = WebClient.builder()
                .baseUrl(kitchenOrderEndpointUrl).build();
    }

    @Transactional
    public void invoke(CustomerOrder order) {
        log.info("Sending order '{}' to kitchen...", order.getId());

        client.post()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(createRequest(order))
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    @SneakyThrows
    private String createRequest(CustomerOrder order) {
        final var dishes = order.getDishes().stream()
                .map(BaseIdCodeDrivenEntity::getId).collect(Collectors.toList());

        final var request = Request.builder()
                .orderId(order.getId())
                .tableId(order.getTable().getId())
                .waiterId(order.getWaiter().getId())
                .items(dishes)
                .priority(order.getPriority())
                .maxWait(order.getMaxWait())
                .pickUpTime(order.getPickUpTime())
                .build();

        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(request);
    }

    @Getter
    @Builder
    private static class Request {
        long orderId;
        long tableId;
        long waiterId;
        List<Long> items;
        int priority;
        double maxWait;
        Instant pickUpTime;
    }
}
