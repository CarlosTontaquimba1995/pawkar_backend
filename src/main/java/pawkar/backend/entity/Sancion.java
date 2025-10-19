package pawkar.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "sanciones")
public class Sancion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sancion_id", columnDefinition = "BIGSERIAL")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "jugador_id", nullable = false)
    private Jugador jugador;

    @ManyToOne
    @JoinColumn(name = "encuentro_id")
    private Encuentro encuentro;

    @Column(name = "tipo_sancion", nullable = false, length = 50)
    private String tipoSancion;

    @Column(name = "motivo")
    private String motivo;

    @Column(name = "detalle_sancion")
    private String detalleSancion;

    @Column(name = "fecha_registro")
    private LocalDate fechaRegistro = LocalDate.now();

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Jugador getJugador() {
        return jugador;
    }

    public void setJugador(Jugador jugador) {
        this.jugador = jugador;
    }

    public Encuentro getEncuentro() {
        return encuentro;
    }

    public void setEncuentro(Encuentro encuentro) {
        this.encuentro = encuentro;
    }

    public String getTipoSancion() {
        return tipoSancion;
    }

    public void setTipoSancion(String tipoSancion) {
        this.tipoSancion = tipoSancion;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getDetalleSancion() {
        return detalleSancion;
    }

    public void setDetalleSancion(String detalleSancion) {
        this.detalleSancion = detalleSancion;
    }

    public LocalDate getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDate fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }
}
