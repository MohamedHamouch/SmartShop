package ma.microtech.smartShop.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import ma.microtech.smartShop.dto.request.ProductRequest;
import ma.microtech.smartShop.dto.response.ProductResponse;

public interface ProductService {

    Page<ProductResponse> findAll(Pageable pageable);

    ProductResponse findById(Long id);

    ProductResponse create(ProductRequest request);

    ProductResponse update(Long id, ProductRequest request);

    void delete(Long id);
}
