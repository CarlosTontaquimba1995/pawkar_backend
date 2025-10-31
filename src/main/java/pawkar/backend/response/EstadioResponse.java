package pawkar.backend.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EstadioResponse {
    private Integer id;
    private String nombre;
    private String detalle;
    private boolean estado;
}
