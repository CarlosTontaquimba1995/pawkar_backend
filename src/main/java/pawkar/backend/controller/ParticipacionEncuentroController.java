package pawkar.backend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pawkar.backend.request.ParticipacionEncuentroRequest;
import pawkar.backend.response.ApiResponseStandard;
import pawkar.backend.response.ParticipacionEncuentroResponse;
import pawkar.backend.service.ParticipacionEncuentroService;

import java.util.List;

@RestController
@RequestMapping("/participaciones-encuentro")
@RequiredArgsConstructor
public class ParticipacionEncuentroController {

    private final ParticipacionEncuentroService participacionEncuentroService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponseStandard<ParticipacionEncuentroResponse> createParticipacion(
            @Valid @RequestBody ParticipacionEncuentroRequest request) {
        ParticipacionEncuentroResponse response = participacionEncuentroService.createParticipacion(request);
        return ApiResponseStandard.success(response, "Participación creada exitosamente");
    }

    @GetMapping("/encuentro/{encuentroId}")
    public ApiResponseStandard<List<ParticipacionEncuentroResponse>> getParticipacionesByEncuentro(
            @PathVariable Integer encuentroId) {
        List<ParticipacionEncuentroResponse> response = participacionEncuentroService
                .getParticipacionesByEncuentro(encuentroId);
        return ApiResponseStandard.success(response, "Participaciones obtenidas exitosamente");
    }

    @GetMapping("/equipo/{equipoId}")
    public ApiResponseStandard<List<ParticipacionEncuentroResponse>> getParticipacionesByEquipo(
            @PathVariable Integer equipoId) {
        List<ParticipacionEncuentroResponse> response = participacionEncuentroService
                .getParticipacionesByEquipo(equipoId);
        return ApiResponseStandard.success(response, "Participaciones obtenidas exitosamente");
    }

    @PutMapping("/{encuentroId}/equipo/{equipoId}/goles")
    public ApiResponseStandard<ParticipacionEncuentroResponse> updateGolesPuntos(
            @PathVariable Integer encuentroId,
            @PathVariable Integer equipoId,
            @RequestParam Integer golesPuntos) {
        ParticipacionEncuentroResponse response = participacionEncuentroService
                .updateGolesPuntos(encuentroId, equipoId, golesPuntos);
        return ApiResponseStandard.success(response, "Goles/puntos actualizados exitosamente");
    }

    @DeleteMapping("/{encuentroId}/equipo/{equipoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteParticipacion(
            @PathVariable Integer encuentroId,
            @PathVariable Integer equipoId) {
        participacionEncuentroService.deleteParticipacion(encuentroId, equipoId);
    }
}
