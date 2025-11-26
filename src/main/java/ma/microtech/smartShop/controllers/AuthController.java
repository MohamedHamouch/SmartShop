package ma.microtech.smartShop.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.microtech.smartShop.dto.ApiResponse;
import ma.microtech.smartShop.dto.request.LoginRequest;
import ma.microtech.smartShop.dto.response.UserResponse;
import ma.microtech.smartShop.services.AuthService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<UserResponse>> login(@Valid @RequestBody LoginRequest request, HttpSession session) {
        UserResponse userResponse = authService.login(request, session);
        return ResponseEntity.ok(
                ApiResponse.<UserResponse>builder()
                        .status(HttpStatus.OK.value())
                        .message("Login successful")
                        .data(userResponse)
                        .build()
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(HttpSession session) {
        authService.logout(session);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .status(HttpStatus.OK.value())
                        .message("Logout successful")
                        .build()
        );
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getCurrentUser(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        UserResponse userResponse = authService.getCurrentUser(userId);
        return ResponseEntity.ok(
                ApiResponse.<UserResponse>builder()
                        .status(HttpStatus.OK.value())
                        .message("User retrieved successfully")
                        .data(userResponse)
                        .build()
        );
    }
}
