package pawkar.backend.response;

public class CategoriaResponse {
    private Integer categoriaId;
    private String nombre;
    private Boolean estado;

    public CategoriaResponse() {
    }

    public CategoriaResponse(Integer categoriaId, String nombre, Boolean estado) {
        this.categoriaId = categoriaId;
        this.nombre = nombre;
        this.estado = estado;
    }

    // Getters and Setters
    public Integer getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(Integer categoriaId) {
        this.categoriaId = categoriaId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }
}
