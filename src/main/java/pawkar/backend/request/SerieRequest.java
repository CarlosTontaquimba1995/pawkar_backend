package pawkar.backend.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SerieRequest {
    @NotNull(message = "El ID de subcategoría es obligatorio")
    private Integer subcategoriaId;
    
    @NotBlank(message = "El nombre de la serie es obligatorio")
    private String nombreSerie;
}
