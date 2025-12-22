package pawkar.backend.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class SubcategoriaRequest {
    @NotNull(message = "El ID de categoría es obligatorio")
    private Long categoriaId;
    
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;
    
    private String descripcion;
    
    private LocalDateTime fechaHora;
    
    private Boolean proximo;

    private String ubicacion;

    private java.math.BigDecimal latitud;

    private java.math.BigDecimal longitud;

    private Set<Long> artistasIds = new HashSet<>();

    // Getters and Setters
    public Long getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(Long categoriaId) {
        this.categoriaId = categoriaId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public Boolean getProximo() {
        return proximo;
    }

    public void setProximo(Boolean proximo) {
        this.proximo = proximo;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public java.math.BigDecimal getLatitud() {
        return latitud;
    }

    public void setLatitud(java.math.BigDecimal latitud) {
        this.latitud = latitud;
    }

    public java.math.BigDecimal getLongitud() {
        return longitud;
    }

    public void setLongitud(java.math.BigDecimal longitud) {
        this.longitud = longitud;
    }

    public Set<Long> getArtistasIds() {
        return artistasIds;
    }

    public void setArtistasIds(Set<Long> artistasIds) {
        this.artistasIds = artistasIds != null ? artistasIds : new HashSet<>();
    }
}
