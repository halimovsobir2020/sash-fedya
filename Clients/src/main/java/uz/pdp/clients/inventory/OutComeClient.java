package uz.pdp.clients.inventory;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import uz.pdp.clients.dtos.OrderItemDTO;

import java.util.List;

@FeignClient("INVENTORYSERVICE")
public interface OutComeClient {

    @PostMapping("/api/v1/inventory/outcome/{orderId}")
    ResponseEntity<?> orderOutcome(@RequestBody List<OrderItemDTO> orderItems, @PathVariable("orderId") Long orderId);

    @PostMapping("/api/v1/inventory/rollback/{orderId}")
    ResponseEntity<?> rollback(@PathVariable("orderId") Long orderId);
}
