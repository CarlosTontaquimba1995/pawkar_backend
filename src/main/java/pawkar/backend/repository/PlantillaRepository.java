package pawkar.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pawkar.backend.entity.Plantilla;
import pawkar.backend.entity.PlantillaId;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlantillaRepository extends JpaRepository<Plantilla, PlantillaId> {
    boolean existsByEquipo_EquipoIdAndJugador_Id(Integer equipoId, Integer jugadorId);
    
    List<Plantilla> findByEquipo_EquipoId(Integer equipoId);
}
