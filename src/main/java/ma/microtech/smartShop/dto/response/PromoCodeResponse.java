package ma.microtech.smartShop.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PromoCodeResponse {

    private Long id;
    private String code;
    private Boolean active;
    private Double discountPercentage;
}
