package md.utm.dininghall.controller;

import md.utm.dininghall.controller.request.DistributeOrderRequest;
import md.utm.dininghall.service.CustomerOrderDistributionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("distribution")
public class DistributionController {
    private final CustomerOrderDistributionService distributionService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> distribution(@RequestBody DistributeOrderRequest request) {
        distributionService.invoke(request.getOrderId());
        return ResponseEntity.noContent().build();
    }
}
