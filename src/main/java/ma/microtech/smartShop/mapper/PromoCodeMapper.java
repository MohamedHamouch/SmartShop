package ma.microtech.smartShop.mapper;

import ma.microtech.smartShop.dto.request.PromoCodeRequest;
import ma.microtech.smartShop.dto.response.PromoCodeResponse;
import ma.microtech.smartShop.entities.PromoCode;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PromoCodeMapper {

    PromoCode toEntity(PromoCodeRequest request);

    PromoCodeResponse toResponse(PromoCode promoCode);

    List<PromoCodeResponse> toResponseList(List<PromoCode> promoCodes);
}
