package uz.pdp.clients.product;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import uz.pdp.clients.dtos.OrderItemDTO;
import uz.pdp.clients.dtos.OrderItemFull;

import java.util.List;

@FeignClient(value = "PRODUCTSERVICE",path = "/api/v1/product")
public interface ProductClient {

    @PostMapping("/leftover/update")
    ResponseEntity<List<OrderItemFull>> updateProductLeftOver(@RequestBody List<OrderItemFull> orderItems);

    @PostMapping("/rollback")
    ResponseEntity<?> rollback(@RequestBody List<OrderItemDTO> orderItemDTOS);

}
