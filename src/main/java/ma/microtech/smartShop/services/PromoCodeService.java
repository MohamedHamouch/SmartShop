package ma.microtech.smartShop.services;

import java.util.List;
import java.util.Optional;

import ma.microtech.smartShop.entities.PromoCode;

public interface PromoCodeService {

    List<PromoCode> findAll();

    List<PromoCode> findActive();

    Optional<PromoCode> findById(Long id);

    PromoCode create(String code, Double discountPercentage);

    Optional<PromoCode> deactivate(Long id);

    void delete(Long id);
}
