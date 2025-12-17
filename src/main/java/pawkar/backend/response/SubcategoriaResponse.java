package pawkar.backend.response;

import java.time.LocalDateTime;

public class SubcategoriaResponse {
    private Integer subcategoriaId;
    private Integer categoriaId;
    private String categoriaNombre;
    private String nombre;
    private String descripcion;
    private Boolean estado;
    private LocalDateTime fechaHora;
    private Boolean proximo;
    private String ubicacion;
    private java.math.BigDecimal latitud;
    private java.math.BigDecimal longitud;

    public SubcategoriaResponse() {
    }

    public SubcategoriaResponse(Integer subcategoriaId, Integer categoriaId, String categoriaNombre, String nombre,
            String descripcion, Boolean estado, java.time.LocalDateTime fechaHora, Boolean proximo, String ubicacion,
            java.math.BigDecimal latitud, java.math.BigDecimal longitud) {
        this.subcategoriaId = subcategoriaId;
        this.categoriaId = categoriaId;
        this.categoriaNombre = categoriaNombre;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.estado = estado;
        this.fechaHora = fechaHora;
        this.proximo = proximo;
        this.ubicacion = ubicacion;
    }

    // Getters and Setters
    public Integer getSubcategoriaId() {
        return subcategoriaId;
    }

    public void setSubcategoriaId(Integer subcategoriaId) {
        this.subcategoriaId = subcategoriaId;
    }

    public Integer getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(Integer categoriaId) {
        this.categoriaId = categoriaId;
    }

    public String getCategoriaNombre() {
        return categoriaNombre;
    }

    public void setCategoriaNombre(String categoriaNombre) {
        this.categoriaNombre = categoriaNombre;
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

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public java.time.LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(java.time.LocalDateTime fechaHora) {
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
}
