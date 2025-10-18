package pawkar.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubcategoriaRolResponseDto {
    private String categoriaNombre;
    private String subcategoria;
    private String rol;
}
