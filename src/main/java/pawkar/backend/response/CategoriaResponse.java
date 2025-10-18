package pawkar.backend.response;

public class CategoriaResponse {
    private Integer categoriaId;
    private String nombre;

    public CategoriaResponse() {
    }

    public CategoriaResponse(Integer categoriaId, String nombre) {
        this.categoriaId = categoriaId;
        this.nombre = nombre;
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
}
