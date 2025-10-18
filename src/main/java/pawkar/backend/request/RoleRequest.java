package pawkar.backend.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pawkar.backend.enums.ERole;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoleRequest {
    private ERole name;
    
    @NotBlank(message = "Detail is required")
    private String detail;
}
