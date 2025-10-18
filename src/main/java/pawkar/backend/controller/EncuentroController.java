package pawkar.backend.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pawkar.backend.dto.EncuentroRequest;
import pawkar.backend.dto.EncuentroResponse;
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
    public ResponseEntity<EncuentroResponse> getEncuentroById(@PathVariable Long id) {
        EncuentroResponse response = encuentroService.getEncuentroById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/subcategoria/{subcategoriaId}")
    public ResponseEntity<List<EncuentroResponse>> getEncuentrosBySubcategoria(
            @PathVariable Long subcategoriaId) {
        List<EncuentroResponse> encuentros = encuentroService.getEncuentrosBySubcategoria(subcategoriaId);
        return ResponseEntity.ok(encuentros);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EncuentroResponse> updateEncuentro(
            @PathVariable Long id,
            @Valid @RequestBody EncuentroRequest request) {
        EncuentroResponse response = encuentroService.updateEncuentro(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEncuentro(@PathVariable Long id) {
        encuentroService.deleteEncuentro(id);
        return ResponseEntity.noContent().build();
    }
}
