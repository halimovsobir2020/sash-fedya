package uz.pdp.clients.product;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import uz.pdp.clients.dtos.OrderItemDTO;

import java.util.List;

@FeignClient("PRODUCTSERVICE")
public interface ProductClient {

    @PostMapping("/api/v1/product/leftover/update")
    ResponseEntity<List<OrderItemDTO>> updateProductLeftOver(@RequestBody List<OrderItemDTO> orderItems);

    @PostMapping("/api/v1/product/rollback")
    ResponseEntity<?> rollback(@RequestBody List<OrderItemDTO> orderItemDTOS);

}
