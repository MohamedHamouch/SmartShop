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

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.microtech.smartShop.dto.ApiResponse;
import ma.microtech.smartShop.dto.request.ClientRequest;
import ma.microtech.smartShop.dto.request.CreateClientRequest;
import ma.microtech.smartShop.dto.response.ClientResponse;
import ma.microtech.smartShop.dto.response.UserResponse;
import ma.microtech.smartShop.services.ClientService;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ClientResponse>>> getAllClients() {
        List<ClientResponse> clients = clientService.findAll();
        return ResponseEntity.ok(
                ApiResponse.<List<ClientResponse>>builder()
                        .status(HttpStatus.OK.value())
                        .message("Clients retrieved successfully")
                        .data(clients)
                        .build()
        );
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<ClientResponse>> getMyClient(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        ClientResponse client = clientService.findByUserId(userId);
        return ResponseEntity.ok(
                ApiResponse.<ClientResponse>builder()
                        .status(HttpStatus.OK.value())
                        .message("Client profile retrieved successfully")
                        .data(client)
                        .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ClientResponse>> getClientById(@PathVariable Long id) {
        ClientResponse client = clientService.findById(id);
        return ResponseEntity.ok(
                ApiResponse.<ClientResponse>builder()
                        .status(HttpStatus.OK.value())
                        .message("Client retrieved successfully")
                        .data(client)
                        .build()
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponse<UserResponse>> createClient(@Valid @RequestBody CreateClientRequest request) {
        UserResponse userResponse = clientService.createClient(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.<UserResponse>builder()
                        .status(HttpStatus.CREATED.value())
                        .message("User and Client created successfully")
                        .data(userResponse)
                        .build()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ClientResponse>> updateClient(
            @PathVariable Long id,
            @Valid @RequestBody ClientRequest request) {
        ClientResponse client = clientService.update(id, request);
        return ResponseEntity.ok(
                ApiResponse.<ClientResponse>builder()
                        .status(HttpStatus.OK.value())
                        .message("Client updated successfully")
                        .data(client)
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteClient(@PathVariable Long id) {
        clientService.delete(id);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .status(HttpStatus.OK.value())
                        .message("Client deleted successfully")
                        .build()
        );
    }

}
