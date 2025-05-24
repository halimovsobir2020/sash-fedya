package uz.pdp.inventoryservice.controller;

import uz.pdp.clients.dtos.OrderItemDTO;
import uz.pdp.inventoryservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;


    @PostMapping("/outcome/{orderId}")
    public ResponseEntity<?> orderOutcome(@RequestBody List<OrderItemDTO> orderItems, @PathVariable("orderId") Long orderId) {
        inventoryService.orderUpdates(orderItems,orderId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/rollback/{orderId}")
    public ResponseEntity<?> rollback(@PathVariable("orderId") Long orderId) {
        inventoryService.rollback(orderId);
        return ResponseEntity.ok().build();
    }





}
