package md.utm.dininghall.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import md.utm.dininghall.service.dto.CustomerOrderDto;
import md.utm.dininghall.service.dto.DishDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
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

    public void invoke(CustomerOrderDto order) {
        log.info("Sending order '{}' to kitchen...", order.getId());

        client.post()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(createRequest(order))
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    @SneakyThrows
    private String createRequest(CustomerOrderDto order) {
        final var dishes = order.getDishes().stream()
                .map(DishDto::getId).collect(Collectors.toList());

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
