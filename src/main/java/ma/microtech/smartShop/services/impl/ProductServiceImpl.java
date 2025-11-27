package ma.microtech.smartShop.services.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ma.microtech.smartShop.dto.request.ProductRequest;
import ma.microtech.smartShop.dto.response.ProductResponse;
import ma.microtech.smartShop.entities.Product;
import ma.microtech.smartShop.exceptions.ResourceNotFoundException;
import ma.microtech.smartShop.mapper.ProductMapper;
import ma.microtech.smartShop.repositories.ProductRepository;
import ma.microtech.smartShop.services.ProductService;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    public Page<ProductResponse> findAll(Pageable pageable) {
        return productRepository.findByDeletedFalse(pageable)
                .map(productMapper::toResponse);
    }

    @Override
    public ProductResponse findById(Long id) {
        Product product = productRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        return productMapper.toResponse(product);
    }

    @Override
    public ProductResponse create(ProductRequest request) {
        Product product = productMapper.toEntity(request);
        Product savedProduct = productRepository.save(product);
        return productMapper.toResponse(savedProduct);
    }

    @Override
    public ProductResponse update(Long id, ProductRequest request) {
        Product product = productRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        product.setName(request.getName());
        product.setUnitPrice(request.getUnitPrice());
        product.setStock(request.getStock());

        Product updatedProduct = productRepository.save(product);
        return productMapper.toResponse(updatedProduct);
    }

    @Override
    public void delete(Long id) {
        Product product = productRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        product.setDeleted(true);
        productRepository.save(product);
    }
}
