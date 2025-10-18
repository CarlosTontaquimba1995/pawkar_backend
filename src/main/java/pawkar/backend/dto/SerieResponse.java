package pawkar.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SerieResponse {
    private Integer serieId;
    private Integer subcategoriaId;
    private String subcategoriaNombre;
    private String nombreSerie;
}