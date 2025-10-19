package pawkar.backend.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ParticipacionEncuentroRequest {
    @NotNull(message = "El ID del encuentro es obligatorio")
    private Integer encuentroId;
    
    @NotNull(message = "El ID del equipo es obligatorio")
    private Integer equipoId;
    
    private Boolean esLocal = false;
    private Integer golesPuntos = 0;
}
