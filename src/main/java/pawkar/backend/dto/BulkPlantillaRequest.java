package pawkar.backend.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BulkPlantillaRequest {
    @NotEmpty(message = "La lista de jugadores no puede estar vacía")
    private List<@Valid PlantillaRequest> jugadores;
}
