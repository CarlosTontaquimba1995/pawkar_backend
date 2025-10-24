package pawkar.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubcategoriaRolDto {
    private Long rolId;
    private String rolName;
    private String rolDetail;
    private Integer subcategoriaId;
    private String subcategoriaName;
}
