package uz.pdp.productservice.service;

import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.pdp.clients.dtos.OrderFullDTO;
import uz.pdp.clients.dtos.OrderItemDTO;
import uz.pdp.clients.dtos.OrderItemFull;
import uz.pdp.clients.dtos.PaymentCreateDTO;
import uz.pdp.clients.payment.PaymentClient;
import uz.pdp.productservice.dto.ProductDTO;
import uz.pdp.productservice.entity.Category;
import uz.pdp.productservice.entity.Product;
import uz.pdp.productservice.repo.CategoryRepository;
import uz.pdp.productservice.repo.ProductRepository;
import uz.pdp.productservice.util.OptimisticLockRetrier;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final LeftoverService leftoverService;
    private final OptimisticLockRetrier optimisticLockRetrier;
    private final PaymentClient paymentClient;


    public Product create(ProductDTO dto) {
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Product product = new Product();
        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        product.setDescription(dto.getDescription());
        product.setCategory(category);

        return productRepository.save(product);
    }

    public List<Product> getAll() {
        return productRepository.findAll();
    }

    public Product getById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public Product update(Long id, ProductDTO dto) {
        Product product = getById(id);
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        product.setDescription(dto.getDescription());
        product.setCategory(category);

        return productRepository.save(product);
    }

    public void delete(Long id) {
        productRepository.deleteById(id);
    }

    public void updateProductLeftover(OrderFullDTO orderFullDTO) {
        optimisticLockRetrier.retry(() ->
                leftoverService.updateLeftover2(orderFullDTO)
        );
    }

    public void rollbackProductLeftover(List<OrderItemDTO> orderItemDTOS) {
        optimisticLockRetrier.retry(() -> leftoverService.rollback2(orderItemDTOS));
    }


}
