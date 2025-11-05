package pawkar.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pawkar.backend.entity.Categoria;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    Optional<Categoria> findByNombre(String nombre);
    boolean existsByNombre(String nombre);
    List<Categoria> findByNombreIn(List<String> nombres);
    
    boolean existsByNemonico(String nemonico);
    
    @Query("SELECT c FROM Categoria c WHERE LOWER(c.nombre) IN ('eventos', 'evento')")
    Optional<Categoria> findEventosCategoria();
}
