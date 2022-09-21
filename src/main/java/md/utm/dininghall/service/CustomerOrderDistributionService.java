package md.utm.dininghall.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerOrderDistributionService {

    public void invoke(Long orderId) {
        log.info("Successfully distributed order '{}'", orderId);
    }
}
