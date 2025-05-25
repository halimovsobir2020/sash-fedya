package uz.pdp.inventoryservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.pdp.clients.dtos.OrderFullDTO;
import uz.pdp.clients.dtos.OrderItemDTO;
import uz.pdp.inventoryservice.entity.ProductOutcome;
import uz.pdp.inventoryservice.repo.ProductOutcomeRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final ProductOutcomeRepository productOutcomeRepository;

    @Transactional
    public void orderUpdates(OrderFullDTO orderFullDTO) {
        List<ProductOutcome> list = orderFullDTO.getOrderItemFullList().stream()
                .map(item ->
                        new ProductOutcome(item.getProductId(), item.getQuantity(), LocalDateTime.now(), orderFullDTO.getOrderId())).toList();
        productOutcomeRepository.saveAll(list);
    }

    public void rollbackOutcome(Long orderId) {
        productOutcomeRepository.deleteAllByOrderId(orderId);
    }
}
