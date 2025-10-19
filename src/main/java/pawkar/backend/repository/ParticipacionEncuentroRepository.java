package pawkar.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pawkar.backend.entity.ParticipacionEncuentro;
import pawkar.backend.entity.Encuentro;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParticipacionEncuentroRepository
        extends JpaRepository<ParticipacionEncuentro, ParticipacionEncuentro.ParticipacionEncuentroId> {

    @Query("SELECT p FROM ParticipacionEncuentro p WHERE p.id.encuentroId = :encuentroId")
    List<ParticipacionEncuentro> findByEncuentroId(@Param("encuentroId") Integer encuentroId);

    @Query("SELECT p FROM ParticipacionEncuentro p WHERE p.id.equipoId = :equipoId")
    List<ParticipacionEncuentro> findByEquipoId(@Param("equipoId") Integer equipoId);

    @Query("SELECT p FROM ParticipacionEncuentro p WHERE p.id.encuentroId = :encuentroId AND p.id.equipoId = :equipoId")
    Optional<ParticipacionEncuentro> findByEncuentroIdAndEquipoId(
            @Param("encuentroId") Integer encuentroId,
            @Param("equipoId") Integer equipoId);

    @Query("SELECT COUNT(p) > 0 FROM ParticipacionEncuentro p WHERE p.id.encuentroId = :encuentroId AND p.id.equipoId = :equipoId")
    boolean existsByEncuentroIdAndEquipoId(
            @Param("encuentroId") Integer encuentroId,
            @Param("equipoId") Integer equipoId);
            
    @Query("SELECT p.encuentro FROM ParticipacionEncuentro p WHERE p.id.equipoId = :equipoId")
    List<Encuentro> findEncuentrosByEquipoId(@Param("equipoId") Integer equipoId);
}
