package pawkar.backend.request;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class TablaPosicionSearchRequest {
    private Integer subcategoriaId;
    private Integer categoriaId;
    private Integer equipoId;
    private Integer serieId;
    private String nombreEquipo;
    private Integer page = 0;
    private Integer size = 10;
    private String sort = "puntos,desc";

    // Getters y Setters
    public Integer getSubcategoriaId() {
        return subcategoriaId;
    }

    public void setSubcategoriaId(Integer subcategoriaId) {
        this.subcategoriaId = subcategoriaId;
    }

    public Integer getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(Integer categoriaId) {
        this.categoriaId = categoriaId;
    }

    public Integer getEquipoId() {
        return equipoId;
    }

    public void setEquipoId(Integer equipoId) {
        this.equipoId = equipoId;
    }

    public Integer getSerieId() {
        return serieId;
    }

    public void setSerieId(Integer serieId) {
        this.serieId = serieId;
    }

    public String getNombreEquipo() {
        return nombreEquipo;
    }

    public void setNombreEquipo(String nombreEquipo) {
        this.nombreEquipo = nombreEquipo;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }
    
    public Pageable toPageable() {
        if (sort != null && !sort.isEmpty()) {
            String[] sortParams = sort.split(",");
            if (sortParams.length == 2) {
                return PageRequest.of(
                    page != null ? page : 0,
                    size != null ? size : 10,
                    Sort.by(Sort.Direction.fromString(sortParams[1].toUpperCase()), sortParams[0])
                );
            }
        }
        return PageRequest.of(
            page != null ? page : 0,
            size != null ? size : 10,
            Sort.by(Sort.Direction.DESC, "puntos")
        );
    }
}
