package uz.pdp.productservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.pdp.clients.dtos.OrderItemDTO;
import uz.pdp.clients.dtos.OrderItemFull;
import uz.pdp.productservice.dto.ProductDTO;
import uz.pdp.productservice.entity.Category;
import uz.pdp.productservice.entity.Product;
import uz.pdp.productservice.repo.CategoryRepository;
import uz.pdp.productservice.repo.ProductRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

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


    public List<OrderItemFull> updateLeftOver(List<OrderItemFull> orderItems)  {
        for (int i = 0; i < 3; i++) {
            try {
                return updateLeftOverStep2(orderItems);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ObjectOptimisticLockingFailureException e) {
                System.out.println("catchga tushdi");
                continue;
            }
        }
        throw new RuntimeException("too many requests");
    }

    @Transactional
    public List<OrderItemFull> updateLeftOverStep2(List<OrderItemFull> orderItems) throws InterruptedException {
        List<Long> productIds = orderItems.stream().map(OrderItemFull::getProductId).toList();
        List<Product> products = productRepository.findAllByIdIn(productIds);
        for (OrderItemFull orderItem : orderItems) {
            Product product = products.stream().filter(item -> item.getId().equals(orderItem.getProductId())).findFirst().orElseThrow();
            if (product.getLeftOver() < orderItem.getQuantity()) {
                throw new RuntimeException("not enough product left over");
            }
            product.setLeftOver(product.getLeftOver() - orderItem.getQuantity());
            orderItem.setPrice(product.getPrice());
            orderItem.setProductName(product.getName());
        }
        productRepository.saveAll(products);
        return orderItems;
    }


    public void compensateAction(List<OrderItemDTO> orderItemDTOS) {
        List<Long> productIds = orderItemDTOS.stream().map(OrderItemDTO::getProductId).toList();
        List<Product> products = productRepository.findAllByIdIn(productIds);
        for (OrderItemDTO orderItemDTO : orderItemDTOS) {
            Product product = products.stream().filter(item -> item.getId().equals(orderItemDTO.getProductId())).findFirst().orElseThrow();
            product.setLeftOver(product.getLeftOver() + orderItemDTO.getQuantity());
        }
        productRepository.saveAll(products);
    }
}
