package uz.pdp.clients.payment;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import uz.pdp.clients.dtos.PaymentCreateDTO;

@FeignClient("PAYMENTSERVICE")
public interface PaymentClient {

    @PostMapping("/api/v1/payment")
    ResponseEntity<?> createPayment(@RequestBody PaymentCreateDTO paymentCreateDTO);

    @DeleteMapping("/api/v1/payment/{orderId}")
    ResponseEntity<?> rollbackPayment(@PathVariable Long orderId);
}
