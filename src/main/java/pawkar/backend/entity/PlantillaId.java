package pawkar.backend.entity;

import java.io.Serializable;
import java.util.Objects;

public class PlantillaId implements Serializable {
    private Integer equipo;
    private Integer jugador;

    // Default constructor
    public PlantillaId() {
    }

    // Constructor with parameters
    public PlantillaId(Integer equipo, Integer jugador) {
        this.equipo = equipo;
        this.jugador = jugador;
    }

    // Getters and Setters
    public Integer getEquipo() {
        return equipo;
    }

    public void setEquipo(Integer equipo) {
        this.equipo = equipo;
    }

    public Integer getJugador() {
        return jugador;
    }

    public void setJugador(Integer jugador) {
        this.jugador = jugador;
    }

    // equals and hashCode methods
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        PlantillaId that = (PlantillaId) o;
        return Objects.equals(equipo, that.equipo) &&
                Objects.equals(jugador, that.jugador);
    }

    @Override
    public int hashCode() {
        return Objects.hash(equipo, jugador);
    }
}
