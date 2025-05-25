package uz.pdp.productservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.pdp.clients.dtos.OrderItemDTO;
import uz.pdp.clients.dtos.OrderItemFull;
import uz.pdp.productservice.entity.Product;
import uz.pdp.productservice.repo.ProductRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LeftOverService {
    private final ProductRepository productRepository;

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


    @Transactional
    public void compensateActionStep2(List<OrderItemDTO> orderItemDTOS) {
        List<Long> productIds = orderItemDTOS.stream().map(OrderItemDTO::getProductId).toList();
        List<Product> products = productRepository.findAllByIdIn(productIds);
        for (OrderItemDTO orderItemDTO : orderItemDTOS) {
            Product product = products.stream().filter(item -> item.getId().equals(orderItemDTO.getProductId())).findFirst().orElseThrow();
            product.setLeftOver(product.getLeftOver() + orderItemDTO.getQuantity());
        }
        productRepository.saveAll(products);
    }

}
