package uz.pdp.inventoryservice.service;

import org.springframework.transaction.annotation.Transactional;
import uz.pdp.clients.dtos.OrderItemDTO;
import uz.pdp.inventoryservice.entity.ProductOutcome;
import uz.pdp.inventoryservice.repo.ProductIncomeRepository;
import uz.pdp.inventoryservice.repo.ProductOutcomeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final ProductOutcomeRepository productOutcomeRepository;

    @Transactional
    public void orderUpdates(List<OrderItemDTO> orderItems, Long orderId) {
        List<ProductOutcome> list = orderItems.stream().map(item -> new ProductOutcome(item.getProductId(), item.getQuantity(), LocalDateTime.now(), orderId)).toList();
        productOutcomeRepository.saveAll(list);
    }
}
