package pawkar.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "plantilla")
@IdClass(PlantillaId.class)
public class Plantilla {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipo_id", nullable = false)
    private Equipo equipo;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "jugador_id", nullable = false)
    private Jugador jugador;

    @Column(name = "numero_camiseta")
    private Integer numeroCamiseta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rol_id", nullable = false)
    private Role rol;

    public static class PlantillaId implements java.io.Serializable {
        private Integer equipo;
        private Integer jugador;

        // Default constructor
        public PlantillaId() {
        }

        // Constructor with fields
        public PlantillaId(Integer equipoId, Integer jugadorId) {
            this.equipo = equipoId;
            this.jugador = jugadorId;
        }

        // Getters and setters
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

        // Required for composite key
        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            PlantillaId that = (PlantillaId) o;
            return equipo.equals(that.equipo) &&
                    jugador.equals(that.jugador);
        }

        @Override
        public int hashCode() {
            int result = equipo != null ? equipo.hashCode() : 0;
            result = 31 * result + (jugador != null ? jugador.hashCode() : 0);
            return result;
        }
    }
}
