package pawkar.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pawkar.backend.entity.Subcategoria;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SubcategoriaRepository extends JpaRepository<Subcategoria, Integer> {
    List<Subcategoria> findByCategoria_CategoriaId(Integer categoriaId);
    
    boolean existsByNombreIgnoreCase(String nombre);
    
    Optional<Subcategoria> findByNemonico(String nemonico);

    boolean existsByCategoriaCategoriaIdAndNombreIgnoreCase(Integer categoriaId, String nombre);
    
    boolean existsByCategoriaCategoriaIdAndNombreIgnoreCaseAndSubcategoriaIdNot(Integer categoriaId, String nombre,
            Integer subcategoriaId);

    List<Subcategoria> findByProximoTrueAndFechaHoraBefore(LocalDateTime fechaHora);
    
    List<Subcategoria> findByCategoria_CategoriaIdAndProximo(Integer categoriaId, boolean proximo);
}
