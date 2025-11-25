package ma.microtech.smartShop.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemResponse {

    private Long id;
    private ProductResponse product;
    private Integer quantity;
    private Double unitPrice;
    private Double lineTotal;
}
