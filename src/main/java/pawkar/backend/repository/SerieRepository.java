package pawkar.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pawkar.backend.entity.Serie;

import java.util.List;
import java.util.Optional;

@Repository
public interface SerieRepository extends JpaRepository<Serie, Integer> {
    List<Serie> findBySubcategoria_SubcategoriaId(Integer subcategoriaId);
    
    @Query("SELECT s FROM Serie s WHERE s.subcategoria.subcategoriaId = :subcategoriaId AND s.nombreSerie = :nombreSerie")
    Optional<Serie> findBySubcategoriaAndNombreSerie(
            @Param("subcategoriaId") Integer subcategoriaId,
            @Param("nombreSerie") String nombreSerie
    );
    
    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END FROM Serie s " +
           "WHERE s.subcategoria.subcategoriaId = :subcategoriaId " +
           "AND s.nombreSerie = :nombreSerie " +
           "AND s.serieId != :excludeSerieId")
    boolean existsBySubcategoriaAndNombreSerieExcludingId(
            @Param("subcategoriaId") Integer subcategoriaId,
            @Param("nombreSerie") String nombreSerie,
            @Param("excludeSerieId") Integer excludeSerieId
    );
}
