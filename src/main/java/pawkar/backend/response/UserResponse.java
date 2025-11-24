package pawkar.backend.response;

import pawkar.backend.entity.Role;

import java.util.Set;

public record UserResponse(
    Long id,
    String username,
    String email,
    Set<Role> roles
) {
    public static UserResponse fromUser(pawkar.backend.entity.User user) {
        return new UserResponse(
            user.getId(),
            user.getUsername(),
            user.getEmail(),
            user.getRoles()
        );
    }
}
