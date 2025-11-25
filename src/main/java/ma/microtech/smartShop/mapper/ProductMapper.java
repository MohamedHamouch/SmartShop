package ma.microtech.smartShop.mapper;

import ma.microtech.smartShop.dto.request.ProductRequest;
import ma.microtech.smartShop.dto.response.ProductResponse;
import ma.microtech.smartShop.entities.Product;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    Product toEntity(ProductRequest request);

    ProductResponse toResponse(Product product);

    List<ProductResponse> toResponseList(List<Product> products);
}
