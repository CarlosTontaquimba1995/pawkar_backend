package pawkar.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pawkar.backend.entity.Configuracion;

@Repository
public interface ConfiguracionRepository extends JpaRepository<Configuracion, Long> {
    // Since there will be only one configuration, we can add a method to get it
    default Configuracion findFirst() {
        return findAll().stream().findFirst().orElse(null);
    }
}
