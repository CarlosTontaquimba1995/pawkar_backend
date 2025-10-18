package pawkar.backend.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BulkSubcategoriaRequest {
    @NotEmpty(message = "La lista de subcategorías no puede estar vacía")
    private List<@Valid SubcategoriaRequest> subcategorias;
}
