package pawkar.backend.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.groups.Default;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;

@Getter
@Setter
@Validated
public class EncuentroRequest {
    @NotNull(message = "El ID de subcategoría es obligatorio")
    private Integer subcategoriaId;

    @NotBlank(message = "El título es obligatorio", groups = OnCreate.class)
    @Size(max = 150, message = "El título no puede tener más de 150 caracteres")
    private String titulo;

    @NotNull(message = "La fecha y hora son obligatorias")
    private LocalDateTime fechaHora;

    @NotNull(message = "El ID del estadio es obligatorio")
    private Long estadioId;

    @Size(max = 50, message = "El estado no puede tener más de 50 caracteres")
    private String estado;

    // Interface for validation group
    public interface OnCreate extends Default {
    }
}