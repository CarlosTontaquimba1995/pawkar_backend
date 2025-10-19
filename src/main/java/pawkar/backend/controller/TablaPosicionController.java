package pawkar.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pawkar.backend.entity.TablaPosicion;
import pawkar.backend.request.TablaPosicionRequest;
import pawkar.backend.response.ApiResponseStandard;
import pawkar.backend.response.TablaPosicionResponse;
import pawkar.backend.service.TablaPosicionService;

import java.util.List;

@RestController
@RequestMapping("/tabla-posicion")
public class TablaPosicionController {

    private final TablaPosicionService tablaPosicionService;

    @Autowired
    public TablaPosicionController(TablaPosicionService tablaPosicionService) {
        this.tablaPosicionService = tablaPosicionService;
    }

    @GetMapping("/subcategoria/{subcategoriaId}")
    public ApiResponseStandard<List<TablaPosicionResponse>> getTablaPosicionBySubcategoria(
            @PathVariable Integer subcategoriaId) {
        List<TablaPosicionResponse> posiciones = tablaPosicionService.getTablaPosicionBySubcategoria(subcategoriaId);
        return ApiResponseStandard.success(posiciones, "Tabla de posiciones obtenida exitosamente");
    }

    @PostMapping
    public ApiResponseStandard<TablaPosicion> createOrUpdateTablaPosicion(
            @RequestBody TablaPosicionRequest request) {
        TablaPosicion posicion = tablaPosicionService.createOrUpdateTablaPosicion(request);
        return ApiResponseStandard.success(posicion, "Posición actualizada exitosamente");
    }

    @PutMapping
    public ApiResponseStandard<TablaPosicion> updateTablaPosicion(
            @RequestBody TablaPosicionRequest request) {
        TablaPosicion posicion = tablaPosicionService.createOrUpdateTablaPosicion(request);
        return ApiResponseStandard.success(posicion, "Posición actualizada exitosamente");
    }

    @DeleteMapping("/subcategoria/{subcategoriaId}/equipo/{equipoId}")
    public ApiResponseStandard<Void> deleteTablaPosicion(
            @PathVariable Integer subcategoriaId,
            @PathVariable Integer equipoId) {
        tablaPosicionService.deleteTablaPosicion(subcategoriaId, equipoId);
        return ApiResponseStandard.success("Posición eliminada exitosamente");
    }
}
