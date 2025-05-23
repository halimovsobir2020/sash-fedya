package uz.pdp.productservice.service;

import jakarta.persistence.OptimisticLockException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.pdp.clients.dtos.OrderItemDTO;
import uz.pdp.clients.dtos.ProductInfoDTO;
import uz.pdp.productservice.dto.ProductDTO;
import uz.pdp.productservice.entity.Category;
import uz.pdp.productservice.entity.Product;
import uz.pdp.productservice.repo.CategoryRepository;
import uz.pdp.productservice.repo.ProductRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

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

    @Transactional
    public List<OrderItemDTO> updateLeftOver(List<OrderItemDTO> orderItems) throws Exception {
        for (OrderItemDTO orderItem : orderItems) {
            Product product = changeLeftOverOfProduct(orderItem);
            orderItem.setPrice(product.getPrice());
            orderItem.setProductName(product.getName());
        }
        return orderItems;
    }

    private Product changeLeftOverOfProduct(OrderItemDTO orderItem) throws Exception {
        Product product = productRepository.findById(orderItem.getProductId()).orElseThrow();
        if(product.getLeftOver() < orderItem.getQuantity() ) {
            throw new Exception("not enough product left over");
        }
        product.setLeftOver(product.getLeftOver() - orderItem.getQuantity());
        try {
            productRepository.save(product);
        } catch (OptimisticLockException e) {
            changeLeftOverOfProduct(orderItem);
        }
        return product;
    }

    public void compensateAction(List<OrderItemDTO> orderItemDTOS) {
        List<Long> productIds = orderItemDTOS.stream().map(item -> item.getProductId()).toList();
        List<Product> products = productRepository.findAllByIdIn(productIds);
        for (OrderItemDTO orderItemDTO : orderItemDTOS) {
            Product product = products.stream().filter(item -> item.getId().equals(orderItemDTO.getProductId())).findFirst().orElseThrow();
            product.setLeftOver(product.getLeftOver() + orderItemDTO.getQuantity());
        }
        productRepository.saveAll(products);
    }
}
