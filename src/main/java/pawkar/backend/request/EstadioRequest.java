package pawkar.backend.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EstadioRequest {
    @NotBlank(message = "El nombre del estadio es obligatorio")
    @Size(max = 150, message = "El nombre no puede tener más de 150 caracteres")
    private String nombre;

    private String detalle;

    private Boolean activo;
}
