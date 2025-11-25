package ma.microtech.smartShop.dto.response;

import lombok.*;
import ma.microtech.smartShop.entities.enums.UserRole;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {

    private Long id;
    private String username;
    private String name;
    private UserRole role;
    private ClientResponse client;
}
