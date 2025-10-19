package pawkar.backend.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ParticipacionEncuentroResponse {
    private Integer encuentroId;
    private Integer equipoId;
    private String nombreEquipo;
    private Boolean esLocal;
    private Integer golesPuntos;
}
