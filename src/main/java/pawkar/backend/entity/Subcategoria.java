package pawkar.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.math.BigDecimal;

@Entity
@Table(name = "subcategorias")
public class Subcategoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer subcategoriaId;

    @ManyToOne
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;
    
    @Column(name = "fecha_hora")
    private LocalDateTime fechaHora;
    
    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean estado = true;
    
    @Column(columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean proximo = true;

    @Column(length = 100)
    private String ubicacion;

    @Column(precision = 9, scale = 6)
    private BigDecimal latitud;

    @Column(precision = 10, scale = 6)
    private BigDecimal longitud;

    @Column(nullable = false, length = 100, unique = true)
    private String nemonico;

    @OneToMany(mappedBy = "subcategoria", fetch = FetchType.LAZY)
    private List<Encuentro> encuentros = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "subcategoria_roles", joinColumns = @JoinColumn(name = "subcategoria_id"), inverseJoinColumns = @JoinColumn(name = "rol_id"))
    private Set<Role> roles = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "subcategoria_artistas", joinColumns = @JoinColumn(name = "subcategoria_id"), inverseJoinColumns = @JoinColumn(name = "artista_id"))
    private Set<Artista> artistas = new HashSet<>();

    public Subcategoria() {
    }

    public Subcategoria(String nombre, String descripcion, LocalDateTime fechaHora, Boolean estado, Boolean proximo,
            String ubicacion, String nemonico, BigDecimal latitud, BigDecimal longitud) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fechaHora = fechaHora;
        this.estado = estado;
        this.proximo = proximo;
        this.ubicacion = ubicacion;
        this.nemonico = nemonico;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    // Getters and Setters
    public Integer getSubcategoriaId() {
        return subcategoriaId;
    }

    public void setSubcategoriaId(Integer subcategoriaId) {
        this.subcategoriaId = subcategoriaId;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
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

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }
    
    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
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

    public String getNemonico() {
        return nemonico;
    }

    public void setNemonico(String nemonico) {
        this.nemonico = nemonico;
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

    // Helper methods for managing the relationship
    public void addRole(Role role) {
        this.roles.add(role);
        role.getSubcategorias().add(this);
    }

    public void removeRole(Role role) {
        this.roles.remove(role);
        role.getSubcategorias().remove(this);
    }

    // 3. Métodos helper para gestionar la relación cómodamente
    public void addArtista(Artista artista) {
        this.artistas.add(artista);
        artista.getSubcategorias().add(this);
    }

    public void removeArtista(Artista artista) {
        this.artistas.remove(artista);
        artista.getSubcategorias().remove(this);
    }

    public List<Encuentro> getEncuentros() {
        return encuentros;
    }
    
    public void setEncuentros(List<Encuentro> encuentros) {
        this.encuentros = encuentros;
    }

    public Set<Artista> getArtistas() {
        return artistas;
    }

    public void setArtistas(Set<Artista> artistas) {
        this.artistas = artistas;
    }
}
