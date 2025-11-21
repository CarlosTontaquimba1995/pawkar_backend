package pawkar.backend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pawkar.backend.request.BulkPlantillaRequest;
import pawkar.backend.request.PlantillaRequest;
import pawkar.backend.response.PlantillaResponse;
import pawkar.backend.response.ApiResponseStandard;
import pawkar.backend.service.PlantillaService;

import java.util.List;

@RestController
@RequestMapping("/plantillas")
@RequiredArgsConstructor
public class PlantillaController {

    private final PlantillaService plantillaService;

    @PostMapping("/bulk")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponseStandard<List<PlantillaResponse>> crearPlantillasBulk(
            @Valid @RequestBody BulkPlantillaRequest request) {
        List<PlantillaResponse> responses = plantillaService.crearPlantillasBulk(request);
        return ApiResponseStandard.success(responses, "Plantillas creadas exitosamente");
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponseStandard<PlantillaResponse> crearPlantilla(
            @Valid @RequestBody PlantillaRequest request) {
        PlantillaResponse response = plantillaService.crearPlantilla(request);
        return ApiResponseStandard.success(response, "Plantilla creada exitosamente");
    }

    @GetMapping
    public ApiResponseStandard<List<PlantillaResponse>> obtenerTodasLasPlantillas() {
        List<PlantillaResponse> plantillas = plantillaService.obtenerTodasLasPlantillas();
        return ApiResponseStandard.success(
                plantillas,
                "Plantillas obtenidas exitosamente");
    }

    @GetMapping("/{equipoId}/{jugadorId}")
    public ApiResponseStandard<PlantillaResponse> obtenerPlantilla(
            @PathVariable Integer equipoId,
            @PathVariable Integer jugadorId) {
        PlantillaResponse response = plantillaService.obtenerPlantilla(equipoId, jugadorId);
        return ApiResponseStandard.success(response, "Plantilla obtenida exitosamente");
    }

    @GetMapping("/equipo/{equipoId}")
    public ApiResponseStandard<List<PlantillaResponse>> obtenerPlantillaPorEquipo(
            @PathVariable Integer equipoId) {
        List<PlantillaResponse> plantillas = plantillaService.obtenerPlantillaPorEquipo(equipoId);
        return ApiResponseStandard.success(plantillas, "Plantillas obtenidas exitosamente");
    }
    
    @GetMapping("/subcategoria/{subcategoriaId}")
    public ApiResponseStandard<List<PlantillaResponse>> obtenerPlantillasPorSubcategoria(
            @PathVariable Integer subcategoriaId) {
        List<PlantillaResponse> plantillas = plantillaService.obtenerPlantillasPorSubcategoria(subcategoriaId);
        return ApiResponseStandard.success(plantillas, "Plantillas obtenidas exitosamente para la subcategoría");
    }

    @DeleteMapping("/{equipoId}/{jugadorId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminarPlantilla(
            @PathVariable Integer equipoId,
            @PathVariable Integer jugadorId) {
        plantillaService.eliminarPlantilla(equipoId, jugadorId);
    }
}
