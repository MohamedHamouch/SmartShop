package ma.microtech.smartShop.services.impl;

import java.util.List;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ma.microtech.smartShop.dto.request.ClientRequest;
import ma.microtech.smartShop.dto.request.CreateClientRequest;
import ma.microtech.smartShop.dto.response.ClientResponse;
import ma.microtech.smartShop.dto.response.UserResponse;
import ma.microtech.smartShop.entities.Client;
import ma.microtech.smartShop.entities.User;
import ma.microtech.smartShop.entities.enums.UserRole;
import ma.microtech.smartShop.exceptions.BadRequestException;
import ma.microtech.smartShop.exceptions.ResourceNotFoundException;
import ma.microtech.smartShop.mapper.ClientMapper;
import ma.microtech.smartShop.mapper.UserMapper;
import ma.microtech.smartShop.repositories.ClientRepository;
import ma.microtech.smartShop.repositories.UserRepository;
import ma.microtech.smartShop.services.ClientService;

@Service
@Transactional
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final UserRepository userRepository;
    private final ClientMapper clientMapper;
    private final UserMapper userMapper;

    @Override
    public List<ClientResponse> findAll() {
        return clientMapper.toResponseList(clientRepository.findAll());
    }

    @Override
    public ClientResponse findById(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + id));
        return clientMapper.toResponse(client);
    }

    @Override
    public ClientResponse findByUserId(Long userId) {
        Client client = clientRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found for user id: " + userId));
        return clientMapper.toResponse(client);
    }

    @Override
    public ClientResponse update(Long id, ClientRequest request) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + id));

        if (!request.getEmail().equals(client.getEmail())
                && clientRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new BadRequestException("Email already exists");
        }

        client.setEmail(request.getEmail());
        Client updatedClient = clientRepository.save(client);
        return clientMapper.toResponse(updatedClient);
    }

    @Override
    public void delete(Long id) {
        if (!clientRepository.existsById(id)) {
            throw new ResourceNotFoundException("Client not found with id: " + id);
        }
        clientRepository.deleteById(id);
    }

    @Override
    public UserResponse createClient(CreateClientRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new BadRequestException("Username already exists");
        }

        if (clientRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already exists");
        }

        User user = User.builder()
                .username(request.getUsername())
                .password(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()))
                .name(request.getName())
                .role(UserRole.CLIENT)
                .build();
        user = userRepository.save(user);

        Client client = Client.builder()
                .user(user)
                .email(request.getEmail())
                .build();
        clientRepository.save(client);

        user.setClient(client);
        return userMapper.toResponse(user);
    }
}
