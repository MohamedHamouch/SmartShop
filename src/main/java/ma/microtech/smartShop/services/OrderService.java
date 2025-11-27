package ma.microtech.smartShop.services;

import java.util.List;

import ma.microtech.smartShop.dto.request.OrderRequest;
import ma.microtech.smartShop.dto.response.OrderResponse;

public interface OrderService {

    List<OrderResponse> findAll();

    OrderResponse findById(Long id);

    List<OrderResponse> findByClientId(Long clientId);

    List<OrderResponse> findByUserId(Long userId);

    OrderResponse create(OrderRequest request);

    OrderResponse confirmOrder(Long id);

    OrderResponse cancelOrder(Long id);
}
