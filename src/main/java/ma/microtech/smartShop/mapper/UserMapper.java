package ma.microtech.smartShop.mapper;

import ma.microtech.smartShop.dto.request.UserRequest;
import ma.microtech.smartShop.dto.response.UserResponse;
import ma.microtech.smartShop.entities.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ClientMapper.class})
public interface UserMapper {

    User toEntity(UserRequest request);

    UserResponse toResponse(User user);

    List<UserResponse> toResponseList(List<User> users);
}
