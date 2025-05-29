package uz.pdp.productservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import uz.pdp.clients.dtos.OrderFullDTO;
import uz.pdp.clients.dtos.OutboxStatus;
import uz.pdp.clients.kafkaconfig.KafkaTopics;
import uz.pdp.productservice.dto.ProductDTO;
import uz.pdp.productservice.entity.Category;
import uz.pdp.productservice.kafkaconfig.outbox.Outbox;
import uz.pdp.productservice.entity.Product;
import uz.pdp.productservice.repo.CategoryRepository;
import uz.pdp.productservice.kafkaconfig.outbox.OutboxRepository;
import uz.pdp.productservice.repo.ProductRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final LeftOverService leftOverService;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final OutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;

    //CRUD OPERATOINS>>>>>>>
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
    //CRUD OPERATOINS>>>>>>>


    @SneakyThrows
    public void updateLeftOver(OrderFullDTO orderFullDTO) {
        try {
            leftOverService.updateLeftOverStep2(orderFullDTO);
        } catch (ObjectOptimisticLockingFailureException e) {
            updateLeftOver(orderFullDTO);
        } catch (Exception e) {
            outboxRepository.save(
                    Outbox.builder()
                            .topic(KafkaTopics.ORDER_ROLLBACK)
                            .status(OutboxStatus.PENDING)
                            .payload(orderFullDTO.getOrderId().toString())
                            .aggregateId(orderFullDTO.getOrderId())
                            .aggregateType("Long")
                            .build()
            );
        }
    }

    public void rollback(OrderFullDTO orderFullDTO) {
        try {
            leftOverService.compensateActionStep2(orderFullDTO);
        } catch (ObjectOptimisticLockingFailureException e) {
            rollback(orderFullDTO);
        }
    }
}
