package pawkar.backend.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pawkar.backend.dto.BulkEquipoRequest;
import pawkar.backend.dto.EquipoRequest;
import pawkar.backend.dto.EquipoResponse;
import pawkar.backend.response.ApiResponseStandard;
import pawkar.backend.service.EquipoService;

import java.util.List;

@RestController
@RequestMapping("/equipos")
public class EquipoController {

    @Autowired
    private EquipoService equipoService;

    @GetMapping("/serie/{serieId}")
    public ApiResponseStandard<List<EquipoResponse>> obtenerEquiposPorSerie(
            @PathVariable Integer serieId) {
        List<EquipoResponse> equipos = equipoService.obtenerEquiposPorSerie(serieId);
        return ApiResponseStandard.success(equipos, "Equipos obtenidos exitosamente");
    }

    @GetMapping("/subcategoria/{subcategoriaId}")
    public ApiResponseStandard<List<EquipoResponse>> obtenerEquiposPorSubcategoria(
            @PathVariable Integer subcategoriaId) {
        List<EquipoResponse> equipos = equipoService.obtenerEquiposPorSubcategoria(subcategoriaId);
        return ApiResponseStandard.success(equipos, "Equipos obtenidos exitosamente");
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
        return ApiResponseStandard.success("Equipo eliminado exitosamente");
    }
}
