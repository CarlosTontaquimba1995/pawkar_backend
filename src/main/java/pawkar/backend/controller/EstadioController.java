package pawkar.backend.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pawkar.backend.request.BulkEstadioRequest;
import pawkar.backend.request.EstadioRequest;
import pawkar.backend.response.ApiResponseStandard;
import pawkar.backend.response.EstadioResponse;
import pawkar.backend.service.EstadioService;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/estadios")
public class EstadioController {

    @Autowired
    private EstadioService estadioService;

    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ApiResponseStandard<List<EstadioResponse>> getAllEstadios() {
        List<EstadioResponse> estadios = estadioService.getAllEstadios();
        return ApiResponseStandard.success(
                estadios,
                "Estadios obtenidos exitosamente");
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ApiResponseStandard<EstadioResponse> getEstadioById(@PathVariable Long id) {
        EstadioResponse estadio = estadioService.getEstadioById(id);
        return ApiResponseStandard.success(
                estadio,
                "Estadio obtenido exitosamente");
    }

    @PostMapping
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ApiResponseStandard<EstadioResponse> createEstadio(
            @Valid @RequestBody EstadioRequest estadioRequest) {
        EstadioResponse estadio = estadioService.createEstadio(estadioRequest);
        return ApiResponseStandard.success(
                estadio,
                "Estadio creado exitosamente");
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ApiResponseStandard<EstadioResponse> updateEstadio(
            @PathVariable Long id,
            @Valid @RequestBody EstadioRequest estadioRequest) {
        EstadioResponse estadio = estadioService.updateEstadio(id, estadioRequest);
        return ApiResponseStandard.success(
                estadio,
                "Estadio actualizado exitosamente");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponseStandard<Void> deleteEstadio(@PathVariable Long id) {
        estadioService.deleteEstadio(id);
        return ApiResponseStandard.success(
                null,
                "Estadio eliminado exitosamente");
    }

    @PostMapping("/bulk")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ApiResponseStandard<List<EstadioResponse>> crearEstadiosEnLote(
            @Valid @RequestBody BulkEstadioRequest request) {
        List<EstadioResponse> estadios = estadioService.crearEstadiosEnLote(request);
        return ApiResponseStandard.success(
                estadios,
                "Estadios creados exitosamente");
    }
}
