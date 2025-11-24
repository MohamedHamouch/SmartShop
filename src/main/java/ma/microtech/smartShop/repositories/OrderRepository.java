package ma.microtech.smartShop.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ma.microtech.smartShop.entities.Client;
import ma.microtech.smartShop.entities.Order;
import ma.microtech.smartShop.entities.enums.OrderStatus;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByClient(Client client);

    List<Order> findByClientIdAndStatus(Long clientId, OrderStatus status);
}
