package pawkar.backend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pawkar.backend.entity.Encuentro;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EncuentroRepository extends JpaRepository<Encuentro, Integer>, JpaSpecificationExecutor<Encuentro> {
    
    List<Encuentro> findBySubcategoria_SubcategoriaId(Integer subcategoriaId);
    
    List<Encuentro> findByEstado(String estado);
    
    @Query("""
        SELECT e FROM Encuentro e 
        WHERE (:titulo IS NULL OR LOWER(e.titulo) LIKE LOWER(CONCAT('%', :titulo, '%')))
        AND (:subcategoriaId IS NULL OR e.subcategoria.subcategoriaId = :subcategoriaId)
        AND (:fechaInicio IS NULL OR e.fechaHora >= :fechaInicio)
        AND (:fechaFin IS NULL OR e.fechaHora <= :fechaFin)
        AND (:estadioLugar IS NULL OR LOWER(e.estadioLugar) LIKE LOWER(CONCAT('%', :estadioLugar, '%')))
        AND (:estado IS NULL OR e.estado = :estado)
    """)
    Page<Encuentro> searchEncuentros(
        @Param("titulo") String titulo,
        @Param("subcategoriaId") Integer subcategoriaId,
        @Param("fechaInicio") LocalDateTime fechaInicio,
        @Param("fechaFin") LocalDateTime fechaFin,
        @Param("estadioLugar") String estadioLugar,
        @Param("estado") String estado,
        Pageable pageable
    );
}
