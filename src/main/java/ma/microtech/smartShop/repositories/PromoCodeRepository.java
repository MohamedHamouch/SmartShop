package ma.microtech.smartShop.repositories;

import ma.microtech.smartShop.entities.PromoCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;


@Repository
public interface PromoCodeRepository extends JpaRepository<PromoCode, Long> {

    Optional<PromoCode> findByCode(String code);

    boolean existsByCode(String code);

    List<PromoCode> findByActiveTrue();

}
