package ma.microtech.smartShop.services;

import jakarta.servlet.http.HttpSession;
import ma.microtech.smartShop.dto.request.LoginRequest;
import ma.microtech.smartShop.dto.response.UserResponse;

public interface AuthService {

    UserResponse login(LoginRequest request, HttpSession session);

    void logout(HttpSession session);

    UserResponse getCurrentUser(Long userId);
}
