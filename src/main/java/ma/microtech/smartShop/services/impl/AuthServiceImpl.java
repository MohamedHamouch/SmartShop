package ma.microtech.smartShop.services.impl;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import ma.microtech.smartShop.dto.request.LoginRequest;
import ma.microtech.smartShop.dto.response.UserResponse;
import ma.microtech.smartShop.entities.User;
import ma.microtech.smartShop.exceptions.BadRequestException;
import ma.microtech.smartShop.mapper.UserMapper;
import ma.microtech.smartShop.repositories.UserRepository;
import ma.microtech.smartShop.services.AuthService;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponse login(LoginRequest request, HttpSession session) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new BadRequestException("Invalid username or password"));

        if (!BCrypt.checkpw(request.getPassword(), user.getPassword())) {
            throw new BadRequestException("Invalid username or password");
        }

        session.setAttribute("userId", user.getId());
        session.setAttribute("username", user.getUsername());
        session.setAttribute("role", user.getRole().name());

        return userMapper.toResponse(user);
    }

    @Override
    public void logout(HttpSession session) {
        session.invalidate();
    }

    @Override
    public UserResponse getCurrentUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BadRequestException("User not found"));
        return userMapper.toResponse(user);
    }
}
