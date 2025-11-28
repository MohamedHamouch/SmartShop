package ma.microtech.smartShop.services.impl;

import java.util.List;
import java.util.Optional;

import ma.microtech.smartShop.exceptions.BusinessRuleException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ma.microtech.smartShop.entities.PromoCode;
import ma.microtech.smartShop.repositories.PromoCodeRepository;
import ma.microtech.smartShop.services.PromoCodeService;

@Service
@Transactional
@RequiredArgsConstructor
public class PromoCodeServiceImpl implements PromoCodeService {

    private final PromoCodeRepository promoCodeRepository;

    @Override
    public List<PromoCode> findAll() {
        return promoCodeRepository.findAll();
    }

    @Override
    public List<PromoCode> findActive() {
        return promoCodeRepository.findByActiveTrue();
    }

    @Override
    public Optional<PromoCode> findById(Long id) {
        return promoCodeRepository.findById(id);
    }

    @Override
    public PromoCode create(String code, Double discountPercentage) {
        if (promoCodeRepository.existsByCode(code)) {
            throw new BusinessRuleException("Promo code already exists: " + code);
        }

        PromoCode promo = PromoCode.builder()
                .code(code)
                .discountPercentage(discountPercentage)
                .build();
        return promoCodeRepository.save(promo);
    }

    @Override
    public Optional<PromoCode> deactivate(Long id) {
        return promoCodeRepository.findById(id).map(existing -> {
            existing.setActive(false);
            return promoCodeRepository.save(existing);
        });
    }

    @Override
    public void delete(Long id) {
        promoCodeRepository.deleteById(id);
    }
}
