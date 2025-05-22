package uz.pdp.clients.payment;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import uz.pdp.clients.dtos.PaymentCreateDTO;

@FeignClient("PAYMENTSERVICE")
public interface PaymentClient {

    @PostMapping
    ResponseEntity<?> createPayment(@RequestBody PaymentCreateDTO paymentCreateDTO);

}
