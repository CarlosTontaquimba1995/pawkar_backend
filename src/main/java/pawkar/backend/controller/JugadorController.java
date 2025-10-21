package pawkar.backend.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import pawkar.backend.exception.BadRequestException;
import pawkar.backend.request.JugadorBulkRequest;
import pawkar.backend.request.JugadorRequest;
import pawkar.backend.response.JugadorCountResponse;
import pawkar.backend.response.JugadorResponse;
import pawkar.backend.response.ApiResponseStandard;
import pawkar.backend.service.JugadorService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/jugadores")
public class JugadorController {

    private final JugadorService jugadorService;

    @Autowired
    public JugadorController(JugadorService jugadorService) {
        this.jugadorService = jugadorService;
    }

    @GetMapping
    public ApiResponseStandard<Page<JugadorResponse>> obtenerTodosLosJugadores(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String search,
            @RequestParam Map<String, String> allParams) {

        // Default sorting if not provided or invalid
        String sortField = "apellido"; // Default sort by last name
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
                Sort.by(direction, sortField));

        Page<JugadorResponse> jugadores;

        // If search parameter is provided, use it for filtering by name or last name
        if (search != null && !search.trim().isEmpty()) {
            jugadores = jugadorService.buscarJugadoresPorNombreOApellido(search, pageable);
            return ApiResponseStandard.success(jugadores,
                    String.format("Se encontraron %d jugador(es) con nombre o apellido que contiene: %s",
                            jugadores.getTotalElements(), search));
        }
        // If no search parameters, return all players with pagination
        else {
            jugadores = jugadorService.obtenerTodosLosJugadores(pageable);
            return ApiResponseStandard.success(jugadores, "Jugadores obtenidos exitosamente");
        }
    }

    // Helper method to validate sort fields to prevent SQL injection
    private boolean isValidSortField(String field) {
        // List of allowed fields to sort by
        String[] allowedFields = { "id", "nombre", "apellido", "fechaNacimiento", "documentoIdentidad" };
        for (String allowed : allowedFields) {
            if (allowed.equalsIgnoreCase(field)) {
                return true;
            }
        }
        return false;
    }

    @GetMapping("/count")
    public ApiResponseStandard<JugadorCountResponse> contarJugadoresActivos() {
        long total = jugadorService.contarJugadoresActivos();
        return ApiResponseStandard.success(
                new JugadorCountResponse(total),
                "Total de jugadores activos obtenido exitosamente");
    }

    @GetMapping("/{id}")
    public ApiResponseStandard<JugadorResponse> obtenerJugadorPorId(@PathVariable Integer id) {
        JugadorResponse jugador = jugadorService.obtenerJugadorPorId(id);
        return ApiResponseStandard.success(jugador, "Jugador obtenido exitosamente");
    }

    @PostMapping
    @ResponseStatus(org.springframework.http.HttpStatus.CREATED)
    public ApiResponseStandard<JugadorResponse> crearJugador(
            @Valid @RequestBody JugadorRequest request) {
        JugadorResponse jugador = jugadorService.crearJugador(request);
        return ApiResponseStandard.success(
                jugador,
                "Jugador creado exitosamente");
    }

    @PutMapping("/{id}")
    public ApiResponseStandard<JugadorResponse> actualizarJugador(
            @PathVariable Integer id,
            @Valid @RequestBody JugadorRequest request) {
        JugadorResponse jugador = jugadorService.actualizarJugador(id, request);
        return ApiResponseStandard.success(
                jugador,
                "Jugador actualizado exitosamente");
    }

    @DeleteMapping("/{id}")
    public ApiResponseStandard<Void> eliminarJugador(@PathVariable Integer id) {
        jugadorService.eliminarJugador(id);
        return ApiResponseStandard.success(null, "Jugador eliminado exitosamente");
    }

    @PostMapping("/bulk")
    @ResponseStatus(org.springframework.http.HttpStatus.CREATED)
    public ApiResponseStandard<List<JugadorResponse>> crearJugadoresEnLote(
            @Valid @RequestBody JugadorBulkRequest request,
            jakarta.servlet.http.HttpServletRequest servletRequest) {
        try {
            List<JugadorResponse> jugadores = jugadorService.crearJugadoresEnLote(request);
            return ApiResponseStandard.success(
                    jugadores,
                    "Jugadores creados exitosamente");
        } catch (BadRequestException e) {
            return ApiResponseStandard.error(
                    e.getMessage(),
                    servletRequest.getRequestURI(),
                    "Error en la solicitud",
                    org.springframework.http.HttpStatus.BAD_REQUEST.value()
            );
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            // Extraer el número de documento del mensaje de error
            String errorMessage = "Error al procesar la solicitud";
            if (e.getMessage() != null && e.getMessage().contains("Detail:")) {
                String detail = e.getMessage().substring(e.getMessage().indexOf("Detail:"));
                errorMessage = detail.replaceAll("^.*\\(documento_identidad\\)=\\((.*?)\\).*$", "$1");
            }
            
            return ApiResponseStandard.error(
                    "El documento de identidad " + errorMessage + " ya se encuentra registrado",
                    servletRequest.getRequestURI(),
                    "Documento duplicado",
                    org.springframework.http.HttpStatus.CONFLICT.value()
            );
        } catch (Exception e) {
            return ApiResponseStandard.error(
                    "Ocurrió un error al procesar la solicitud",
                    servletRequest.getRequestURI(),
                    "Error del servidor",
                    org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR.value()
            );
        }
    }
}
