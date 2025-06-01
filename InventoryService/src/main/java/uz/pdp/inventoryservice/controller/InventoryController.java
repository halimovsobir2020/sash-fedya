package uz.pdp.inventoryservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.clients.dtos.OrderFullDTO;
import uz.pdp.inventoryservice.service.InventoryService;

@RestController
@RequestMapping("api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;


    @PostMapping("/outcome")
    public ResponseEntity<?> orderOutcome(@RequestBody OrderFullDTO orderFullDTO)  {
        inventoryService.orderUpdates(orderFullDTO);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/rollback/{orderId}")
    public ResponseEntity<?> rollback(@PathVariable("orderId") Long orderId) {
        inventoryService.rollback(orderId);
        return ResponseEntity.ok().build();
    }





}
