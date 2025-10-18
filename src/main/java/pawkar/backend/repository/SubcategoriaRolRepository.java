package pawkar.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pawkar.backend.entity.SubcategoriaRol;

import java.util.List;

@Repository
public interface SubcategoriaRolRepository extends JpaRepository<SubcategoriaRol, SubcategoriaRol.SubcategoriaRolId> {

    boolean existsBySubcategoria_SubcategoriaIdAndRol_Id(Integer subcategoriaId, Long rolId);

    @Modifying
    @Query("DELETE FROM SubcategoriaRol sr WHERE sr.subcategoria.subcategoriaId = :subcategoriaId AND sr.rol.id = :rolId")
    void deleteBySubcategoria_SubcategoriaIdAndRol_Id(@Param("subcategoriaId") Integer subcategoriaId,
            @Param("rolId") Long rolId);

    @Query("SELECT sr FROM SubcategoriaRol sr WHERE sr.subcategoria.subcategoriaId = :subcategoriaId")
    java.util.List<SubcategoriaRol> findBySubcategoria_SubcategoriaId(@Param("subcategoriaId") Integer subcategoriaId);
    
    @Query("SELECT sr FROM SubcategoriaRol sr WHERE sr.subcategoria.subcategoriaId = :subcategoriaId AND sr.rol.id IN :rolIds")
    List<SubcategoriaRol> findBySubcategoria_SubcategoriaIdAndRol_IdIn(
            @Param("subcategoriaId") Integer subcategoriaId, 
            @Param("rolIds") List<Long> rolIds
    );
    
    @Query("SELECT sr FROM SubcategoriaRol sr WHERE LOWER(sr.subcategoria.nombre) = LOWER(:nombreSubcategoria)")
    List<SubcategoriaRol> findBySubcategoriaNombre(@Param("nombreSubcategoria") String nombreSubcategoria);
}
