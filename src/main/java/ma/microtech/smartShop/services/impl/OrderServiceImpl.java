package ma.microtech.smartShop.services.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ma.microtech.smartShop.dto.request.OrderRequest;
import ma.microtech.smartShop.dto.response.OrderResponse;
import ma.microtech.smartShop.entities.Client;
import ma.microtech.smartShop.entities.Order;
import ma.microtech.smartShop.entities.OrderItem;
import ma.microtech.smartShop.entities.Product;
import ma.microtech.smartShop.entities.PromoCode;
import ma.microtech.smartShop.entities.enums.OrderStatus;
import ma.microtech.smartShop.exceptions.BadRequestException;
import ma.microtech.smartShop.exceptions.BusinessRuleException;
import ma.microtech.smartShop.exceptions.ResourceNotFoundException;
import ma.microtech.smartShop.mapper.OrderMapper;
import ma.microtech.smartShop.repositories.ClientRepository;
import ma.microtech.smartShop.repositories.OrderRepository;
import ma.microtech.smartShop.repositories.ProductRepository;
import ma.microtech.smartShop.repositories.PromoCodeRepository;
import ma.microtech.smartShop.services.OrderService;
import ma.microtech.smartShop.services.TierService;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    @Value("${app.tva.rate:0.20}")
    private Double tvaRate;

    private final OrderRepository orderRepository;
    private final ClientRepository clientRepository;
    private final ProductRepository productRepository;
    private final PromoCodeRepository promoCodeRepository;
    private final OrderMapper orderMapper;
    private final TierService tierService;

    @Override
    public List<OrderResponse> findAll() {
        return orderMapper.toResponseList(orderRepository.findAll());
    }

    @Override
    public OrderResponse findById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
        return orderMapper.toResponse(order);
    }

    @Override
    public List<OrderResponse> findByClientId(Long clientId) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + clientId));
        return orderMapper.toResponseList(orderRepository.findByClient(client));
    }

    @Override
    public List<OrderResponse> findByUserId(Long userId) {
        Client client = clientRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found for user id: " + userId));
        return orderMapper.toResponseList(orderRepository.findByClient(client));
    }

    @Override
    public OrderResponse create(OrderRequest request) {
        Client client = clientRepository.findById(request.getClientId())
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + request.getClientId()));

        List<OrderItem> items = new ArrayList<>();
        Double subtotalHT = 0.0;

        for (var itemRequest : request.getItems()) {
            Product product = productRepository.findByIdAndDeletedFalse(itemRequest.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + itemRequest.getProductId()));

            if (product.getStock() < itemRequest.getQuantity()) {
                throw new BadRequestException("Insufficient stock for product: " + product.getName());
            }

            OrderItem item = OrderItem.builder()
                    .product(product)
                    .quantity(itemRequest.getQuantity())
                    .unitPrice(product.getUnitPrice())
                    .lineTotal(product.getUnitPrice() * itemRequest.getQuantity())
                    .build();

            items.add(item);
            subtotalHT += item.getLineTotal();
        }


        Double tierDiscount = tierService.getTierDiscount(client.getTier(), subtotalHT);

        Double promoDiscount = 0.0;
        PromoCode promoCode = null;

        if (request.getPromoCode() != null && !request.getPromoCode().isBlank()) {
            promoCode = promoCodeRepository.findByCode(request.getPromoCode())
                    .orElseThrow(() -> new BadRequestException("Invalid promo code"));

            if (!promoCode.getActive()) {
                throw new BadRequestException("Promo code is not active");
            }

            promoDiscount = promoCode.getDiscountPercentage() / 100.0;
        }

        Double totalDiscountPercent = tierDiscount + promoDiscount;
        Double discountAmount = subtotalHT * totalDiscountPercent;
        Double amountAfterDiscount = subtotalHT - discountAmount;

        Double tva = amountAfterDiscount * tvaRate;

        Double totalTTC = amountAfterDiscount + tva;

        Order order = Order.builder()
                .client(client)
                .createdAt(LocalDateTime.now())
                .subtotal(subtotalHT)
                .discount(discountAmount)
                .tva(tva)
                .tvaRate(tvaRate)
                .total(totalTTC)
                .remainingAmount(totalTTC)
                .status(OrderStatus.PENDING)
                .promoCode(promoCode)
                .items(items)
                .build();

        items.forEach(item -> item.setOrder(order));
        Order savedOrder = orderRepository.save(order);

        return orderMapper.toResponse(savedOrder);
    }

    @Override
    public OrderResponse confirmOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));

        if (order.getStatus() == OrderStatus.CONFIRMED) {
            throw new BusinessRuleException("Order is already confirmed");
        }

        if (order.getStatus() == OrderStatus.CANCELED) {
            throw new BusinessRuleException("Cannot confirm a canceled order");
        }

        if (order.getStatus() == OrderStatus.REJECTED) {
            throw new BusinessRuleException("Cannot confirm a rejected order");
        }

        if (order.getRemainingAmount() > 0) {
            throw new BusinessRuleException(
                    "Cannot confirm order. Remaining amount: " + order.getRemainingAmount() + " DH"
            );
        }

        for (OrderItem item : order.getItems()) {
            Product product = item.getProduct();
            if (product.getStock() < item.getQuantity()) {
                order.setStatus(OrderStatus.REJECTED);
                orderRepository.save(order);
                throw new BusinessRuleException(
                        "Order rejected: insufficient stock for product " + product.getName()
                        + ". Available: " + product.getStock() + ", Required: " + item.getQuantity()
                );
            }
        }

        for (OrderItem item : order.getItems()) {
            Product product = item.getProduct();
            product.setStock(product.getStock() - item.getQuantity());
            productRepository.save(product);
        }

        Client client = order.getClient();
        client.setTotalSpent(client.getTotalSpent() + order.getTotal());
        client.setTotalOrders(client.getTotalOrders() + 1);

        tierService.checkAndUpgradeTier(client);
        clientRepository.save(client);

        order.setStatus(OrderStatus.CONFIRMED);
        Order updatedOrder = orderRepository.save(order);

        return orderMapper.toResponse(updatedOrder);
    }

    @Override
    public OrderResponse cancelOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));

        if (order.getStatus() == OrderStatus.CONFIRMED) {
            throw new BusinessRuleException("Cannot cancel a confirmed order");
        }

        if (order.getStatus() == OrderStatus.CANCELED) {
            throw new BusinessRuleException("Order is already canceled");
        }

        order.setStatus(OrderStatus.CANCELED);
        Order updatedOrder = orderRepository.save(order);

        return orderMapper.toResponse(updatedOrder);
    }
}
