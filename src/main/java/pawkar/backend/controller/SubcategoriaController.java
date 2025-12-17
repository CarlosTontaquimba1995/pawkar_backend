package pawkar.backend.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import pawkar.backend.entity.Categoria;
import pawkar.backend.entity.Subcategoria;
import pawkar.backend.exception.ResourceNotFoundException;
import pawkar.backend.request.BulkSubcategoriaRequest;
import pawkar.backend.request.SubcategoriaRequest;
import pawkar.backend.response.ApiResponseStandard;
import pawkar.backend.response.SubcategoriaResponse;
import pawkar.backend.service.SubcategoriaService;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/subcategorias")
public class SubcategoriaController {

    @Autowired
    private SubcategoriaService subcategoriaService;

    @PostMapping("/bulk")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(org.springframework.http.HttpStatus.CREATED)
    public ApiResponseStandard<List<SubcategoriaResponse>> crearSubcategoriasBulk(
            @Valid @RequestBody BulkSubcategoriaRequest request) {
        List<Subcategoria> subcategorias = subcategoriaService.crearSubcategoriasBulk(request);
        List<SubcategoriaResponse> response = subcategorias.stream()
                .map(this::toSubcategoriaResponse)
                .collect(Collectors.toList());
        return ApiResponseStandard.success(response, "Subcategorías creadas exitosamente");
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ApiResponseStandard<SubcategoriaResponse> crearSubcategoria(
            @Valid @RequestBody SubcategoriaRequest request) {
        try {
            Subcategoria subcategoria = subcategoriaService.crearSubcategoria(request);
            SubcategoriaResponse response = toSubcategoriaResponse(subcategoria);
            return ApiResponseStandard.success(response, "Subcategoría creada exitosamente");
        } catch (Exception e) {
            return ApiResponseStandard.error(e.getMessage(), "/api/subcategorias", "Bad Request", 400);
        }
    }

    @GetMapping
    public ApiResponseStandard<List<SubcategoriaResponse>> listarSubcategorias() {
        try {
            List<Subcategoria> subcategorias = subcategoriaService.listarSubcategorias();
            List<SubcategoriaResponse> response = subcategorias.stream()
                .map(this::toSubcategoriaResponse)
                .collect(Collectors.toList());
            return ApiResponseStandard.success(response, "Lista de subcategorías obtenida exitosamente");
        } catch (Exception e) {
            return ApiResponseStandard.error("Error al listar subcategorías: " + e.getMessage(), 
                "/api/subcategorias", "Internal Server Error", 500);
        }
    }
    
    private SubcategoriaResponse toSubcategoriaResponse(Subcategoria subcategoria) {
        SubcategoriaResponse response = new SubcategoriaResponse();
        response.setSubcategoriaId(subcategoria.getSubcategoriaId());
        response.setNombre(subcategoria.getNombre());
        response.setDescripcion(subcategoria.getDescripcion());
        response.setEstado(subcategoria.getEstado());
        response.setFechaHora(subcategoria.getFechaHora());
        response.setProximo(subcategoria.getProximo());
        response.setUbicacion(subcategoria.getUbicacion());
        response.setLatitud(subcategoria.getLatitud());
        response.setLongitud(subcategoria.getLongitud());
        
        if (subcategoria.getCategoria() != null) {
            Categoria categoria = subcategoria.getCategoria();
            response.setCategoriaId(categoria.getCategoriaId());
            response.setCategoriaNombre(categoria.getNombre());
        }
        
        return response;
    }

    @GetMapping("/categoria/{categoriaId}")
    public ApiResponseStandard<List<SubcategoriaResponse>> listarSubcategoriasPorCategoria(
            @PathVariable Integer categoriaId) {
        List<SubcategoriaResponse> subcategorias = subcategoriaService
                .listarSubcategoriasPorCategoria(categoriaId)
                .stream()
                .map(this::toSubcategoriaResponse)
                .collect(Collectors.toList());
        return ApiResponseStandard.success(subcategorias, "Subcategorías obtenidas exitosamente");
    }

    @GetMapping("/nemonico/{nemonico}")
    public ApiResponseStandard<SubcategoriaResponse> obtenerSubcategoriaPorNemonico(
            @PathVariable String nemonico) {
        Subcategoria subcategoria = subcategoriaService.obtenerSubcategoriaPorNemonico(nemonico);
        return ApiResponseStandard.success(toSubcategoriaResponse(subcategoria), "Subcategoría obtenida exitosamente");
    }

    @GetMapping("/{id}")
    public ApiResponseStandard<SubcategoriaResponse> obtenerSubcategoriaPorId(@PathVariable Integer id) {
        try {
            Subcategoria subcategoria = subcategoriaService.obtenerSubcategoriaPorId(id);
            SubcategoriaResponse response = toSubcategoriaResponse(subcategoria);
            return ApiResponseStandard.success(response, "Subcategoría encontrada exitosamente");
        } catch (Exception e) {
            return ApiResponseStandard.error(e.getMessage(), 
                "/api/subcategorias/" + id, "Not Found", 404);
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ApiResponseStandard<SubcategoriaResponse> actualizarSubcategoria(
            @PathVariable Integer id,
            @Valid @RequestBody SubcategoriaRequest request) {
        try {
            Subcategoria subcategoria = subcategoriaService.actualizarSubcategoria(id, request);
            SubcategoriaResponse response = toSubcategoriaResponse(subcategoria);
            return ApiResponseStandard.success(response, "Subcategoría actualizada exitosamente");
        } catch (Exception e) {
            return ApiResponseStandard.error(e.getMessage(), 
                "/api/subcategorias/" + id, "Bad Request", 400);
        }
    }

    @GetMapping("/eventos/proximos")
    public ApiResponseStandard<List<SubcategoriaResponse>> getProximosEventos() {
        try {
            List<Subcategoria> subcategorias = subcategoriaService.findProximosEventos();
            List<SubcategoriaResponse> response = subcategorias.stream()
                    .map(this::toSubcategoriaResponse)
                    .collect(Collectors.toList());

            return ApiResponseStandard.<List<SubcategoriaResponse>>success(response,
                    "Próximos eventos obtenidos correctamente");

        } catch (ResourceNotFoundException e) {
            return ApiResponseStandard.<List<SubcategoriaResponse>>error(
                    e.getMessage(),
                    "/api/subcategorias/eventos/proximos",
                    "Not Found", 404);
        } catch (Exception e) {
            return ApiResponseStandard.<List<SubcategoriaResponse>>error(
                    "Error al obtener próximos eventos: " + e.getMessage(),
                    "/api/subcategorias/eventos/proximos",
                    "Internal Server Error", 500);
        }
    }

    @GetMapping("/eventos/pasados")
    public ApiResponseStandard<List<SubcategoriaResponse>> getEventosPasados() {
        try {
            List<Subcategoria> subcategorias = subcategoriaService.findEventosPasados();
            List<SubcategoriaResponse> response = subcategorias.stream()
                    .map(this::toSubcategoriaResponse)
                    .collect(Collectors.toList());

            return ApiResponseStandard.<List<SubcategoriaResponse>>success(response,
                    "Eventos pasados obtenidos correctamente");

        } catch (ResourceNotFoundException e) {
            return ApiResponseStandard.<List<SubcategoriaResponse>>error(
                    e.getMessage(),
                    "/api/subcategorias/eventos/pasados",
                    "Not Found", 404);
        } catch (Exception e) {
            return ApiResponseStandard.<List<SubcategoriaResponse>>error(
                    "Error al obtener eventos pasados: " + e.getMessage(),
                    "/api/subcategorias/eventos/pasados",
                    "Internal Server Error", 500);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ApiResponseStandard<Void> eliminarSubcategoria(@PathVariable Integer id) {
        try {
            subcategoriaService.eliminarSubcategoria(id);
            return ApiResponseStandard.success("Subcategoría eliminada exitosamente");
        } catch (Exception e) {
            return ApiResponseStandard.error(e.getMessage(), 
                "/api/subcategorias/" + id, "Bad Request", 400);
        }
    }

}
