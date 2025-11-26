package ma.microtech.smartShop.services;

import ma.microtech.smartShop.dto.request.ClientRequest;
import ma.microtech.smartShop.dto.request.CreateClientRequest;
import ma.microtech.smartShop.dto.response.ClientResponse;
import ma.microtech.smartShop.dto.response.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ClientService {
    List<ClientResponse> findAll();
    ClientResponse findById(Long id);
    ClientResponse findByUserId(Long userId);
    ClientResponse update(Long id, ClientRequest request);
    void delete(Long id);
    UserResponse createClient(CreateClientRequest request);
}
