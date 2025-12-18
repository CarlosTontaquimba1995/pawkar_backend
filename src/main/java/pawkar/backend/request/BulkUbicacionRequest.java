package pawkar.backend.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public class BulkUbicacionRequest {
    @NotNull(message = "La lista de ubicaciones no puede ser nula")
    private List<@Valid UbicacionRequest> ubicaciones;

    public List<UbicacionRequest> getUbicaciones() {
        return ubicaciones;
    }

    public void setUbicaciones(List<UbicacionRequest> ubicaciones) {
        this.ubicaciones = ubicaciones;
    }
}
