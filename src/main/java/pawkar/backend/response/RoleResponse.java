package pawkar.backend.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pawkar.backend.entity.Role;
import pawkar.backend.enums.ERole;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoleResponse {
    private Long id;
    private ERole name;
    private String detail;

    public static RoleResponse fromEntity(Role role) {
        if (role == null) {
            return null;
        }
        return new RoleResponse(
            role.getId(),
            role.getName(),
            role.getDetail()
        );
    }
}
