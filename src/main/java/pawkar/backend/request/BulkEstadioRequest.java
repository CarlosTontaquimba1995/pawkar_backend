package pawkar.backend.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BulkEstadioRequest {
    @NotNull(message = "La lista de estadios no puede ser nula")
    private List<@Valid EstadioRequest> estadios;
}
