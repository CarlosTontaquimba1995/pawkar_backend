package pawkar.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pawkar.backend.entity.TablaPosicion;
import pawkar.backend.request.TablaPosicionRequest;
import pawkar.backend.request.TablaPosicionSearchRequest;
import pawkar.backend.response.ApiResponseStandard;
import pawkar.backend.response.TablaPosicionResponse;
import pawkar.backend.service.TablaPosicionService;

import java.util.List;

class ActualizarTablaRequest {
    private Integer subcategoriaId;
    private Integer equipoLocalId;
    private Integer equipoVisitanteId;
    private Integer golesLocal;
    private Integer golesVisitante;
    private String estadoPartido;

    // Getters y Setters
    public Integer getSubcategoriaId() { return subcategoriaId; }
    public void setSubcategoriaId(Integer subcategoriaId) { this.subcategoriaId = subcategoriaId; }
    public Integer getEquipoLocalId() { return equipoLocalId; }
    public void setEquipoLocalId(Integer equipoLocalId) { this.equipoLocalId = equipoLocalId; }
    public Integer getEquipoVisitanteId() { return equipoVisitanteId; }
    public void setEquipoVisitanteId(Integer equipoVisitanteId) { this.equipoVisitanteId = equipoVisitanteId; }
    public Integer getGolesLocal() { return golesLocal; }
    public void setGolesLocal(Integer golesLocal) { this.golesLocal = golesLocal; }
    public Integer getGolesVisitante() { return golesVisitante; }
    public void setGolesVisitante(Integer golesVisitante) { this.golesVisitante = golesVisitante; }
    public String getEstadoPartido() { return estadoPartido; }
    public void setEstadoPartido(String estadoPartido) { this.estadoPartido = estadoPartido; }
}

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

    @GetMapping("/search")
    public ApiResponseStandard<List<TablaPosicionResponse>> searchTablaPosicion(
            @RequestParam(required = false) Integer subcategoriaId,
            @RequestParam(required = false) Integer categoriaId,
            @RequestParam(required = false) Integer equipoId,
            @RequestParam(required = false) Integer serieId,
            @RequestParam(required = false) String nombreEquipo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "puntos,desc") String sort) {
        
        TablaPosicionSearchRequest searchRequest = new TablaPosicionSearchRequest();
        searchRequest.setSubcategoriaId(subcategoriaId);
        searchRequest.setCategoriaId(categoriaId);
        searchRequest.setEquipoId(equipoId);
        searchRequest.setSerieId(serieId);
        searchRequest.setNombreEquipo(nombreEquipo);
        searchRequest.setPage(page);
        searchRequest.setSize(size);
        searchRequest.setSort(sort);
        
        List<TablaPosicionResponse> resultados = tablaPosicionService.searchTablaPosicion(searchRequest);
        return ApiResponseStandard.success(resultados, "Búsqueda de tabla de posiciones exitosa");
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

    @PostMapping("/actualizar-desde-partido")
    public ApiResponseStandard<Void> actualizarDesdePartido(
            @RequestBody ActualizarTablaRequest request) {
        
        // Validar que los campos requeridos no sean nulos
        if (request.getSubcategoriaId() == null || 
            request.getEquipoLocalId() == null || 
            request.getEquipoVisitanteId() == null ||
            request.getEstadoPartido() == null) {
            throw new IllegalArgumentException("Todos los campos son requeridos");
        }

        // Llamar al servicio para actualizar la tabla de posiciones
        tablaPosicionService.actualizarTablaPosicionDesdePartido(
            request.getSubcategoriaId(),
            request.getEquipoLocalId(),
            request.getEquipoVisitanteId(),
            request.getGolesLocal() != null ? request.getGolesLocal() : 0,
            request.getGolesVisitante() != null ? request.getGolesVisitante() : 0,
            request.getEstadoPartido()
        );

        return ApiResponseStandard.success("Tabla de posiciones actualizada exitosamente");
    }
}
