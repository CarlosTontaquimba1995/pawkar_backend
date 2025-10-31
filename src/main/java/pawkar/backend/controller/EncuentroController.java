package pawkar.backend.controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.web.bind.annotation.*;
import pawkar.backend.request.EncuentroRequest;
import pawkar.backend.request.EncuentroResponse;
import pawkar.backend.request.EncuentroSearchRequest;
import pawkar.backend.response.ApiResponseStandard;
import pawkar.backend.service.EncuentroService;


@RestController
@RequestMapping("/encuentros")
public class EncuentroController {

    private final EncuentroService encuentroService;

    public EncuentroController(EncuentroService encuentroService) {
        this.encuentroService = encuentroService;
    }

    @PostMapping
    public ApiResponseStandard<EncuentroResponse> createEncuentro(
            @Validated(EncuentroRequest.OnCreate.class) @RequestBody EncuentroRequest request) {
        EncuentroResponse response = encuentroService.createEncuentro(request);
        return ApiResponseStandard.success(response, "Encuentro creado exitosamente");
    }

    /**
     * Endpoint para guardar múltiples encuentros en una sola operación
     * 
     * @param requests Lista de objetos EncuentroRequest a guardar
     * @return Lista de EncuentroResponse con los encuentros guardados
     */
    @PostMapping("/bulk")
    public ApiResponseStandard<List<EncuentroResponse>> createEncuentrosBulk(
            @Validated(EncuentroRequest.OnCreate.class) @RequestBody List<EncuentroRequest> requests) {
        List<EncuentroResponse> responses = encuentroService.saveAllEncuentros(requests);
        return ApiResponseStandard.success(responses, "Encuentros creados exitosamente");
    }

    @GetMapping
    public ApiResponseStandard<List<EncuentroResponse>> getAllEncuentros() {
        List<EncuentroResponse> encuentros = encuentroService.getAllEncuentros();
        return ApiResponseStandard.success(encuentros, "Lista de encuentros obtenida exitosamente");
    }

    @GetMapping("/{id}")
    public ApiResponseStandard<EncuentroResponse> getEncuentroById(@PathVariable Integer id) {
        EncuentroResponse response = encuentroService.getEncuentroById(id);
        return ApiResponseStandard.success(response, "Encuentro obtenido exitosamente");
    }

    @GetMapping("/subcategoria/{subcategoriaId}")
    public ApiResponseStandard<List<EncuentroResponse>> getEncuentrosBySubcategoria(
            @PathVariable Integer subcategoriaId) {
        List<EncuentroResponse> encuentros = encuentroService.getEncuentrosBySubcategoria(subcategoriaId);
        return ApiResponseStandard.success(encuentros, "Encuentros por subcategoría obtenidos exitosamente");
    }

    @PostMapping("/search")
    public ApiResponseStandard<Page<EncuentroResponse>> searchEncuentros(
            @Valid @RequestBody EncuentroSearchRequest searchRequest) {
        Page<EncuentroResponse> response = encuentroService.searchEncuentros(searchRequest);
        return ApiResponseStandard.success(response, "Búsqueda de encuentros completada exitosamente");
    }

    @GetMapping("/search")
    public ApiResponseStandard<Page<EncuentroResponse>> searchEncuentros(
            @RequestParam(required = false) String titulo,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin,
            @RequestParam(required = false) Integer subcategoriaId,
            @RequestParam(required = false) Integer equipoId,
            @RequestParam(required = false) String estadioLugar,
            @PageableDefault(size = 10) Pageable pageable) {

        EncuentroSearchRequest searchRequest = new EncuentroSearchRequest();
        searchRequest.setTitulo(titulo);
        searchRequest.setFechaInicio(fechaInicio != null ? fechaInicio.toLocalDate() : null);
        searchRequest.setFechaFin(fechaFin != null ? fechaFin.toLocalDate() : null);
        searchRequest.setSubcategoriaId(subcategoriaId);
        searchRequest.setEquipoId(equipoId);
        searchRequest.setEstadioLugar(estadioLugar);

        Page<EncuentroResponse> page = encuentroService.searchEncuentros(
                searchRequest.getSubcategoriaId(),
                searchRequest.getFechaInicio() != null ? searchRequest.getFechaInicio().atStartOfDay() : null,
                searchRequest.getFechaFin() != null ? searchRequest.getFechaFin().atTime(23, 59, 59) : null,
                searchRequest.getEstadioLugar(),
                searchRequest.getEstado(),
                searchRequest.getEquipoId(),
                pageable);
        return ApiResponseStandard.success(page, "Búsqueda de encuentros exitosa");
    }

    /**
     * Obtiene todos los encuentros en los que participa un equipo específico
     * 
     * @param equipoId ID del equipo
     * @return Lista de encuentros en los que participa el equipo
     */
    @GetMapping("/equipo/{equipoId}")
    public ApiResponseStandard<Page<EncuentroResponse>> getEncuentrosByEquipoId(
            @PathVariable Integer equipoId,
            @PageableDefault(size = 10) Pageable pageable) {
        Page<EncuentroResponse> page = encuentroService.searchEncuentros(
                null, // subcategoriaId
                null, // fechaInicio
                null, // fechaFin
                null, // estadioLugar
                null, // estado
                equipoId,
                pageable);
        return ApiResponseStandard.success(page, "Encuentros del equipo obtenidos exitosamente");
    }

    @GetMapping("/search/params")
    public ApiResponseStandard<Page<EncuentroResponse>> searchEncuentrosWithQueryParams(
            @RequestParam(required = false) Integer subcategoriaId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
                    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin,
            @RequestParam(required = false) String estadioLugar,
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) Integer equipoId,
            @PageableDefault(size = 10) Pageable pageable) {

        try {
            Page<EncuentroResponse> response = encuentroService.searchEncuentros(
                    subcategoriaId,
                    fechaInicio,
                    fechaFin,
                    estadioLugar,
                    estado,
                    equipoId,
                    pageable);
            return ApiResponseStandard.success(response, "Búsqueda de encuentros completada exitosamente");
        } catch (Exception e) {
            // Fallback a resultados sin paginación si hay un error con la paginación
            List<EncuentroResponse> response = encuentroService.searchEncuentrosWithoutPagination(
                    subcategoriaId,
                    fechaInicio,
                    fechaFin,
                    estadioLugar,
                    estado,
                    equipoId);
            return ApiResponseStandard.success(new PageImpl<>(response),
                    "Búsqueda de encuentros completada (sin paginación)");
        }
    }

    @PutMapping("/{id}")
    public ApiResponseStandard<EncuentroResponse> updateEncuentro(
            @PathVariable Integer id,
            @Valid @RequestBody EncuentroRequest request) {
        EncuentroResponse response = encuentroService.updateEncuentro(id, request);
        return ApiResponseStandard.success(response, "Encuentro actualizado exitosamente");
    }

    @DeleteMapping("/{id}")
    public ApiResponseStandard<Void> deleteEncuentro(@PathVariable Integer id) {
        encuentroService.deleteEncuentro(id);
        return ApiResponseStandard.success(null, "Encuentro eliminado exitosamente");
    }
}
