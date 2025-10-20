package pawkar.backend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pawkar.backend.entity.Equipo;

import java.util.List;

@Repository
public interface EquipoRepository extends JpaRepository<Equipo, Integer> {
    
    List<Equipo> findBySerie_SerieId(Integer serieId);
    
    @Query("SELECT e FROM Equipo e WHERE e.subcategoria.subcategoriaId = :subcategoriaId")
    List<Equipo> findBySubcategoriaId(@Param("subcategoriaId") Integer subcategoriaId);
    
    @Query("SELECT e FROM Equipo e WHERE LOWER(e.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))")
    Page<Equipo> findByNombreContainingIgnoreCase(@Param("nombre") String nombre, Pageable pageable);
    
    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END FROM Equipo e " +
           "WHERE e.nombre = :nombre AND e.serie.serieId = :serieId")
    boolean existsByNombreAndSerieId(
        @Param("nombre") String nombre, 
        @Param("serieId") Integer serieId
    );
    
    @Query("SELECT e FROM Equipo e ORDER BY e.nombre")
    Page<Equipo> findAll(Pageable pageable);

    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END FROM Equipo e " +
           "WHERE e.nombre = :nombre AND e.serie.serieId = :serieId AND e.equipoId != :excludeEquipoId")
    boolean existsByNombreAndSerieIdExcludingId(
        @Param("nombre") String nombre, 
        @Param("serieId") Integer serieId,
        @Param("excludeEquipoId") Integer excludeEquipoId
    );
}
