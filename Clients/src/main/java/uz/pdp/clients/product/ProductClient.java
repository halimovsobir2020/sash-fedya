package uz.pdp.clients.product;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import uz.pdp.clients.dtos.OrderItemDTO;

import java.util.List;

@FeignClient("PRODUCTSERVICE")
public interface ProductClient {

    @PostMapping("/leftover/update")
    ResponseEntity<?> updateProductLeftOver(@RequestBody List<OrderItemDTO> orderItems);

}
