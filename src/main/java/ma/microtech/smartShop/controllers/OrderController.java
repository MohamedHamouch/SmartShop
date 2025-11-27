package ma.microtech.smartShop.controllers;

import java.util.List;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import ma.microtech.smartShop.dto.request.OrderRequest;
import ma.microtech.smartShop.dto.response.OrderResponse;
import ma.microtech.smartShop.services.OrderService;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getAllOrders() {
        List<OrderResponse> orders = orderService.findAll();
        return ResponseEntity.ok(
                ApiResponse.<List<OrderResponse>>builder()
                        .status(HttpStatus.OK.value())
                        .message("Orders retrieved successfully")
                        .data(orders)
                        .build()
        );
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getMyOrders(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        List<OrderResponse> orders = orderService.findByUserId(userId);
        return ResponseEntity.ok(
                ApiResponse.<List<OrderResponse>>builder()
                        .status(HttpStatus.OK.value())
                        .message("Orders retrieved successfully")
                        .data(orders)
                        .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderResponse>> getOrderById(@PathVariable Long id) {
        OrderResponse order = orderService.findById(id);
        return ResponseEntity.ok(
                ApiResponse.<OrderResponse>builder()
                        .status(HttpStatus.OK.value())
                        .message("Order retrieved successfully")
                        .data(order)
                        .build()
        );
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getOrdersByClient(@PathVariable Long clientId) {
        List<OrderResponse> orders = orderService.findByClientId(clientId);
        return ResponseEntity.ok(
                ApiResponse.<List<OrderResponse>>builder()
                        .status(HttpStatus.OK.value())
                        .message("Client orders retrieved successfully")
                        .data(orders)
                        .build()
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponse<OrderResponse>> createOrder(@Valid @RequestBody OrderRequest request) {
        OrderResponse order = orderService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.<OrderResponse>builder()
                        .status(HttpStatus.CREATED.value())
                        .message("Order created successfully")
                        .data(order)
                        .build()
        );
    }

    @PutMapping("/{id}/confirm")
    public ResponseEntity<ApiResponse<OrderResponse>> confirmOrder(@PathVariable Long id) {
        OrderResponse order = orderService.confirmOrder(id);
        return ResponseEntity.ok(
                ApiResponse.<OrderResponse>builder()
                        .status(HttpStatus.OK.value())
                        .message("Order confirmed successfully")
                        .data(order)
                        .build()
        );
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse<OrderResponse>> cancelOrder(@PathVariable Long id) {
        OrderResponse order = orderService.cancelOrder(id);
        return ResponseEntity.ok(
                ApiResponse.<OrderResponse>builder()
                        .status(HttpStatus.OK.value())
                        .message("Order canceled successfully")
                        .data(order)
                        .build()
        );
    }
}
