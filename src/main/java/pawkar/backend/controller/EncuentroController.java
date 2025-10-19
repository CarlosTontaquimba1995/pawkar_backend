package pawkar.backend.controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pawkar.backend.request.EncuentroRequest;
import pawkar.backend.request.EncuentroResponse;
import pawkar.backend.request.EncuentroSearchRequest;
import pawkar.backend.service.EncuentroService;

import java.util.List;

@RestController
@RequestMapping("/encuentros")
public class EncuentroController {

    private final EncuentroService encuentroService;

    public EncuentroController(EncuentroService encuentroService) {
        this.encuentroService = encuentroService;
    }

    @PostMapping
    public ResponseEntity<EncuentroResponse> createEncuentro(@Valid @RequestBody EncuentroRequest request) {
        EncuentroResponse response = encuentroService.createEncuentro(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<EncuentroResponse>> getAllEncuentros() {
        List<EncuentroResponse> encuentros = encuentroService.getAllEncuentros();
        return ResponseEntity.ok(encuentros);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EncuentroResponse> getEncuentroById(@PathVariable Integer id) {
        EncuentroResponse response = encuentroService.getEncuentroById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/subcategoria/{subcategoriaId}")
    public ResponseEntity<List<EncuentroResponse>> getEncuentrosBySubcategoria(
            @PathVariable Integer subcategoriaId) {
        List<EncuentroResponse> encuentros = encuentroService.getEncuentrosBySubcategoria(subcategoriaId);
        return ResponseEntity.ok(encuentros);
    }

    @PostMapping("/search")
    public ResponseEntity<Page<EncuentroResponse>> searchEncuentros(
            @Valid @RequestBody EncuentroSearchRequest searchRequest) {
        Page<EncuentroResponse> response = encuentroService.searchEncuentros(searchRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<EncuentroResponse>> searchEncuentrosWithQueryParams(
            @RequestParam(required = false) String titulo,
            @RequestParam(required = false) Integer subcategoriaId,
            @RequestParam(required = false) String fechaInicio,
            @RequestParam(required = false) String fechaFin,
            @RequestParam(required = false) String estadioLugar,
            @RequestParam(required = false) String estado,
            @PageableDefault(sort = "fechaHora", direction = org.springframework.data.domain.Sort.Direction.ASC) Pageable pageable) {

        EncuentroSearchRequest searchRequest = new EncuentroSearchRequest();
        searchRequest.setSubcategoriaId(subcategoriaId);
        // Convertir fechas de String a LocalDate si es necesario
        // searchRequest.setFechaInicio(...);
        // searchRequest.setFechaFin(...);
        searchRequest.setEstadioLugar(estadioLugar);
        searchRequest.setEstado(estado);

        // Configurar paginación
        searchRequest.setPage(pageable.getPageNumber());
        searchRequest.setSize(pageable.getPageSize());

        if (pageable.getSort().isSorted()) {
            pageable.getSort().stream().findFirst().ifPresent(order -> {
                searchRequest.setSortBy(order.getProperty());
                searchRequest.setSortDirection(order.getDirection());
            });
        }

        Page<EncuentroResponse> response = encuentroService.searchEncuentros(searchRequest);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EncuentroResponse> updateEncuentro(
            @PathVariable Integer id,
            @Valid @RequestBody EncuentroRequest request) {
        EncuentroResponse response = encuentroService.updateEncuentro(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEncuentro(@PathVariable Integer id) {
        encuentroService.deleteEncuentro(id);
        return ResponseEntity.noContent().build();
    }
}
