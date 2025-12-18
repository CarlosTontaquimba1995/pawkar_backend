package pawkar.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pawkar.backend.entity.Ubicacion;

import java.util.Optional;

@Repository
public interface UbicacionRepository extends JpaRepository<Ubicacion, Long> {
    boolean existsByNemonicoIgnoreCase(String nemonico);
    Optional<Ubicacion> findByNemonicoIgnoreCase(String nemonico);
}
