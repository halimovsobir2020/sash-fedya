package uz.pdp.productservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.clients.dtos.OrderItemDTO;
import uz.pdp.clients.dtos.OrderItemFull;
import uz.pdp.productservice.dto.ProductDTO;
import uz.pdp.productservice.entity.Product;
import uz.pdp.productservice.service.ProductService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService service) {
        this.productService = service;
    }

    @PostMapping("/rollback")
    public ResponseEntity<?> rollback(@RequestBody List<OrderItemDTO> orderItemDTOS) {
        productService.compensateAction(orderItemDTOS);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/leftover/update")
    public ResponseEntity<List<OrderItemFull>> updateProductLeftOver(@RequestBody List<OrderItemFull> orderItems) {
        List<OrderItemFull> orderItemDTOS = productService.updateLeftOver(orderItems);
        return ResponseEntity.ok().body(orderItemDTOS);
    }

    @PostMapping
    public ResponseEntity<Product> create(@RequestBody ProductDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.create(dto));
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAll() {
        return ResponseEntity.ok(productService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> update(@PathVariable Long id, @RequestBody ProductDTO dto) {
        return ResponseEntity.ok(productService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }


}
