package uz.pdp.inventoryservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.clients.dtos.OrderFullDTO;
import uz.pdp.clients.dtos.OrderItemDTO;
import uz.pdp.inventoryservice.service.InventoryService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;


    @PostMapping("/outcome")
    public ResponseEntity<?> orderOutcome(@RequestBody OrderFullDTO orderFullDTO) {
        inventoryService.orderUpdates(orderFullDTO);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/rollback/outcome/{orderId}")
    public ResponseEntity<?> rollbackOutcome(@PathVariable Long orderId) {
        inventoryService.rollbackOutcome(orderId);
        return ResponseEntity.ok().build();
    }

}
