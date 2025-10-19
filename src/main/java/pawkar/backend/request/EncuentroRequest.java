package pawkar.backend.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class EncuentroRequest {
    @NotNull(message = "El ID de subcategoría es obligatorio")
    private Integer subcategoriaId;

    @NotBlank(message = "El título es obligatorio")
    @Size(max = 150, message = "El título no puede tener más de 150 caracteres")
    private String titulo;

    @NotNull(message = "La fecha y hora son obligatorias")
    private LocalDateTime fechaHora;

    @NotBlank(message = "El estadio o lugar es obligatorio")
    @Size(max = 150, message = "El lugar no puede tener más de 150 caracteres")
    private String estadioLugar;

    @Size(max = 50, message = "El estado no puede tener más de 50 caracteres")
    private String estado;
}
