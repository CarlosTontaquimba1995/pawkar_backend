package pawkar.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@Entity
@Table(name = "subcategoria_roles")
@IdClass(SubcategoriaRol.SubcategoriaRolId.class)
public class SubcategoriaRol {
    
    @Id
    @ManyToOne
    @JoinColumn(name = "subcategoria_id")
    private Subcategoria subcategoria;
    
    @Id
    @ManyToOne
    @JoinColumn(name = "rol_id")
    private Role rol;
    
    public SubcategoriaRol(Subcategoria subcategoria, Role rol) {
        this.subcategoria = subcategoria;
        this.rol = rol;
    }
    
    @Data
    @NoArgsConstructor
    public static class SubcategoriaRolId implements Serializable {
        private Integer subcategoria;
        private Long rol;
    }
}
