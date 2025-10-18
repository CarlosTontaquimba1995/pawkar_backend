package pawkar.backend.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pawkar.backend.request.BulkRoleRequest;
import pawkar.backend.request.RoleRequest;
import pawkar.backend.response.RoleResponse;
import pawkar.backend.enums.ERole;
import pawkar.backend.response.ApiResponseStandard;
import pawkar.backend.service.RoleService;

import java.util.List;

@RestController
@RequestMapping("/roles")
public class RoleController {

    private final RoleService roleService;

    @Autowired
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping
    public ResponseEntity<ApiResponseStandard<RoleResponse>> createOrUpdateRole(
            @Valid @RequestBody RoleRequest roleRequest) {
        RoleResponse response = roleService.createOrUpdateRole(roleRequest);
        String message = response.getId() == null ? "Rol creado exitosamente" : "Rol actualizado exitosamente";
        return ResponseEntity.ok(
                ApiResponseStandard.success(response, message));
    }

    @PostMapping("/bulk")
    public ResponseEntity<ApiResponseStandard<List<RoleResponse>>> createOrUpdateRoles(
            @Valid @RequestBody BulkRoleRequest bulkRequest) {
        List<RoleResponse> responses = roleService.createOrUpdateRoles(bulkRequest.getRoles());
        return ResponseEntity.ok(
                ApiResponseStandard.success(responses, "Roles procesados exitosamente"));
    }

    @GetMapping
    public ResponseEntity<ApiResponseStandard<List<RoleResponse>>> getAllRoles() {
        List<RoleResponse> roles = roleService.getAllRoles();
        String message = roles.isEmpty() ? "No se encontraron roles" : "Lista de roles obtenida exitosamente";
        return ResponseEntity.ok(
                ApiResponseStandard.success(roles, message));
    }

    @GetMapping("/{name}")
    public ResponseEntity<ApiResponseStandard<RoleResponse>> getRoleByName(@PathVariable String name) {
        try {
            ERole roleEnum = ERole.valueOf(name.toUpperCase());
            return roleService.getRoleByName(roleEnum)
                    .map(role -> ResponseEntity.ok(
                            ApiResponseStandard.success(role, "Rol encontrado exitosamente")))
                    .orElseGet(() -> ResponseEntity.ok(
                            ApiResponseStandard.error("No encontrado", null, "No se encontró el rol especificado",
                                    404)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                    ApiResponseStandard.error("Error", null, "Nombre de rol no válido: " + name, 400));
        }
    }
}
