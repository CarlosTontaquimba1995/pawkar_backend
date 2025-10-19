package pawkar.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name = "participacion_encuentro")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ParticipacionEncuentro implements Serializable {
    
    @EmbeddedId
    private ParticipacionEncuentroId id;
    
    @MapsId("encuentroId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "encuentro_id", nullable = false, insertable = false, updatable = false)
    private Encuentro encuentro;
    
    @MapsId("equipoId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipo_id", nullable = false, insertable = false, updatable = false)
    private Equipo equipo;
    
    @Column(name = "es_local", nullable = false)
    private Boolean esLocal = false;
    
    @Column(name = "goles_puntos")
    private Integer golesPuntos = 0;
    
    @Embeddable
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ParticipacionEncuentroId implements Serializable {
        @Column(name = "encuentro_id")
        private Integer encuentroId;
        
        @Column(name = "equipo_id")
        private Integer equipoId;
        
        // Equals and hashCode methods
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof ParticipacionEncuentroId)) return false;
            ParticipacionEncuentroId that = (ParticipacionEncuentroId) o;
            return encuentroId.equals(that.encuentroId) &&
                   equipoId.equals(that.equipoId);
        }
        
        @Override
        public int hashCode() {
            return 31 * encuentroId + equipoId;
        }
    }
}
