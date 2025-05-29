package uz.pdp.clients.payment;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.clients.dtos.OrderFullDTO;
import uz.pdp.clients.dtos.PaymentCreateDTO;

@FeignClient(value = "PAYMENTSERVICE",path = "/api/payment")
public interface PaymentClient {

    @PostMapping
    ResponseEntity<?> createPayment(@RequestBody OrderFullDTO orderFullDTO);

    @DeleteMapping("/{orderId}")
    ResponseEntity<?> rollbackPayment(@PathVariable Long orderId);
}
