package pawkar.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "categorias")
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer categoriaId;

    @Column(nullable = false, unique = true, length = 100)
    private String nombre;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean estado = true;

    @Column(nullable = false, unique = true, length = 100)
    private String nemonico;

    public Categoria() {
    }

    public Categoria(String nombre) {
        this.nombre = nombre;
        this.estado = true;
        this.nemonico = generateNemonico(nombre);
    }
    
    private String generateNemonico(String nombre) {
        if (nombre == null) {
            return null;
        }
        // Convert to uppercase, replace spaces with underscores, and remove special characters
        return nombre.trim()
                .toUpperCase()
                .replaceAll("\\s+", "_")
                .replaceAll("[^A-Z0-9_]", "");
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

    public Boolean isEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public String getNemonico() {
        return nemonico;
    }

    public void setNemonico(String nemonico) {
        this.nemonico = nemonico;
    }
}
