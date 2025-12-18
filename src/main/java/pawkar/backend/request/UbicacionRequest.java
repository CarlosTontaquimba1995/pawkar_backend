package pawkar.backend.request;

import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;

public class UbicacionRequest {
    @NotBlank(message = "La descripción es obligatoria")
    private String descripcion;
    
    private String nemonico;
    
    private Boolean estado;
    
    private BigDecimal latitud;
    
    private BigDecimal longitud;

    // Getters and Setters
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNemonico() {
        return nemonico;
    }

    public void setNemonico(String nemonico) {
        this.nemonico = nemonico;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public BigDecimal getLatitud() {
        return latitud;
    }

    public void setLatitud(BigDecimal latitud) {
        this.latitud = latitud;
    }

    public BigDecimal getLongitud() {
        return longitud;
    }

    public void setLongitud(BigDecimal longitud) {
        this.longitud = longitud;
    }
}
