package ma.microtech.smartShop.services.impl;

import java.time.LocalDateTime;
import java.util.List;

import ma.microtech.smartShop.entities.enums.OrderStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ma.microtech.smartShop.dto.request.PaymentRequest;
import ma.microtech.smartShop.dto.response.PaymentResponse;
import ma.microtech.smartShop.entities.Order;
import ma.microtech.smartShop.entities.Payment;
import ma.microtech.smartShop.entities.enums.PaymentStatus;
import ma.microtech.smartShop.entities.enums.PaymentType;
import ma.microtech.smartShop.exceptions.BadRequestException;
import ma.microtech.smartShop.exceptions.BusinessRuleException;
import ma.microtech.smartShop.exceptions.ResourceNotFoundException;
import ma.microtech.smartShop.mapper.PaymentMapper;
import ma.microtech.smartShop.repositories.OrderRepository;
import ma.microtech.smartShop.repositories.PaymentRepository;
import ma.microtech.smartShop.services.PaymentService;

@Service
@Transactional
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final PaymentMapper paymentMapper;

    @Override
    public PaymentResponse recordPayment(PaymentRequest request) {
        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + request.getOrderId()));

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new BusinessRuleException("Payments can only be recorded for orders with PENDING status");
        }

        if (request.getAmount() <= 0) {
            throw new BadRequestException("Payment amount must be greater than 0");
        }

        if (request.getAmount() > order.getRemainingAmount()) {
            throw new BadRequestException("Payment amount exceeds remaining amount: " + order.getRemainingAmount() + " DH");
        }

        if (request.getType() == PaymentType.CASH && request.getAmount() > 20000) {
            throw new BusinessRuleException("CASH payments cannot exceed 20,000 DH (Moroccan legal limit)");
        }

        if (request.getType() == PaymentType.CHECK || request.getType() == PaymentType.TRANSFER) {
            if (request.getBank() == null || request.getBank().isBlank()) {
                throw new BadRequestException(request.getType() + " payment requires a bank name");
            }
        }

        Integer paymentNumber = paymentRepository.findByOrderId(request.getOrderId()).size() + 1;

        PaymentStatus status = determinePaymentStatus(request.getType());

        Payment payment = Payment.builder()
                .order(order)
                .paymentNumber(paymentNumber)
                .reference(request.getReference())
                .amount(request.getAmount())
                .type(request.getType())
                .status(status)
                .paymentDate(LocalDateTime.now())
                .bank(request.getBank())
                .build();

        Payment savedPayment = paymentRepository.save(payment);

        order.setRemainingAmount(order.getRemainingAmount() - request.getAmount());
        orderRepository.save(order);

        return paymentMapper.toResponse(savedPayment);
    }

    @Override
    public PaymentResponse encashPayment(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + paymentId));

        if (payment.getStatus() == PaymentStatus.ENCASHED) {
            throw new BusinessRuleException("Payment is already encashed");
        }

        if (payment.getStatus() == PaymentStatus.REJECTED) {
            throw new BusinessRuleException("Cannot encash a rejected payment");
        }

        payment.setStatus(PaymentStatus.ENCASHED);
        payment.setEncashmentDate(LocalDateTime.now());

        Payment updatedPayment = paymentRepository.save(payment);
        return paymentMapper.toResponse(updatedPayment);
    }

    @Override
    public PaymentResponse rejectPayment(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + paymentId));

        if (payment.getStatus() == PaymentStatus.ENCASHED) {
            throw new BusinessRuleException("Cannot reject an encashed payment");
        }

        if (payment.getStatus() == PaymentStatus.REJECTED) {
            throw new BusinessRuleException("Payment is already rejected");
        }

        Order order = payment.getOrder();

        order.setRemainingAmount(order.getRemainingAmount() + payment.getAmount());
        orderRepository.save(order);

        payment.setStatus(PaymentStatus.REJECTED);
        Payment updatedPayment = paymentRepository.save(payment);

        return paymentMapper.toResponse(updatedPayment);
    }

    @Override
    public List<PaymentResponse> findByOrderId(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));

        return paymentMapper.toResponseList(paymentRepository.findByOrderId(orderId));
    }

    @Override
    public PaymentResponse findById(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + paymentId));

        return paymentMapper.toResponse(payment);
    }

    private PaymentStatus determinePaymentStatus(PaymentType type) {
        if (type == PaymentType.CASH) {
            return PaymentStatus.ENCASHED;
        }
        return PaymentStatus.PENDING;
    }
}
