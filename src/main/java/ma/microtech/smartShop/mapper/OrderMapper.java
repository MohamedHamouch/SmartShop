package ma.microtech.smartShop.mapper;

import ma.microtech.smartShop.dto.request.OrderRequest;
import ma.microtech.smartShop.dto.response.OrderResponse;
import ma.microtech.smartShop.entities.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ClientMapper.class, PromoCodeMapper.class, OrderItemMapper.class, PaymentMapper.class})
public interface OrderMapper {

    @Mapping(target = "client", ignore = true)
    @Mapping(target = "promoCode", ignore = true)
    @Mapping(target = "items", ignore = true)
    Order toEntity(OrderRequest request);

    OrderResponse toResponse(Order order);

    List<OrderResponse> toResponseList(List<Order> orders);
}
