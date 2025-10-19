package pawkar.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pawkar.backend.dto.SancionRequest;
import pawkar.backend.dto.SancionResponse;
import pawkar.backend.service.SancionService;

import java.util.List;

@RestController
@RequestMapping("/sanciones")
@CrossOrigin(origins = "*")
public class SancionController {

    @Autowired
    private SancionService sancionService;

    @GetMapping
    public ResponseEntity<List<SancionResponse>> getAllSanciones() {
        return ResponseEntity.ok(sancionService.getAllSanciones());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SancionResponse> getSancionById(@PathVariable Long id) {
        return ResponseEntity.ok(sancionService.getSancionById(id));
    }

    @PostMapping
    public ResponseEntity<SancionResponse> createSancion(@RequestBody SancionRequest sancionRequest) {
        return ResponseEntity.ok(sancionService.createSancion(sancionRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SancionResponse> updateSancion(
            @PathVariable Long id,
            @RequestBody SancionRequest sancionRequest) {
        return ResponseEntity.ok(sancionService.updateSancion(id, sancionRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSancion(@PathVariable Long id) {
        sancionService.deleteSancion(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/jugador/{jugadorId}")
    public ResponseEntity<List<SancionResponse>> getSancionesByJugadorId(@PathVariable Long jugadorId) {
        return ResponseEntity.ok(sancionService.getSancionesByJugadorId(jugadorId));
    }

    @GetMapping("/encuentro/{encuentroId}")
    public ResponseEntity<List<SancionResponse>> getSancionesByEncuentroId(@PathVariable Integer encuentroId) {
        return ResponseEntity.ok(sancionService.getSancionesByEncuentroId(encuentroId));
    }

    @GetMapping("/tipo/{tipoSancion}")
    public ResponseEntity<List<SancionResponse>> getSancionesByTipo(@PathVariable String tipoSancion) {
        return ResponseEntity.ok(sancionService.getSancionesByTipo(tipoSancion));
    }
}
