package pawkar.backend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pawkar.backend.entity.Jugador;

import java.util.List;
import java.util.Optional;

@Repository
public interface JugadorRepository extends JpaRepository<Jugador, Integer> {
    boolean existsByDocumentoIdentidad(String documentoIdentidad);

    Optional<Jugador> findByDocumentoIdentidad(String documentoIdentidad);
    
    List<Jugador> findByDocumentoIdentidadIn(List<String> documentos);
    
    Page<Jugador> findByNombreContainingIgnoreCaseOrApellidoContainingIgnoreCase(
        String nombre, String apellido, Pageable pageable);
}
