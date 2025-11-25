package ma.microtech.smartShop.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponse {

    private Long id;
    private String name;
    private Double unitPrice;
    private Integer stock;
    private Boolean deleted;
}
