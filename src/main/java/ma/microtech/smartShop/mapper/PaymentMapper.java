package ma.microtech.smartShop.mapper;

import ma.microtech.smartShop.dto.request.PaymentRequest;
import ma.microtech.smartShop.dto.response.PaymentResponse;
import ma.microtech.smartShop.entities.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    @Mapping(target = "order", ignore = true)
    Payment toEntity(PaymentRequest request);

    PaymentResponse toResponse(Payment payment);

    List<PaymentResponse> toResponseList(List<Payment> payments);
}
