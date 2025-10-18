package pawkar.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pawkar.backend.entity.Encuentro;

import java.util.List;

@Repository
public interface EncuentroRepository extends JpaRepository<Encuentro, Long> {
    List<Encuentro> findBySubcategoria_SubcategoriaId(Long subcategoriaId);
    List<Encuentro> findByEstado(String estado);
}
