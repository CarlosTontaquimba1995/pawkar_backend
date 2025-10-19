package pawkar.backend.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import java.time.LocalDate;

@Getter
@Setter
public class EncuentroSearchRequest {
    private Integer categoriaId;
    private Integer subcategoriaId;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String estadioLugar;
    private Integer equipoId;
    private String estado;
    private int page = 0;
    private int size = 10;
    private String sortBy = "fechaHora";
    private Sort.Direction sortDirection = Sort.Direction.ASC;

    public Pageable toPageable() {
        return PageRequest.of(
                page,
                size,
                Sort.by(sortDirection, sortBy));
    }
}
