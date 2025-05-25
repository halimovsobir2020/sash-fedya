package uz.pdp.productservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import uz.pdp.clients.dtos.OrderFullDTO;
import uz.pdp.clients.dtos.OrderItemDTO;
import uz.pdp.clients.dtos.OrderItemFull;
import uz.pdp.clients.payment.PaymentClient;
import uz.pdp.productservice.entity.Product;
import uz.pdp.productservice.repo.ProductRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LeftoverService {

    private final ProductRepository productRepository;
    private final PaymentClient paymentClient;

    @Transactional
    public void updateLeftover2(OrderFullDTO orderFullDTO) {
        List<Long> productIds = orderFullDTO.getOrderItemFullList().stream().map(OrderItemFull::getProductId).toList();
        List<Product> products = productRepository.findAllByIdIn(productIds);
        for (OrderItemFull orderItemDTO : orderFullDTO.getOrderItemFullList()) {
            Product product = products.stream().filter((item) -> item.getId().equals(orderItemDTO.getProductId())).findFirst().orElseThrow();
            if (product.getLeftOver() < orderItemDTO.getQuantity()) {
                throw new RuntimeException("Not enough left over");
            }
            product.setLeftOver(product.getLeftOver() - orderItemDTO.getQuantity());
            orderItemDTO.setPrice(product.getPrice());
            orderItemDTO.setProductName(product.getName());
        }
        productRepository.saveAll(products);
        paymentClient.savePayment(orderFullDTO);
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
