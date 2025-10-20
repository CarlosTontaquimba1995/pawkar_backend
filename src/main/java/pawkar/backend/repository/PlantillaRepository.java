package pawkar.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pawkar.backend.entity.Plantilla;
import pawkar.backend.entity.PlantillaId;

import java.util.List;

@Repository
public interface PlantillaRepository extends JpaRepository<Plantilla, PlantillaId> {
    boolean existsByEquipo_EquipoIdAndJugador_Id(Integer equipoId, Integer jugadorId);
    
    @Query("SELECT COUNT(p) > 0 FROM Plantilla p WHERE p.equipo.equipoId = :equipoId AND p.numeroCamiseta = :numeroCamiseta")
    boolean existsByEquipo_EquipoIdAndNumeroCamiseta(@Param("equipoId") Integer equipoId, 
                                                   @Param("numeroCamiseta") Integer numeroCamiseta);
    
    List<Plantilla> findByEquipo_EquipoId(Integer equipoId);
    
    long countByEquipo_EquipoId(Integer equipoId);
}
