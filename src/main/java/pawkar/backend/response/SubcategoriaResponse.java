package pawkar.backend.response;

public class SubcategoriaResponse {
    private Integer subcategoriaId;
    private Integer categoriaId;
    private String categoriaNombre;
    private String nombre;
    private String descripcion;

    public SubcategoriaResponse() {
    }

    public SubcategoriaResponse(Integer subcategoriaId, Integer categoriaId, String categoriaNombre, String nombre, String descripcion) {
        this.subcategoriaId = subcategoriaId;
        this.categoriaId = categoriaId;
        this.categoriaNombre = categoriaNombre;
        this.nombre = nombre;
        this.descripcion = descripcion;
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
}
