package pawkar.backend.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pawkar.backend.request.JugadorRequest;
import pawkar.backend.response.JugadorResponse;
import pawkar.backend.request.JugadorBulkRequest;
import pawkar.backend.response.ApiResponseStandard;
import pawkar.backend.service.JugadorService;

import java.util.List;

@RestController
@RequestMapping("/jugadores")
public class JugadorController {

    private final JugadorService jugadorService;

    @Autowired
    public JugadorController(JugadorService jugadorService) {
        this.jugadorService = jugadorService;
    }

    @GetMapping
    public ApiResponseStandard<List<JugadorResponse>> obtenerTodosLosJugadores() {
        List<JugadorResponse> jugadores = jugadorService.obtenerTodosLosJugadores();
        return ApiResponseStandard.success(jugadores, "Jugadores obtenidos exitosamente");
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
    @ResponseStatus(org.springframework.http.HttpStatus.NO_CONTENT)
    public void eliminarJugador(@PathVariable Integer id) {
        jugadorService.eliminarJugador(id);
    }

    @PostMapping("/bulk")
    @ResponseStatus(org.springframework.http.HttpStatus.CREATED)
    public ApiResponseStandard<List<JugadorResponse>> crearJugadoresEnLote(
            @Valid @RequestBody JugadorBulkRequest request) {
        List<JugadorResponse> jugadores = jugadorService.crearJugadoresEnLote(request);
        return ApiResponseStandard.success(
                jugadores,
                "Jugadores creados exitosamente");
    }
}
