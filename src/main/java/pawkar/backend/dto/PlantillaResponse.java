package pawkar.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlantillaResponse {
    private Integer equipoId;
    private String equipoNombre;
    private Integer jugadorId;
    private String jugadorNombreCompleto;
    private Integer numeroCamiseta;
    private Long rolId;
    private String rolNombre;
}
