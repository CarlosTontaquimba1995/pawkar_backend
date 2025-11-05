package pawkar.backend.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pawkar.backend.entity.Categoria;
import pawkar.backend.entity.Subcategoria;
import pawkar.backend.repository.CategoriaRepository;
import pawkar.backend.repository.SubcategoriaRepository;
import pawkar.backend.request.BulkCategoriaRequest;
import pawkar.backend.request.CategoriaRequest;
import pawkar.backend.exception.ResourceNotFoundException;
import pawkar.backend.response.ApiResponseStandard;
import pawkar.backend.response.CategoriaResponse;
import pawkar.backend.response.SubcategoriaResponse;
import pawkar.backend.service.CategoriaService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;
    private CategoriaRepository categoriaRepository;
    private SubcategoriaRepository subcategoriaRepository;

    @Autowired
    public CategoriaController(CategoriaService categoriaService, CategoriaRepository categoriaRepository, SubcategoriaRepository subcategoriaRepository) {
        this.categoriaService = categoriaService;
        this.categoriaRepository = categoriaRepository;
        this.subcategoriaRepository = subcategoriaRepository;
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
    public ResponseEntity<ApiResponseStandard<CategoriaResponse>> getCategoriaByNemonico(@PathVariable String nemonico) {
        try {
            Categoria categoria = categoriaRepository.findByNemonico(nemonico)
                    .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con nemonico: " + nemonico));
            
            CategoriaResponse response = convertToResponse(categoria);
            return ResponseEntity.ok(ApiResponseStandard.success(response, "Categoría obtenida exitosamente"));
            
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponseStandard.error(e.getMessage(), 
                            "/api/categorias/nemonico/" + nemonico, 
                            "Not Found", 404));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponseStandard.error("Error al obtener la categoría: " + e.getMessage(), 
                            "/api/categorias/nemonico/" + nemonico, 
                            "Internal Server Error", 500));
        }
    }
    
    @GetMapping("/eventos/subcategorias")
    public ResponseEntity<ApiResponseStandard<List<SubcategoriaResponse>>> getSubcategoriasDeEventos() {
        try {
            // Buscar la categoría de eventos (EVENTOS o EVENTO)
            Optional<Categoria> categoriaOpt = categoriaRepository.findEventosCategoria();
            
            if (categoriaOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponseStandard.<List<SubcategoriaResponse>>error("No se encontró la categoría de eventos", 
                                "/api/categorias/eventos/subcategorias", 
                                "Not Found", 404));
            }
            
            // Obtener las subcategorías de la categoría encontrada
            List<Subcategoria> subcategorias = subcategoriaRepository
                    .findByCategoria_CategoriaId(categoriaOpt.get().getCategoriaId());
            
            // Convertir a DTOs
            List<SubcategoriaResponse> response = subcategorias.stream()
                    .map(this::toSubcategoriaResponse)
                    .collect(Collectors.toList());
            
        return ResponseEntity.ok(ApiResponseStandard.<List<SubcategoriaResponse>>success(response, "Subcategorías de eventos obtenidas correctamente"));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponseStandard.<List<SubcategoriaResponse>>error("Error al obtener subcategorías de eventos: " + e.getMessage(), 
                            "/api/categorias/eventos/subcategorias", 
                            "Internal Server Error", 500));
        }
    }
    
    private CategoriaResponse convertToResponse(Categoria categoria) {
        CategoriaResponse response = new CategoriaResponse();
        response.setCategoriaId(categoria.getCategoriaId());
        response.setNombre(categoria.getNombre());
        response.setNemonico(categoria.getNemonico());
        response.setEstado(categoria.isEstado());
        return response;
    }
    
    private SubcategoriaResponse toSubcategoriaResponse(Subcategoria subcategoria) {
        SubcategoriaResponse response = new SubcategoriaResponse();
        response.setSubcategoriaId(subcategoria.getSubcategoriaId());
        response.setNombre(subcategoria.getNombre());
        response.setDescripcion(subcategoria.getDescripcion());
        response.setEstado(subcategoria.getEstado());
        response.setFechaHora(subcategoria.getFechaHora());
        response.setProximo(subcategoria.getProximo());
        
        if (subcategoria.getCategoria() != null) {
            response.setCategoriaId(subcategoria.getCategoria().getCategoriaId());
            response.setCategoriaNombre(subcategoria.getCategoria().getNombre());
        }
        
        return response;
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
