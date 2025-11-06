package pawkar.backend.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pawkar.backend.request.BulkCategoriaRequest;
import pawkar.backend.request.CategoriaRequest;
import pawkar.backend.response.ApiResponseStandard;
import pawkar.backend.response.CategoriaResponse;
import pawkar.backend.service.CategoriaService;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ApiResponseStandard<List<CategoriaResponse>> getAllCategorias() {
        List<CategoriaResponse> categorias = categoriaService.getAllCategorias();
        return ApiResponseStandard.success(
                categorias,
                "Categorías obtenidas exitosamente");
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ApiResponseStandard<CategoriaResponse> getCategoriaById(@PathVariable Long id) {
        CategoriaResponse categoria = categoriaService.getCategoriaById(id);
        return ApiResponseStandard.success(
                categoria,
                "Categoría obtenida exitosamente");
    }

    @PostMapping
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ApiResponseStandard<CategoriaResponse> createCategoria(
            @Valid @RequestBody CategoriaRequest categoriaRequest) {
        CategoriaResponse categoria = categoriaService.createCategoria(categoriaRequest);
        return ApiResponseStandard.success(
                categoria,
                "Categoría creada exitosamente");
    }

    @PostMapping("/bulk")
    @PreAuthorize("permitAll() or hasRole('ADMIN')")
    public ApiResponseStandard<List<CategoriaResponse>> createBulkCategorias(
            @Valid @RequestBody BulkCategoriaRequest bulkRequest) {
        List<CategoriaResponse> categorias = categoriaService.createBulkCategorias(bulkRequest);
        return ApiResponseStandard.success(
                categorias,
                "Categorías creadas exitosamente");
    }

    @GetMapping("/nemonico/{nemonico}")
    public ApiResponseStandard<CategoriaResponse> getCategoriaByNemonico(@PathVariable String nemonico) {
        CategoriaResponse response = categoriaService.getCategoriaByNemonico(nemonico);
        return ApiResponseStandard.success(response, "Categoría obtenida exitosamente");
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ApiResponseStandard<CategoriaResponse> updateCategoria(
            @PathVariable Long id,
            @Valid @RequestBody CategoriaRequest categoriaRequest) {
        CategoriaResponse categoria = categoriaService.updateCategoria(id, categoriaRequest);
        return ApiResponseStandard.success(
                categoria,
                "Categoría actualizada exitosamente");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponseStandard<Void> deleteCategoria(@PathVariable Long id) {
        categoriaService.deleteCategoria(id);
        return ApiResponseStandard.success("Categoría eliminada exitosamente");
    }
}
