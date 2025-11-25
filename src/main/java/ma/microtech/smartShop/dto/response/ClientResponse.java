package ma.microtech.smartShop.dto.response;

import lombok.*;
import ma.microtech.smartShop.entities.enums.CustomerTier;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientResponse {

    private Long id;
    private String email;
    private CustomerTier tier;
    private Integer totalOrders;
    private Double totalSpent;
}
