package pawkar.backend.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pawkar.backend.request.BulkRoleRequest;
import pawkar.backend.request.RoleRequest;
import pawkar.backend.response.RoleResponse;
import pawkar.backend.enums.ERole;
import pawkar.backend.response.ApiResponseStandard;
import pawkar.backend.service.RoleService;
import pawkar.backend.exception.ResourceNotFoundException;

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
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponseStandard<RoleResponse> createOrUpdateRole(
            @Valid @RequestBody RoleRequest roleRequest) {
        RoleResponse response = roleService.createOrUpdateRole(roleRequest);
        String message = response.getId() == null ? "Rol creado exitosamente" : "Rol actualizado exitosamente";
        return ApiResponseStandard.success(response, message);
    }

    @PostMapping("/bulk")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponseStandard<List<RoleResponse>> createOrUpdateRoles(
            @Valid @RequestBody BulkRoleRequest bulkRequest) {
        List<RoleResponse> responses = roleService.createOrUpdateRoles(bulkRequest.getRoles());
        return ApiResponseStandard.success(responses, "Roles procesados exitosamente");
    }

    @GetMapping
    public ApiResponseStandard<List<RoleResponse>> getAllRoles() {
        List<RoleResponse> roles = roleService.getAllRoles();
        String message = roles.isEmpty() ? "No se encontraron roles" : "Lista de roles obtenida exitosamente";
        return ApiResponseStandard.success(roles, message);
    }

    @GetMapping("/{name}")
    public ApiResponseStandard<RoleResponse> getRoleByName(@PathVariable String name) {
        try {
            ERole roleEnum = ERole.valueOf(name.toUpperCase());
            return roleService.getRoleByName(roleEnum)
                    .map(role -> ApiResponseStandard.success(role, "Rol encontrado exitosamente"))
                    .orElseThrow(() -> new ResourceNotFoundException("No se encontró el rol especificado"));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Nombre de rol no válido: " + name);
        }
    }
}
