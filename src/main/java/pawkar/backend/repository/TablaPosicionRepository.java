package pawkar.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import pawkar.backend.entity.TablaPosicion;
import pawkar.backend.entity.TablaPosicionId;

@Repository
public interface TablaPosicionRepository
        extends JpaRepository<TablaPosicion, TablaPosicionId>, JpaSpecificationExecutor<TablaPosicion> {

    // Find all positions by subcategoria ID, ordered by points (descending)
    List<TablaPosicion> findBySubcategoriaSubcategoriaIdOrderByPuntosDesc(Integer subcategoriaId);

    // Find position by subcategoria and equipo
    Optional<TablaPosicion> findBySubcategoriaSubcategoriaIdAndEquipoEquipoId(Integer subcategoriaId, Integer equipoId);
    
    // Check if position exists by subcategoria and equipo
    boolean existsBySubcategoriaSubcategoriaIdAndEquipoEquipoId(Integer subcategoriaId, Integer equipoId);
    
    // Delete position by subcategoria and equipo
    void deleteBySubcategoriaSubcategoriaIdAndEquipoEquipoId(Integer subcategoriaId, Integer equipoId);

    // Search with specifications and pagination
    Page<TablaPosicion> findAll(Specification<TablaPosicion> spec, Pageable pageable);
}