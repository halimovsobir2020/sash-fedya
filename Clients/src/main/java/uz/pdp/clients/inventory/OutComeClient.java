package uz.pdp.clients.inventory;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import uz.pdp.clients.dtos.OrderFullDTO;

@FeignClient(value = "INVENTORYSERVICE",path = "/api/inventory")
public interface OutComeClient {

    @PostMapping("/outcome")
    ResponseEntity<?> orderOutcome(@RequestBody OrderFullDTO orderFullDTO);

    @PostMapping("/rollback/{orderId}")
    ResponseEntity<?> rollback(@PathVariable("orderId") Long orderId);
}
