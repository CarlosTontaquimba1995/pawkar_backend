package pawkar.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pawkar.backend.entity.Artista;

import java.util.List;
import java.util.Set;

@Repository
public interface ArtistaRepository extends JpaRepository<Artista, Long> {
    List<Artista> findByIdIn(Set<Long> ids);
}
