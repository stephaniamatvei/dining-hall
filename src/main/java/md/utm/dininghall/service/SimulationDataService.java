package md.utm.dininghall.service;

import lombok.extern.slf4j.Slf4j;
import md.utm.dininghall.service.dto.RestaurantTableDto;
import md.utm.dininghall.service.dto.WaiterDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;
import static md.utm.dininghall.service.dto.RestaurantTableDto.RestaurantTableStatusCode.WAITING_FOR_WAITER;

@Slf4j
@Service
public class SimulationDataService {
    @Value("${app.tablesNum}")
    private int tablesNum;

    @Value("${app.waitersNum}")
    private int waitersNum;

    public List<RestaurantTableDto> generateRestaurantTables() {
        log.info("Generating restaurant tables data...");

        return IntStream.range(1, tablesNum + 1)
                .mapToObj((i) -> new RestaurantTableDto((long) i, WAITING_FOR_WAITER))
                .collect(toList());
    }

    public List<WaiterDto> generateWaiters() {
        log.info("Generating restaurant waiters data...");

        return IntStream.range(1, waitersNum + 1)
                .mapToObj((i) -> new WaiterDto((long) i))
                .collect(toList());
    }
}
