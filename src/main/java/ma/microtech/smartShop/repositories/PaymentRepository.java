package ma.microtech.smartShop.repositories;

import ma.microtech.smartShop.entities.Payment;
import ma.microtech.smartShop.entities.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findByOrderId(Long orderId);

    List<Payment> findByStatus(PaymentStatus status);
}
