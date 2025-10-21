package pawkar.backend.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EquipoCountResponse {
    private long totalEquipos;
    private long equiposActivos;
    private long equiposInactivos;
}
