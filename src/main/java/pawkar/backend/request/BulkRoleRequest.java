package pawkar.backend.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BulkRoleRequest {
    @NotNull(message = "Roles list cannot be null")
    @Valid
    private List<RoleRequest> roles;
}
