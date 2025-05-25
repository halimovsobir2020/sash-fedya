package uz.pdp.clients.inventory;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import uz.pdp.clients.dtos.OrderItemDTO;

import java.util.List;

@FeignClient(value = "INVENTORYSERVICE", path = "/api/v1/inventory")
public interface InventoryClient {

    @PostMapping("/outcome/{orderId}")
    ResponseEntity<?> orderOutcome(@RequestBody List<OrderItemDTO> orderItems, @PathVariable("orderId") Long orderId);

    @PostMapping("/rollback/outcome/{orderId}")
    ResponseEntity<?> rollbackOutcome(@PathVariable Long orderId);

}
