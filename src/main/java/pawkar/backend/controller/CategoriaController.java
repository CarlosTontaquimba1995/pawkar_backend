package pawkar.backend.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pawkar.backend.entity.Categoria;
import pawkar.backend.repository.CategoriaRepository;
import pawkar.backend.request.BulkCategoriaRequest;
import pawkar.backend.request.CategoriaRequest;
import pawkar.backend.exception.ResourceNotFoundException;
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
    private CategoriaRepository categoriaRepository;

    @Autowired
    public CategoriaController(CategoriaService categoriaService, CategoriaRepository categoriaRepository) {
        this.categoriaService = categoriaService;
        this.categoriaRepository = categoriaRepository;
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
        try {
            Categoria categoria = categoriaRepository.findByNemonico(nemonico)
                    .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con nemonico: " + nemonico));

            CategoriaResponse response = new CategoriaResponse();
            response.setCategoriaId(categoria.getCategoriaId());
            response.setNombre(categoria.getNombre());
            response.setNemonico(categoria.getNemonico());
            response.setEstado(categoria.isEstado());

            return ApiResponseStandard.success(response, "Categoría obtenida exitosamente");

        } catch (ResourceNotFoundException e) {
            return ApiResponseStandard.error(e.getMessage(),
                    "/api/categorias/nemonico/" + nemonico,
                    "Not Found", 404);
        } catch (Exception e) {
            return ApiResponseStandard.error("Error al obtener la categoría: " + e.getMessage(),
                    "/api/categorias/nemonico/" + nemonico,
                    "Internal Server Error", 500);
        }
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
