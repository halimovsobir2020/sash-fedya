package uz.pdp.productservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.pdp.clients.dtos.OrderItemDTO;
import uz.pdp.clients.dtos.OrderItemFull;
import uz.pdp.productservice.entity.Product;
import uz.pdp.productservice.repo.ProductRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LeftoverService {

    private final ProductRepository productRepository;

    @Transactional
    public List<OrderItemFull> updateLeftover2(List<OrderItemDTO> orderItemDTOS) {
        List<OrderItemFull> orderItemFullList = new ArrayList<>();
        List<Long> productIds = orderItemDTOS.stream().map(OrderItemDTO::getProductId).toList();
        List<Product> products = productRepository.findAllByIdIn(productIds);
        for (OrderItemDTO orderItemDTO : orderItemDTOS) {
            Product product = products.stream().filter((item) -> item.getId().equals(orderItemDTO.getProductId())).findFirst().orElseThrow();
            product.setLeftOver(product.getLeftOver() - orderItemDTO.getQuantity());
            orderItemFullList.add(new OrderItemFull(orderItemDTO.getProductId(), orderItemDTO.getQuantity(), product.getPrice(), product.getName()));
        }
        productRepository.saveAll(products);
        return orderItemFullList;
    }

    public void rollback2(List<OrderItemDTO> orderItemDTOS) {
        List<Long> productIds = orderItemDTOS.stream().map(OrderItemDTO::getProductId).toList();
        List<Product> products = productRepository.findAllByIdIn(productIds);
        for (OrderItemDTO orderItemDTO : orderItemDTOS) {
            Product product = products.stream()
                    .filter(item -> item.getId().equals(orderItemDTO.getProductId()))
                    .findFirst().orElseThrow();
            product.setLeftOver(product.getLeftOver() + orderItemDTO.getQuantity());
        }
        productRepository.saveAll(products);
    }

}
