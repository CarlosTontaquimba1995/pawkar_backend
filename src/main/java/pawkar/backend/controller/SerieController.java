package pawkar.backend.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pawkar.backend.request.BulkSerieRequest;
import pawkar.backend.response.ApiResponseStandard;
import pawkar.backend.request.SerieRequest;
import pawkar.backend.response.SerieResponse;
import pawkar.backend.service.SerieService;

import java.util.List;

@RestController
@RequestMapping("/series")
public class SerieController {

    private final SerieService serieService;

    @Autowired
    public SerieController(SerieService serieService) {
        this.serieService = serieService;
    }

    @PostMapping
    @ResponseStatus(org.springframework.http.HttpStatus.CREATED)
    public ApiResponseStandard<SerieResponse> crearSerie(
            @Valid @RequestBody SerieRequest request) {
        try {
            SerieResponse serie = serieService.crearSerie(request);
            return ApiResponseStandard.success(
                    serie,
                    "Serie creada exitosamente");
        } catch (Exception e) {
            return ApiResponseStandard.error(
                    e.getMessage(),
                    "/api/series",
                    "Error al crear la serie",
                    400);
        }
    }

    @PostMapping("/bulk")
    @ResponseStatus(org.springframework.http.HttpStatus.CREATED)
    public ApiResponseStandard<List<SerieResponse>> crearSeriesEnLote(
            @Valid @RequestBody BulkSerieRequest request) {
        try {
            List<SerieResponse> series = serieService.crearSeriesEnLote(request);
            return ApiResponseStandard.success(
                    series,
                    "Series creadas exitosamente");
        } catch (Exception e) {
            return ApiResponseStandard.error(
                    e.getMessage(),
                    "/api/series/bulk",
                    "Error al crear las series",
                    400);
        }
    }

    @GetMapping("/subcategoria/{subcategoriaId}")
    public ApiResponseStandard<List<SerieResponse>> obtenerSeriesPorSubcategoria(
            @PathVariable Integer subcategoriaId) {
        try {
            List<SerieResponse> series = serieService.obtenerSeriesPorSubcategoria(subcategoriaId);
            return ApiResponseStandard.success(
                    series,
                    "Series obtenidas exitosamente");
        } catch (Exception e) {
            return ApiResponseStandard.error(
                    e.getMessage(),
                    "/api/series/subcategoria/" + subcategoriaId,
                    "Error al obtener las series",
                    400);
        }
    }

    @GetMapping("/{id}")
    public ApiResponseStandard<SerieResponse> obtenerSeriePorId(
            @PathVariable Integer id) {
        try {
            SerieResponse serie = serieService.obtenerSeriePorId(id);
            return ApiResponseStandard.success(
                    serie,
                    "Serie obtenida exitosamente");
        } catch (Exception e) {
            return ApiResponseStandard.error(
                    e.getMessage(),
                    "/api/series/" + id,
                    "Error al obtener la serie",
                    404);
        }
    }

    @PutMapping("/{id}")
    public ApiResponseStandard<SerieResponse> actualizarSerie(
            @PathVariable Integer id,
            @Valid @RequestBody SerieRequest request) {
        try {
            SerieResponse serieActualizada = serieService.actualizarSerie(id, request);
            return ApiResponseStandard.success(
                    serieActualizada,
                    "Serie actualizada exitosamente");
        } catch (Exception e) {
            return ApiResponseStandard.error(
                    e.getMessage(),
                    "/api/series/" + id,
                    "Error al actualizar la serie",
                    400);
        }
    }

    @DeleteMapping("/{id}")
    public ApiResponseStandard<Void> eliminarSerie(@PathVariable Integer id) {
        try {
            serieService.eliminarSerie(id);
            return ApiResponseStandard.success(
                    null,
                    "Serie eliminada exitosamente");
        } catch (Exception e) {
            return ApiResponseStandard.error(
                    e.getMessage(),
                    "/api/series/" + id,
                    "Error al eliminar la serie",
                    404);
        }
    }
}
