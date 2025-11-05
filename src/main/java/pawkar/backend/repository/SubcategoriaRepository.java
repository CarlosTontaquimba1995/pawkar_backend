package pawkar.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pawkar.backend.entity.Subcategoria;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SubcategoriaRepository extends JpaRepository<Subcategoria, Integer> {
    List<Subcategoria> findByCategoria_CategoriaId(Integer categoriaId);
    
    boolean existsByNombreIgnoreCase(String nombre);
    
    boolean existsByCategoriaCategoriaIdAndNombreIgnoreCase(Integer categoriaId, String nombre);
    
    List<Subcategoria> findByProximoTrueAndFechaHoraBefore(LocalDateTime fechaHora);
}
