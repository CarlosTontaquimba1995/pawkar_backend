package pawkar.backend.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class JugadorBulkRequest {
    @NotEmpty(message = "La lista de jugadores no puede estar vacía")
    private List<@Valid JugadorRequest> jugadores;
}
