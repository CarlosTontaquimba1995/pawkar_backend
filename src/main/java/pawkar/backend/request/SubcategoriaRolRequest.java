package pawkar.backend.request;

import jakarta.validation.constraints.NotNull;

public class SubcategoriaRolRequest {
    @NotNull(message = "El ID de la subcategoría es obligatorio")
    private Integer subcategoriaId;
    
    @NotNull(message = "El ID del rol es obligatorio")
    private Long rolId;

    // Getters and Setters
    public Integer getSubcategoriaId() {
        return subcategoriaId;
    }

    public void setSubcategoriaId(Integer subcategoriaId) {
        this.subcategoriaId = subcategoriaId;
    }

    public Long getRolId() {
        return rolId;
    }

    public void setRolId(Long rolId) {
        this.rolId = rolId;
    }
}
