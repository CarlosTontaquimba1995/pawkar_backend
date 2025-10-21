package pawkar.backend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    @Query("SELECT j FROM Jugador j WHERE j.estado = :estado")
    Page<Jugador> findByEstado(@Param("estado") boolean estado, Pageable pageable);

    @Query("SELECT j FROM Jugador j WHERE (LOWER(j.nombre) LIKE LOWER(concat('%', :busqueda, '%')) OR LOWER(j.apellido) LIKE LOWER(concat('%', :busqueda, '%'))) AND j.estado = :estado")
    Page<Jugador> buscarPorNombreOApellidoYEstado(@Param("busqueda") String busqueda, @Param("estado") boolean estado,
            Pageable pageable);

    @Modifying
    @Query("UPDATE Jugador j SET j.estado = false WHERE j.id = :id")
    void softDelete(@Param("id") Integer id);

    @Query("SELECT j FROM Jugador j WHERE j.id = :id AND j.estado = true")
    Optional<Jugador> findByIdAndEstadoTrue(@Param("id") Integer id);

    @Query("SELECT j FROM Jugador j WHERE j.documentoIdentidad = :documentoIdentidad AND j.estado = true")
    Optional<Jugador> findByDocumentoIdentidadAndEstadoTrue(@Param("documentoIdentidad") String documentoIdentidad);

    @Query("SELECT CASE WHEN COUNT(j) > 0 THEN true ELSE false END FROM Jugador j WHERE j.documentoIdentidad = :documentoIdentidad AND j.estado = true")
    boolean existsByDocumentoIdentidadAndEstadoTrue(@Param("documentoIdentidad") String documentoIdentidad);

    @Query("SELECT CASE WHEN COUNT(j) > 0 THEN true ELSE false END FROM Jugador j WHERE j.id = :id AND j.estado = true")
    boolean existsByIdAndEstadoTrue(@Param("id") Integer id);
    
    @Query("SELECT COUNT(j) FROM Jugador j WHERE j.estado = true")
    long countByEstadoTrue();
}
