package pawkar.backend.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import pawkar.backend.request.BulkEquipoRequest;
import pawkar.backend.request.EquipoRequest;
import pawkar.backend.response.EquipoResponse;
import pawkar.backend.response.ApiResponseStandard;
import pawkar.backend.service.EquipoService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/equipos")
public class EquipoController {

    @Autowired
    private EquipoService equipoService;
    
    @GetMapping
    public ApiResponseStandard<Page<EquipoResponse>> obtenerTodosLosEquipos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String search,
            @RequestParam Map<String, String> allParams) {
        
        // Default sorting if not provided or invalid
        String sortField = "nombre";
        Sort.Direction direction = Sort.Direction.ASC;

        // Check if sort parameter is actually a search term (contains space)
        if (sort != null && !sort.trim().isEmpty()) {
            if (sort.contains(" ")) {
                // If sort contains spaces, treat it as a search term
                search = sort;
                sort = null; // Reset sort to use default
            } else {
                // Otherwise, process it as a sort parameter
                String[] sortParams = sort.split(",");
                if (sortParams.length > 0) {
                    // Only use the first part before comma as the sort field
                    // and validate it's a valid field to prevent SQL injection
                    String potentialField = sortParams[0].trim();
                    if (isValidSortField(potentialField)) {
                        sortField = potentialField;
                    }

                    // Check for sort direction
                    if (sortParams.length > 1) {
                        String dir = sortParams[1].trim();
                        if ("desc".equalsIgnoreCase(dir)) {
                            direction = Sort.Direction.DESC;
                        }
                    }
                }
            }
        }

        Pageable pageable = PageRequest.of(
            page, 
            size, 
            Sort.by(direction, sortField)
        );
        
        Page<EquipoResponse> equipos;

        // If search parameter is provided, use it for filtering
        if (search != null && !search.trim().isEmpty()) {
            equipos = equipoService.buscarEquiposPorNombre(search, pageable);
            return ApiResponseStandard.success(equipos,
                    String.format("Se encontraron %d equipo(s) con el nombre que contiene: %s",
                            equipos.getTotalElements(), search));
        }
        // If nombre parameter is provided (for backward compatibility)
        else if (nombre != null && !nombre.trim().isEmpty()) {
            equipos = equipoService.buscarEquiposPorNombre(nombre, pageable);
            return ApiResponseStandard.success(equipos,
                    String.format("Se encontraron %d equipo(s) con el nombre que contiene: %s",
                            equipos.getTotalElements(), nombre));
        }
        // If no search parameters, return all teams
        else {
            equipos = equipoService.obtenerTodosLosEquipos(pageable);
            return ApiResponseStandard.success(equipos, "Equipos obtenidos exitosamente");
        }
    }

    // Helper method to validate sort fields to prevent SQL injection
    private boolean isValidSortField(String field) {
        // List of allowed fields to sort by
        String[] allowedFields = { "nombre", "fechaCreacion", "equipoId" };
        for (String allowed : allowedFields) {
            if (allowed.equalsIgnoreCase(field)) {
                return true;
            }
        }
        return false;
    }

    @GetMapping("/serie/{serieId}")
    public ApiResponseStandard<List<EquipoResponse>> obtenerEquiposPorSerie(
            @PathVariable Integer serieId) {
        List<EquipoResponse> equipos = equipoService.obtenerEquiposPorSerie(serieId);
        return ApiResponseStandard.success(equipos, "Equipos obtenidos exitosamente");
    }

    @GetMapping("/subcategoria/{subcategoriaId}")
    public ApiResponseStandard<List<EquipoResponse>> obtenerEquiposPorSubcategoria(
            @PathVariable Integer subcategoriaId,
            @RequestParam(required = false) Integer serieId) {
        List<EquipoResponse> equipos;
        if (serieId != null) {
            equipos = equipoService.obtenerEquiposPorSubcategoriaYSerie(subcategoriaId, serieId);
        } else {
            equipos = equipoService.obtenerEquiposPorSubcategoria(subcategoriaId);
        }
        String message = "Equipos obtenidos exitosamente";
        if (serieId != null) {
            message += String.format(" (filtrados por subcategoría: %d y serie: %d)", subcategoriaId, serieId);
        } else {
            message += String.format(" (filtrados por subcategoría: %d)", subcategoriaId);
        }
        return ApiResponseStandard.success(equipos, message);
    }

    @GetMapping("/{id}")
    public ApiResponseStandard<EquipoResponse> obtenerEquipoPorId(
            @PathVariable Integer id) {
        EquipoResponse equipo = equipoService.obtenerEquipoPorId(id);
        return ApiResponseStandard.success(equipo, "Equipo obtenido exitosamente");
    }

    @PostMapping
    @ResponseStatus(org.springframework.http.HttpStatus.CREATED)
    public ApiResponseStandard<EquipoResponse> crearEquipo(
            @Valid @RequestBody EquipoRequest request) {
        EquipoResponse equipo = equipoService.crearEquipo(request);
        return ApiResponseStandard.success(equipo, "Equipo creado exitosamente");
    }
    
    @PostMapping("/bulk")
    @ResponseStatus(org.springframework.http.HttpStatus.CREATED)
    public ApiResponseStandard<List<EquipoResponse>> crearEquiposEnLote(
            @Valid @RequestBody BulkEquipoRequest request) {
        List<EquipoResponse> equipos = equipoService.crearEquiposEnLote(request);
        return ApiResponseStandard.success(equipos, "Equipos creados exitosamente");
    }

    @PutMapping("/{id}")
    public ApiResponseStandard<EquipoResponse> actualizarEquipo(
            @PathVariable Integer id,
            @Valid @RequestBody EquipoRequest request) {
        EquipoResponse equipo = equipoService.actualizarEquipo(id, request);
        return ApiResponseStandard.success(equipo, "Equipo actualizado exitosamente");
    }

    @DeleteMapping("/{id}")
    public ApiResponseStandard<Void> eliminarEquipo(
            @PathVariable Integer id) {
        equipoService.eliminarEquipo(id);
        return ApiResponseStandard.success(null, "Equipo eliminado exitosamente");
    }

    @GetMapping("/existen")
    @ResponseBody
    public boolean existenEquiposRegistrados() {
        return equipoService.existenEquiposRegistrados();
    }
}
