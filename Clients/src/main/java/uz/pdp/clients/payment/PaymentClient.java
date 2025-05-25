package uz.pdp.clients.payment;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import uz.pdp.clients.dtos.PaymentCreateDTO;

@FeignClient(value = "PAYMENTSERVICE", path = "/api/v1/payment")
public interface PaymentClient {

    @PostMapping()
    ResponseEntity<?> savePayment(@RequestBody PaymentCreateDTO paymentCreateDTO);

    @DeleteMapping("/{orderId}")
    ResponseEntity<?> rollbackPayment(@PathVariable Long orderId);

}
