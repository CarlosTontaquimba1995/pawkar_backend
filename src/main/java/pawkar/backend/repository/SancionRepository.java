package pawkar.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pawkar.backend.entity.Sancion;

import java.util.List;

@Repository
public interface SancionRepository extends JpaRepository<Sancion, Long> {
    List<Sancion> findByJugadorId(Long jugadorId);
    List<Sancion> findByEncuentroId(Integer encuentroId);
    List<Sancion> findByTipoSancion(String tipoSancion);
}
