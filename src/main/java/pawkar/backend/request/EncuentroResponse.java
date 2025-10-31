package pawkar.backend.request;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class EncuentroResponse {
    private Integer id;
    private Integer subcategoriaId;
    private String subcategoriaNombre;
    private String titulo;
    private LocalDateTime fechaHora;
    private String estadioLugar;
    private String estado;
    private boolean activo;
}
