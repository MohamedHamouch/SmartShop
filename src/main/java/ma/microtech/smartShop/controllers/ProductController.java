package ma.microtech.smartShop.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.microtech.smartShop.dto.ApiResponse;
import ma.microtech.smartShop.dto.request.ProductRequest;
import ma.microtech.smartShop.dto.response.ProductResponse;
import ma.microtech.smartShop.services.ProductService;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<ProductResponse>>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ProductResponse> products = productService.findAll(pageable);
        return ResponseEntity.ok(
                ApiResponse.<Page<ProductResponse>>builder()
                        .status(HttpStatus.OK.value())
                        .message("Products retrieved successfully")
                        .data(products)
                        .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> getProductById(@PathVariable Long id) {
        ProductResponse product = productService.findById(id);
        return ResponseEntity.ok(
                ApiResponse.<ProductResponse>builder()
                        .status(HttpStatus.OK.value())
                        .message("Product retrieved successfully")
                        .data(product)
                        .build()
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ProductResponse>> createProduct(@Valid @RequestBody ProductRequest request) {
        ProductResponse product = productService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.<ProductResponse>builder()
                        .status(HttpStatus.CREATED.value())
                        .message("Product created successfully")
                        .data(product)
                        .build()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequest request) {
        ProductResponse product = productService.update(id, request);
        return ResponseEntity.ok(
                ApiResponse.<ProductResponse>builder()
                        .status(HttpStatus.OK.value())
                        .message("Product updated successfully")
                        .data(product)
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .status(HttpStatus.OK.value())
                        .message("Product deleted successfully")
                        .build()
        );
    }
}
