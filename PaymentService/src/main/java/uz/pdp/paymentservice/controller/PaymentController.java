package uz.pdp.paymentservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.clients.dtos.OrderFullDTO;
import uz.pdp.clients.dtos.PaymentCreateDTO;
import uz.pdp.paymentservice.entity.Payment;
import uz.pdp.paymentservice.service.PaymentService;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<?> createPayment(@RequestBody OrderFullDTO orderFullDTO) {
        Payment payment = paymentService.create(orderFullDTO);
        return new ResponseEntity<>(payment, HttpStatus.CREATED);
    }

}
