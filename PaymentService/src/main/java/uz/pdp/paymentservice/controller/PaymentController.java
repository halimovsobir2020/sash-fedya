package uz.pdp.paymentservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.clients.dtos.PaymentCreateDTO;
import uz.pdp.paymentservice.entity.Payment;
import uz.pdp.paymentservice.service.PaymentService;

@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<?> createPayment(@RequestBody PaymentCreateDTO paymentCreateDTO) {
        Payment payment = paymentService.create(paymentCreateDTO);
        return new ResponseEntity<>(payment, HttpStatus.CREATED);
    }

    @DeleteMapping("{orderId}")
    public ResponseEntity<?> rollbackPayment(@PathVariable Long orderId) {
        paymentService.rollbackPayment(orderId);
        return ResponseEntity.ok().build();
    }
}
