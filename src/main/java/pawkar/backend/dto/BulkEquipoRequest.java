package pawkar.backend.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public class BulkEquipoRequest {
    @NotNull(message = "La lista de equipos no puede ser nula")
    @Size(min = 1, message = "Debe proporcionar al menos un equipo")
    private List<@Valid EquipoRequest> equipos;

    public List<EquipoRequest> getEquipos() {
        return equipos;
    }

    public void setEquipos(List<EquipoRequest> equipos) {
        this.equipos = equipos;
    }
}
