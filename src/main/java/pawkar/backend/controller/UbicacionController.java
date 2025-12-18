package pawkar.backend.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pawkar.backend.entity.Ubicacion;
import pawkar.backend.request.BulkUbicacionRequest;
import pawkar.backend.request.UbicacionRequest;
import pawkar.backend.response.ApiResponseStandard;
import pawkar.backend.response.UbicacionResponse;
import pawkar.backend.service.UbicacionService;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/ubicaciones")
public class UbicacionController {

    private final UbicacionService ubicacionService;

    @Autowired
    public UbicacionController(UbicacionService ubicacionService) {
        this.ubicacionService = ubicacionService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ApiResponseStandard<UbicacionResponse> crearUbicacion(
            @Valid @RequestBody UbicacionRequest request) {
        try {
            Ubicacion ubicacion = ubicacionService.crearUbicacion(request);
            UbicacionResponse response = ubicacionService.toUbicacionResponse(ubicacion);
            return ApiResponseStandard.success(response, "Ubicación creada exitosamente");
        } catch (Exception e) {
            return ApiResponseStandard.error(
                e.getMessage(), 
                "/api/ubicaciones", 
                "Bad Request", 
                HttpStatus.BAD_REQUEST.value()
            );
        }
    }

    @PostMapping("/bulk")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ApiResponseStandard<List<UbicacionResponse>> crearUbicacionesBulk(
            @Valid @RequestBody BulkUbicacionRequest request) {
        try {
            List<Ubicacion> ubicaciones = ubicacionService.crearUbicacionesBulk(request);
            List<UbicacionResponse> response = ubicaciones.stream()
                    .map(ubicacionService::toUbicacionResponse)
                    .collect(Collectors.toList());
            return ApiResponseStandard.success(response, "Ubicaciones creadas exitosamente");
        } catch (Exception e) {
            return ApiResponseStandard.error(
                e.getMessage(),
                "/api/ubicaciones/bulk",
                "Bad Request",
                HttpStatus.BAD_REQUEST.value()
            );
        }
    }

    @GetMapping
    public ApiResponseStandard<List<UbicacionResponse>> listarUbicaciones() {
        try {
            List<Ubicacion> ubicaciones = ubicacionService.listarUbicaciones();
            List<UbicacionResponse> response = ubicaciones.stream()
                    .map(ubicacionService::toUbicacionResponse)
                    .collect(Collectors.toList());
            return ApiResponseStandard.success(response, "Lista de ubicaciones obtenida exitosamente");
        } catch (Exception e) {
            return ApiResponseStandard.error(
                "Error al listar ubicaciones: " + e.getMessage(),
                "/api/ubicaciones",
                "Internal Server Error",
                HttpStatus.INTERNAL_SERVER_ERROR.value()
            );
        }
    }

    @GetMapping("/{id}")
    public ApiResponseStandard<UbicacionResponse> obtenerUbicacionPorId(@PathVariable Long id) {
        try {
            Ubicacion ubicacion = ubicacionService.obtenerUbicacionPorId(id);
            UbicacionResponse response = ubicacionService.toUbicacionResponse(ubicacion);
            return ApiResponseStandard.success(response, "Ubicación obtenida exitosamente");
        } catch (Exception e) {
            return ApiResponseStandard.error(
                e.getMessage(),
                "/api/ubicaciones/" + id,
                "Not Found",
                HttpStatus.NOT_FOUND.value()
            );
        }
    }

    @GetMapping("/nemonico/{nemonico}")
    public ApiResponseStandard<UbicacionResponse> obtenerUbicacionPorNemonico(
            @PathVariable String nemonico) {
        try {
            Ubicacion ubicacion = ubicacionService.obtenerUbicacionPorNemonico(nemonico);
            UbicacionResponse response = ubicacionService.toUbicacionResponse(ubicacion);
            return ApiResponseStandard.success(response, "Ubicación obtenida exitosamente");
        } catch (Exception e) {
            return ApiResponseStandard.error(
                e.getMessage(),
                "/api/ubicaciones/nemonico/" + nemonico,
                "Not Found",
                HttpStatus.NOT_FOUND.value()
            );
        }
    }
}
