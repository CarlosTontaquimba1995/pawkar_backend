package pawkar.backend.request;

import lombok.Data;

import java.util.List;

@Data
public class BulkSubcategoriaRolRequest {
    private Integer subcategoriaId;
    private List<Long> roles;
}
