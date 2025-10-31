package pawkar.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pawkar.backend.entity.Estadio;

import java.util.List;
import java.util.Optional;

@Repository
public interface EstadioRepository extends JpaRepository<Estadio, Long> {
    Optional<Estadio> findByNombre(String nombre);
    boolean existsByNombre(String nombre);
    List<Estadio> findByNombreIn(List<String> nombres);
}
