package pawkar.backend.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlantillaResponse {
    private Integer equipoId;
    private String equipoNombre;
    private Integer jugadorId;
    private String jugadorNombreCompleto;
    private Integer numeroCamiseta;
    private Long rolId;
    private String rolNombre;
    private boolean tieneSancion;
    private List<SancionInfo> sanciones;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SancionInfo {
        private Long sancionId;
        private String tipoSancion;
        private String motivo;
        private String detalleSancion;
        private String fechaRegistro;
    }
}
