package pawkar.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import pawkar.backend.request.BulkSubcategoriaRolRequest;
import pawkar.backend.response.ApiResponseStandard;
import pawkar.backend.service.SubcategoriaRolService;

import java.util.Map;

@RestController
@RequestMapping("/subcategoria-roles")
public class SubcategoriaRolController {

    private final SubcategoriaRolService subcategoriaRolService;

    @Autowired
    public SubcategoriaRolController(SubcategoriaRolService subcategoriaRolService) {
        this.subcategoriaRolService = subcategoriaRolService;
    }

    @PostMapping("/subcategoria/{subcategoriaId}/rol/{rolId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponseStandard<?> asignarRolASubcategoria(
            @PathVariable Integer subcategoriaId,
            @PathVariable Long rolId) {
        try {
            Map<String, Object> result = subcategoriaRolService.asignarRolASubcategoria(subcategoriaId, rolId);
            return ApiResponseStandard.success(result, "Rol asignado correctamente a la subcategoría");
        } catch (Exception e) {
            return ApiResponseStandard.error(e.getMessage(),
                    "/api/subcategoria-roles/subcategoria/" + subcategoriaId + "/rol/" + rolId,
                    "Bad Request", 400);
        }
    }

    @DeleteMapping("/subcategoria/{subcategoriaId}/rol/{rolId}")
    public ApiResponseStandard<?> eliminarRolDeSubcategoria(
            @PathVariable Integer subcategoriaId,
            @PathVariable Long rolId) {
        try {
            subcategoriaRolService.eliminarRolDeSubcategoria(subcategoriaId, rolId);
            return ApiResponseStandard.success("Rol eliminado correctamente de la subcategoría");
        } catch (Exception e) {
            return ApiResponseStandard.error(e.getMessage(),
                    "/api/subcategoria-roles/subcategoria/" + subcategoriaId + "/rol/" + rolId,
                    "Bad Request", 400);
        }
    }

    @GetMapping("/subcategoria/{subcategoriaId}")
    public ApiResponseStandard<?> obtenerRolesPorSubcategoria(
            @PathVariable Integer subcategoriaId) {
        try {
            return ApiResponseStandard.success(subcategoriaRolService.obtenerRolesPorSubcategoria(subcategoriaId),
                    "Roles obtenidos correctamente");
        } catch (Exception e) {
            return ApiResponseStandard.error(
                    e.getMessage(),
                    "/api/subcategoria-roles/subcategoria/" + subcategoriaId,
                    "Error al obtener roles",
                    500);
        }
    }

    @GetMapping("/subcategoria/nombre/{nombreSubcategoria}")
    public ApiResponseStandard<?> obtenerRolesPorNombreSubcategoria(@PathVariable String nombreSubcategoria) {
        try {
            return ApiResponseStandard.success(
                    subcategoriaRolService.obtenerRolesPorNombreSubcategoria(nombreSubcategoria),
                    "Roles obtenidos correctamente para la subcategoría: " + nombreSubcategoria);
        } catch (Exception e) {
            return ApiResponseStandard.error("Error al obtener roles por nombre de subcategoría",
                    "/api/subcategoria-roles/subcategoria/nombre/" + nombreSubcategoria,
                    e.getMessage(),
                    500);
        }
    }

    @PostMapping("/bulk")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponseStandard<?> asignarRolesASubcategoria(@RequestBody BulkSubcategoriaRolRequest request) {
        try {
            Map<String, Object> result = subcategoriaRolService.asignarRolesASubcategoria(request);
            return ApiResponseStandard.success(result, "Roles asignados correctamente a la subcategoría");
        } catch (Exception e) {
            return ApiResponseStandard.error(e.getMessage(), "/api/subcategoria-roles/bulk",
                    "Bad Request", 400);
        }
    }
}
