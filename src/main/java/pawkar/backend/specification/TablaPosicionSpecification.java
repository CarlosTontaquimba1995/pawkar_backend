package pawkar.backend.specification;

import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;
import pawkar.backend.entity.TablaPosicion;
import pawkar.backend.request.TablaPosicionSearchRequest;

public class TablaPosicionSpecification implements Specification<TablaPosicion> {

    private final TablaPosicionSearchRequest searchRequest;

    public TablaPosicionSpecification(TablaPosicionSearchRequest searchRequest) {
        this.searchRequest = searchRequest;
    }

    @Override
    public Predicate toPredicate(Root<TablaPosicion> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        Predicate predicate = cb.conjunction();

        // Filter by subcategoriaId
        if (searchRequest.getSubcategoriaId() != null) {
            predicate = cb.and(predicate,
                    cb.equal(root.get("subcategoria").get("subcategoriaId"), searchRequest.getSubcategoriaId()));
        }

        // Filter by categoriaId (if subcategoria has a relationship with categoria)
        if (searchRequest.getCategoriaId() != null) {
            predicate = cb.and(predicate,
                    cb.equal(root.get("subcategoria").get("categoria").get("categoriaId"),
                            searchRequest.getCategoriaId()));
        }

        // Filter by equipoId
        if (searchRequest.getEquipoId() != null) {
            predicate = cb.and(predicate,
                    cb.equal(root.get("equipo").get("equipoId"), searchRequest.getEquipoId()));
        }

        // Filter by serieId (if equipo has a relationship with serie)
        if (searchRequest.getSerieId() != null) {
            predicate = cb.and(predicate,
                    cb.equal(root.get("equipo").get("serie").get("serieId"), searchRequest.getSerieId()));
        }

        // Filter by nombreEquipo (case-insensitive partial match)
        if (searchRequest.getNombreEquipo() != null && !searchRequest.getNombreEquipo().isEmpty()) {
            predicate = cb.and(predicate,
                    cb.like(cb.lower(root.get("equipo").get("nombreEquipo")),
                            "%" + searchRequest.getNombreEquipo().toLowerCase() + "%"));
        }

        return predicate;
    }
}
