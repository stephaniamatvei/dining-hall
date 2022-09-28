package md.utm.dininghall.controller;

import lombok.RequiredArgsConstructor;
import md.utm.dininghall.controller.request.DistributeOrderRequest;
import md.utm.dininghall.service.SimulationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("distribution")
public class DistributionController {
    private final SimulationService simulationService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> distribution(@RequestBody DistributeOrderRequest request) {
        simulationService.distributeOrder(request.getOrderId(), request.getItems());
        return ResponseEntity.noContent().build();
    }
}
