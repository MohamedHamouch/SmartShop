package ma.microtech.smartShop.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.microtech.smartShop.dto.ApiResponse;
import ma.microtech.smartShop.dto.request.PaymentRequest;
import ma.microtech.smartShop.dto.response.PaymentResponse;
import ma.microtech.smartShop.services.PaymentService;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<ApiResponse<PaymentResponse>> recordPayment(@Valid @RequestBody PaymentRequest request) {
        PaymentResponse payment = paymentService.recordPayment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.<PaymentResponse>builder()
                        .status(HttpStatus.CREATED.value())
                        .message("Payment recorded successfully")
                        .data(payment)
                        .build()
        );
    }

    @PutMapping("/{id}/encash")
    public ResponseEntity<ApiResponse<PaymentResponse>> encashPayment(@PathVariable Long id) {
        PaymentResponse payment = paymentService.encashPayment(id);
        return ResponseEntity.ok(
                ApiResponse.<PaymentResponse>builder()
                        .status(HttpStatus.OK.value())
                        .message("Payment encashed successfully")
                        .data(payment)
                        .build()
        );
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<ApiResponse<PaymentResponse>> rejectPayment(@PathVariable Long id) {
        PaymentResponse payment = paymentService.rejectPayment(id);
        return ResponseEntity.ok(
                ApiResponse.<PaymentResponse>builder()
                        .status(HttpStatus.OK.value())
                        .message("Payment rejected successfully")
                        .data(payment)
                        .build()
        );
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<ApiResponse<List<PaymentResponse>>> getPaymentsByOrder(@PathVariable Long orderId) {
        List<PaymentResponse> payments = paymentService.findByOrderId(orderId);
        return ResponseEntity.ok(
                ApiResponse.<List<PaymentResponse>>builder()
                        .status(HttpStatus.OK.value())
                        .message("Payments retrieved successfully")
                        .data(payments)
                        .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PaymentResponse>> getPaymentById(@PathVariable Long id) {
        PaymentResponse payment = paymentService.findById(id);
        return ResponseEntity.ok(
                ApiResponse.<PaymentResponse>builder()
                        .status(HttpStatus.OK.value())
                        .message("Payment retrieved successfully")
                        .data(payment)
                        .build()
        );
    }
}
