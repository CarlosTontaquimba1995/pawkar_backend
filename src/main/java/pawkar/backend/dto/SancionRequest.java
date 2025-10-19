package pawkar.backend.dto;

import java.time.LocalDate;

public class SancionRequest {
    private Long jugadorId;
    private Integer encuentroId;
    private String tipoSancion;
    private String motivo;
    private String detalleSancion;
    private LocalDate fechaRegistro;

    // Getters and Setters
    public Long getJugadorId() {
        return jugadorId;
    }

    public void setJugadorId(Long jugadorId) {
        this.jugadorId = jugadorId;
    }

    public Integer getEncuentroId() {
        return encuentroId;
    }

    public void setEncuentroId(Integer encuentroId) {
        this.encuentroId = encuentroId;
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
