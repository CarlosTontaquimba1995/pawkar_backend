package pawkar.backend.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PlantillaRequest {
    @NotNull(message = "El ID del equipo es obligatorio")
    private Integer equipoId;
    
    @NotNull(message = "El ID del jugador es obligatorio")
    private Integer jugadorId;
    
    private Integer numeroCamiseta;
    
    @NotNull(message = "El ID del rol es obligatorio")
    private Long rolId;
}
