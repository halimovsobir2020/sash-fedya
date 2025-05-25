package uz.pdp.orderservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.pdp.orderservice.dto.OrderDTO;
import uz.pdp.orderservice.entity.Order;
import uz.pdp.orderservice.service.OrderService;

@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<Order> saveOrder(@RequestBody OrderDTO orderDTO){
        Order order = orderService.saveOrder(orderDTO);
        return ResponseEntity.ok(order);
    }

}
