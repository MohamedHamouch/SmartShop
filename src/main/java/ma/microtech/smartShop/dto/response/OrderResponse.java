package ma.microtech.smartShop.dto.response;

import lombok.*;
import ma.microtech.smartShop.entities.enums.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponse {

    private Long id;
    private ClientResponse client;
    private LocalDateTime createdAt;
    private LocalDateTime confirmedAt;
    private Double subtotal;
    private Double discount;
    private Double tvaRate;
    private Double tva;
    private Double total;
    private OrderStatus status;
    private Double remainingAmount;
    private PromoCodeResponse promoCode;
    private List<OrderItemResponse> items;
    private List<PaymentResponse> payments;
}
