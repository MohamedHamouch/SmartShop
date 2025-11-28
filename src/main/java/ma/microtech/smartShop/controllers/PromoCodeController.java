package ma.microtech.smartShop.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import ma.microtech.smartShop.dto.ApiResponse;
import ma.microtech.smartShop.dto.request.PromoCodeRequest;
import ma.microtech.smartShop.dto.response.PromoCodeResponse;
import ma.microtech.smartShop.entities.PromoCode;
import ma.microtech.smartShop.mapper.PromoCodeMapper;
import ma.microtech.smartShop.services.PromoCodeService;

@RestController
@RequestMapping("/api/promocodes")
public class PromoCodeController {

    private final PromoCodeService promoCodeService;
    private final PromoCodeMapper promoCodeMapper;

    public PromoCodeController(PromoCodeService promoCodeService, PromoCodeMapper promoCodeMapper) {
        this.promoCodeService = promoCodeService;
        this.promoCodeMapper = promoCodeMapper;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PromoCodeResponse>>> getAllPromoCodes() {
        List<PromoCode> promoCodes = promoCodeService.findAll();
        List<PromoCodeResponse> responses = promoCodeMapper.toResponseList(promoCodes);

        ApiResponse<List<PromoCodeResponse>> response = ApiResponse.<List<PromoCodeResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Promo codes retrieved successfully")
                .data(responses)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<PromoCodeResponse>>> getActivePromoCodes() {
        List<PromoCode> promoCodes = promoCodeService.findActive();
        List<PromoCodeResponse> responses = promoCodeMapper.toResponseList(promoCodes);

        ApiResponse<List<PromoCodeResponse>> response = ApiResponse.<List<PromoCodeResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Active promo codes retrieved successfully")
                .data(responses)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PromoCodeResponse>> getPromoCodeById(@PathVariable Long id) {
        return promoCodeService.findById(id)
                .map(pc -> {
                    PromoCodeResponse promoResponse = promoCodeMapper.toResponse(pc);
                    ApiResponse<PromoCodeResponse> response = ApiResponse.<PromoCodeResponse>builder()
                            .status(HttpStatus.OK.value())
                            .message("Promo code found")
                            .data(promoResponse)
                            .build();
                    return ResponseEntity.ok(response);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ApiResponse<PromoCodeResponse>> createPromoCode(@Valid @RequestBody PromoCodeRequest request) {
        PromoCode created = promoCodeService.create(request.getCode(), request.getDiscountPercentage());
        PromoCodeResponse promoResponse = promoCodeMapper.toResponse(created);

        ApiResponse<PromoCodeResponse> response = ApiResponse.<PromoCodeResponse>builder()
                .status(HttpStatus.CREATED.value())
                .message("Promo code created successfully")
                .data(promoResponse)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}/deactivate")
    public ResponseEntity<ApiResponse<PromoCodeResponse>> deactivatePromoCode(@PathVariable Long id) {
        return promoCodeService.deactivate(id)
                .map(pc -> {
                    PromoCodeResponse promoResponse = promoCodeMapper.toResponse(pc);
                    ApiResponse<PromoCodeResponse> response = ApiResponse.<PromoCodeResponse>builder()
                            .status(HttpStatus.OK.value())
                            .message("Promo code deactivated successfully")
                            .data(promoResponse)
                            .build();
                    return ResponseEntity.ok(response);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletePromoCode(@PathVariable Long id) {
        promoCodeService.delete(id);

        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .status(HttpStatus.NO_CONTENT.value())
                .message("Promo code deleted successfully")
                .build();

        return ResponseEntity.ok(response);
    }
}
