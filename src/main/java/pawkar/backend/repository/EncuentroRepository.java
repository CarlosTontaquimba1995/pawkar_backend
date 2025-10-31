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
    
    @Query(value = """
            SELECT DISTINCT e.* FROM encuentros e
            LEFT JOIN participacion_encuentro pe ON e.encuentro_id = pe.encuentro_id
            WHERE (:subcategoriaId IS NULL OR e.subcategoria_id = :subcategoriaId)
            AND (cast(:fechaInicio as timestamp) IS NULL OR e.fecha_hora >= :fechaInicio)
            AND (cast(:fechaFin as timestamp) IS NULL OR e.fecha_hora <= :fechaFin)
            AND (:estadioId IS NULL OR e.estadio_id = :estadioId)
            AND (COALESCE(:estado, '') = '' OR e.estado = :estado)
            AND (:equipoId IS NULL OR pe.equipo_id = :equipoId)
            ORDER BY e.fecha_hora
            """,
        countQuery = """
                            SELECT COUNT(DISTINCT e.encuentro_id) FROM encuentros e
                            LEFT JOIN participacion_encuentro pe ON e.encuentro_id = pe.encuentro_id
            WHERE (:subcategoriaId IS NULL OR e.subcategoria_id = :subcategoriaId)
            AND (cast(:fechaInicio as timestamp) IS NULL OR e.fecha_hora >= :fechaInicio)
            AND (cast(:fechaFin as timestamp) IS NULL OR e.fecha_hora <= :fechaFin)
            AND (:estadioId IS NULL OR e.estadio_id = :estadioId)
            AND (COALESCE(:estado, '') = '' OR e.estado = :estado)
                            AND (:equipoId IS NULL OR pe.equipo_id = :equipoId)
            """,
        nativeQuery = true
    )
    Page<Encuentro> searchEncuentros(
        @Param("subcategoriaId") Integer subcategoriaId,
        @Param("fechaInicio") LocalDateTime fechaInicio,
        @Param("fechaFin") LocalDateTime fechaFin,
        @Param("estadioId") Integer estadioId,
        @Param("estado") String estado,
        @Param("equipoId") Integer equipoId,
        Pageable pageable
    );

    @Query(value = """
            SELECT DISTINCT e.* FROM encuentros e
            LEFT JOIN participacion_encuentro pe ON e.encuentro_id = pe.encuentro_id
            WHERE (:subcategoriaId IS NULL OR e.subcategoria_id = :subcategoriaId)
            AND (cast(:fechaInicio as timestamp) IS NULL OR e.fecha_hora >= :fechaInicio)
            AND (cast(:fechaFin as timestamp) IS NULL OR e.fecha_hora <= :fechaFin)
            AND (:estadioId IS NULL OR e.estadio_id = :estadioId)
            AND (COALESCE(:estado, '') = '' OR e.estado = :estado)
            AND (:equipoId IS NULL OR pe.equipo_id = :equipoId)
            ORDER BY e.fecha_hora
            """,
        nativeQuery = true
    )
    List<Encuentro> searchEncuentrosWithoutPagination(
        @Param("subcategoriaId") Integer subcategoriaId,
        @Param("fechaInicio") LocalDateTime fechaInicio,
        @Param("fechaFin") LocalDateTime fechaFin,
        @Param("estadioId") Integer estadioId,
        @Param("estado") String estado,
        @Param("equipoId") Integer equipoId
    );
}
