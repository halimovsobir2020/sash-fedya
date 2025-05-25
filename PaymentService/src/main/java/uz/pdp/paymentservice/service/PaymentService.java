package uz.pdp.paymentservice.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.pdp.clients.dtos.OrderFullDTO;
import uz.pdp.clients.dtos.OrderItemFull;
import uz.pdp.clients.dtos.PaymentCreateDTO;
import uz.pdp.clients.inventory.InventoryClient;
import uz.pdp.paymentservice.entity.Payment;
import uz.pdp.paymentservice.repo.PaymentRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final InventoryClient inventoryClient;

    @Transactional
    public Payment savePayment(OrderFullDTO orderFullDTO) {
        Payment payment = new Payment(
                calculateOrderTotalPrice(orderFullDTO.getOrderItemFullList()),
                LocalDateTime.now(),
                orderFullDTO.getOrderId());
        paymentRepository.save(payment);
        inventoryClient.orderOutcome(orderFullDTO);
//        throw new RuntimeException("payment uxladi");
        return payment;
    }

    private Integer calculateOrderTotalPrice(List<OrderItemFull> orderItemDTOS) {
        return orderItemDTOS.stream().mapToInt(item -> item.getQuantity() * item.getPrice()).sum();
    }

    public void rollbackPayment(Long orderId) {
        paymentRepository.deleteByOrderId(orderId);
    }
}
