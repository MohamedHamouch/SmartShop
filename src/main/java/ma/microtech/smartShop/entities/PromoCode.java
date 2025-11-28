package ma.microtech.smartShop.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "promo_codes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PromoCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    @Builder.Default
    private Boolean active = true;

    @Column(nullable = false)
    private Double discountPercentage;
}
