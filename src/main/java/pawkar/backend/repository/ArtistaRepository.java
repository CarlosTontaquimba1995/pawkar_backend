package pawkar.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import io.lettuce.core.dynamic.annotation.Param;
import pawkar.backend.entity.Artista;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ArtistaRepository extends JpaRepository<Artista, Long> {
    @Query("SELECT a FROM Artista a WHERE a.artistaId IN :ids")
    List<Artista> findByIdIn(@Param("ids") Set<Long> ids);

    Optional<Artista> findByNombreIgnoreCase(String nombre);
}
