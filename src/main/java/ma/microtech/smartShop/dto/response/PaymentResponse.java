package ma.microtech.smartShop.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ma.microtech.smartShop.entities.enums.PaymentStatus;
import ma.microtech.smartShop.entities.enums.PaymentType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentResponse {

    private Long id;
    private Integer paymentNumber;
    private String reference;
    private Double amount;
    private PaymentType type;
    private PaymentStatus status;
    private LocalDateTime paymentDate;
    private LocalDateTime encashmentDate;
    private String bank;
}
