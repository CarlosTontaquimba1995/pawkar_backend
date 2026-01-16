package pawkar.backend.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.HashSet;

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
    private BigDecimal latitud;
    private BigDecimal longitud;
    private BigDecimal precio;
    private Set<ArtistaResponse> artistas = new HashSet<>();

    public SubcategoriaResponse() {
    }

    public SubcategoriaResponse(Integer subcategoriaId, Integer categoriaId, String categoriaNombre, String nombre,
            String descripcion, Boolean estado, java.time.LocalDateTime fechaHora, Boolean proximo, String ubicacion,
            BigDecimal latitud, BigDecimal longitud, BigDecimal precio) {
        this.subcategoriaId = subcategoriaId;
        this.categoriaId = categoriaId;
        this.categoriaNombre = categoriaNombre;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.estado = estado;
        this.fechaHora = fechaHora;
        this.proximo = proximo;
        this.ubicacion = ubicacion;
        this.latitud = latitud;
        this.longitud = longitud;
        this.precio = precio;
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

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public Set<ArtistaResponse> getArtistas() {
        return artistas;
    }

    public void setArtistas(Set<ArtistaResponse> artistas) {
        this.artistas = artistas != null ? artistas : new HashSet<>();
    }
}
