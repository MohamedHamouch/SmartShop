package ma.microtech.smartShop.repositories;

import ma.microtech.smartShop.entities.Client;
import ma.microtech.smartShop.entities.enums.CustomerTier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    Optional<Client> findByEmail(String email);

    boolean existsByEmail(String email);

    Optional<Client> findByUserId(Long userId);
}
