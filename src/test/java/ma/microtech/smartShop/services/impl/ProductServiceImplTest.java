package ma.microtech.smartShop.services.impl;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.ArgumentMatchers.any;

import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import ma.microtech.smartShop.dto.request.ProductRequest;
import ma.microtech.smartShop.dto.response.ProductResponse;
import ma.microtech.smartShop.entities.Product;
import ma.microtech.smartShop.exceptions.ResourceNotFoundException;
import ma.microtech.smartShop.mapper.ProductMapper;
import ma.microtech.smartShop.repositories.ProductRepository;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product product;
    private ProductRequest productRequest;
    private ProductResponse productResponse;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        product = Product.builder()
                .id(1L)
                .name("Coffee")
                .unitPrice(250.0)
                .stock(100)
                .deleted(false)
                .build();

        productRequest = ProductRequest.builder()
                .name("Coffee")
                .unitPrice(250.0)
                .stock(100)
                .build();

        productResponse = ProductResponse.builder()
                .id(1L)
                .name("Coffee")
                .unitPrice(250.0)
                .stock(100)
                .build();

        pageable = PageRequest.of(0, 10);
    }

    @Test
    void findAll_ShouldReturnPageOfProducts() {
        Page<Product> productPage = new PageImpl<>(List.of(product));
        when(productRepository.findByDeletedFalse(pageable)).thenReturn(productPage);
        when(productMapper.toResponse(product)).thenReturn(productResponse);

        Page<ProductResponse> result = productService.findAll(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(productResponse.getId(), result.getContent().get(0).getId());
        verify(productRepository).findByDeletedFalse(pageable);
        verify(productMapper).toResponse(product);
    }

    @Test
    void findById_WhenProductExists_ShouldReturnProduct() {
        when(productRepository.findByIdAndDeletedFalse(1L)).thenReturn(Optional.of(product));
        when(productMapper.toResponse(product)).thenReturn(productResponse);

        ProductResponse result = productService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Coffee", result.getName());
        verify(productRepository).findByIdAndDeletedFalse(1L);
        verify(productMapper).toResponse(product);
    }

    @Test
    void findById_WhenProductNotFound_ShouldThrowException() {
        when(productRepository.findByIdAndDeletedFalse(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> productService.findById(1L)
        );
        assertEquals("Product not found with id: 1", exception.getMessage());
        verify(productRepository).findByIdAndDeletedFalse(1L);
        verify(productMapper, never()).toResponse(any());
    }

    @Test
    void create_ShouldSaveAndReturnProduct() {
        when(productMapper.toEntity(productRequest)).thenReturn(product);
        when(productRepository.save(product)).thenReturn(product);
        when(productMapper.toResponse(product)).thenReturn(productResponse);

        ProductResponse result = productService.create(productRequest);

        assertNotNull(result);
        assertEquals("Coffee", result.getName());
        assertEquals(250.0, result.getUnitPrice());
        assertEquals(100, result.getStock());
        verify(productMapper).toEntity(productRequest);
        verify(productRepository).save(product);
        verify(productMapper).toResponse(product);
    }

    @Test
    void update_WhenProductExists_ShouldUpdateAndReturn() {
        ProductRequest updateRequest = ProductRequest.builder()
                .name("Updated Coffee")
                .unitPrice(300.0)
                .stock(150)
                .build();

        Product updatedProduct = Product.builder()
                .id(1L)
                .name("Updated Coffee")
                .unitPrice(300.0)
                .stock(150)
                .deleted(false)
                .build();

        ProductResponse updatedResponse = ProductResponse.builder()
                .id(1L)
                .name("Updated Coffee")
                .unitPrice(300.0)
                .stock(150)
                .build();

        when(productRepository.findByIdAndDeletedFalse(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(updatedProduct);
        when(productMapper.toResponse(updatedProduct)).thenReturn(updatedResponse);

        ProductResponse result = productService.update(1L, updateRequest);

        assertNotNull(result);
        assertEquals("Updated Coffee", result.getName());
        assertEquals(300.0, result.getUnitPrice());
        assertEquals(150, result.getStock());
        verify(productRepository).findByIdAndDeletedFalse(1L);
        verify(productRepository).save(any(Product.class));
        verify(productMapper).toResponse(updatedProduct);
    }

    @Test
    void update_WhenProductNotFound_ShouldThrowException() {
        when(productRepository.findByIdAndDeletedFalse(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> productService.update(1L, productRequest)
        );
        assertEquals("Product not found with id: 1", exception.getMessage());
        verify(productRepository).findByIdAndDeletedFalse(1L);
        verify(productRepository, never()).save(any());
    }

    @Test
    void delete_WhenProductExists_ShouldMarkAsDeleted() {
        when(productRepository.findByIdAndDeletedFalse(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        productService.delete(1L);

        assertTrue(product.getDeleted());
        verify(productRepository).findByIdAndDeletedFalse(1L);
        verify(productRepository).save(product);
    }

    @Test
    void delete_WhenProductNotFound_ShouldThrowException() {
        when(productRepository.findByIdAndDeletedFalse(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> productService.delete(1L)
        );
        assertEquals("Product not found with id: 1", exception.getMessage());
        verify(productRepository).findByIdAndDeletedFalse(1L);
        verify(productRepository, never()).save(any());
    }
}
