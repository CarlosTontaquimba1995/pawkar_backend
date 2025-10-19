package pawkar.backend.entity;

import java.io.Serializable;
import java.util.Objects;

public class TablaPosicionId implements Serializable {
    private Integer subcategoria;
    private Integer equipo;

    public TablaPosicionId() {}

    public TablaPosicionId(Integer subcategoria, Integer equipo) {
        this.subcategoria = subcategoria;
        this.equipo = equipo;
    }

    // Getters and Setters
    public Integer getSubcategoria() {
        return subcategoria;
    }

    public void setSubcategoria(Integer subcategoria) {
        this.subcategoria = subcategoria;
    }

    public Integer getEquipo() {
        return equipo;
    }

    public void setEquipo(Integer equipo) {
        this.equipo = equipo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TablaPosicionId that = (TablaPosicionId) o;
        return Objects.equals(subcategoria, that.subcategoria) &&
               Objects.equals(equipo, that.equipo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(subcategoria, equipo);
    }
}
