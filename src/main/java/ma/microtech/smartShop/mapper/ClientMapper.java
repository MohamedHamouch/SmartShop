package ma.microtech.smartShop.mapper;

import ma.microtech.smartShop.dto.request.ClientRequest;
import ma.microtech.smartShop.dto.response.ClientResponse;
import ma.microtech.smartShop.entities.Client;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ClientMapper {

    @Mapping(target = "user", ignore = true)
    Client toEntity(ClientRequest request);

    ClientResponse toResponse(Client client);

    List<ClientResponse> toResponseList(List<Client> clients);
}
