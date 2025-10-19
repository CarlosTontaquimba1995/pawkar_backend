package pawkar.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pawkar.backend.dto.SancionRequest;
import pawkar.backend.dto.SancionResponse;
import pawkar.backend.response.ApiResponseStandard;
import pawkar.backend.service.SancionService;

import java.util.List;

@RestController
@RequestMapping("/sanciones")
@CrossOrigin(origins = "*")
public class SancionController {

    @Autowired
    private SancionService sancionService;

    @GetMapping
    public ApiResponseStandard<List<SancionResponse>> getAllSanciones() {
        return ApiResponseStandard.success(sancionService.getAllSanciones(), "Sanciones obtenidas exitosamente");
    }

    @GetMapping("/{id}")
    public ApiResponseStandard<SancionResponse> getSancionById(@PathVariable Long id) {
        return ApiResponseStandard.success(sancionService.getSancionById(id), "Sancion obtenida exitosamente");
    }

    @PostMapping
    public ApiResponseStandard<SancionResponse> createSancion(@RequestBody SancionRequest sancionRequest) {
        return ApiResponseStandard.success(sancionService.createSancion(sancionRequest), "Sanción creada exitosamente");
    }

    @PutMapping("/{id}")
    public ApiResponseStandard<SancionResponse> updateSancion(
            @PathVariable Long id,
            @RequestBody SancionRequest sancionRequest) {
        return ApiResponseStandard.success(sancionService.updateSancion(id, sancionRequest),
                "Sanción actualizada exitosamente");
    }

    @DeleteMapping("/{id}")
    public ApiResponseStandard<Void> deleteSancion(@PathVariable Long id) {
        sancionService.deleteSancion(id);
        return ApiResponseStandard.success("Sanción eliminada exitosamente");
    }

    @GetMapping("/jugador/{jugadorId}")
    public ApiResponseStandard<List<SancionResponse>> getSancionesByJugadorId(@PathVariable Long jugadorId) {
        return ApiResponseStandard.success(sancionService.getSancionesByJugadorId(jugadorId),
                "Sanciones por jugador obtenidas exitosamente");
    }

    @GetMapping("/encuentro/{encuentroId}")
    public ApiResponseStandard<List<SancionResponse>> getSancionesByEncuentroId(@PathVariable Integer encuentroId) {
        return ApiResponseStandard.success(sancionService.getSancionesByEncuentroId(encuentroId),
                "Sanciones por encuentro obtenidas exitosamente");
    }

    @GetMapping("/tipo/{tipoSancion}")
    public ApiResponseStandard<List<SancionResponse>> getSancionesByTipo(@PathVariable String tipoSancion) {
        return ApiResponseStandard.success(sancionService.getSancionesByTipo(tipoSancion),
                "Sanciones por tipo obtenidas exitosamente");
    }
}
