package pawkar.backend.entity;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "artistas")
public class Artista {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long artistaId;

    @Column(nullable = false, length = 150)
    private String nombre;

    private String genero;

    // Usamos JsonIgnore para evitar bucles infinitos al convertir a JSON
    @ManyToMany(mappedBy = "artistas")
    @JsonIgnore
    private Set<Subcategoria> subcategorias = new HashSet<>();

    public Artista() {}

    // Getters y Setters
    public Long getArtistaId() { return artistaId; }
    public void setArtistaId(Long artistaId) { this.artistaId = artistaId; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }
    public Set<Subcategoria> getSubcategorias() { return subcategorias; }
    public void setSubcategorias(Set<Subcategoria> subcategorias) { this.subcategorias = subcategorias; }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Artista artista = (Artista) o;
        return Objects.equals(artistaId, artista.artistaId) &&
                Objects.equals(nombre, artista.nombre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(artistaId, nombre);
    }
}