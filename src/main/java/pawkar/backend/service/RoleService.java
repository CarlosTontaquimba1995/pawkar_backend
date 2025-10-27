package pawkar.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pawkar.backend.request.RoleRequest;
import pawkar.backend.response.RoleResponse;
import pawkar.backend.entity.Role;
import pawkar.backend.exception.BadRequestException;
import pawkar.backend.exception.ResourceNotFoundException;
import pawkar.backend.repository.RoleRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Transactional
    public RoleResponse createOrUpdateRole(RoleRequest roleRequest) {
        if (roleRequest.getName() == null || roleRequest.getName().trim().isEmpty()) {
            throw new BadRequestException("El nombre del rol no puede estar vacío");
        }

        // Convertir a mayúsculas para evitar duplicados por diferencias en mayúsculas/minúsculas
        String roleName = roleRequest.getName().trim().toUpperCase();
        
        Role role = roleRepository.findByName(roleName)
                .map(existingRole -> {
                    existingRole.setDetail(roleRequest.getDetail());
                    return existingRole;
                })
                .orElseGet(() -> new Role(roleName, roleRequest.getDetail()));

        Role savedRole = roleRepository.save(role);
        return RoleResponse.fromEntity(savedRole);
    }

    @Transactional
    public List<RoleResponse> createOrUpdateRoles(List<RoleRequest> roleRequests) {
        return roleRequests.stream()
                .map(this::createOrUpdateRole)
                .collect(Collectors.toList());
    }

    public List<RoleResponse> getAllRoles() {
        return roleRepository.findAll().stream()
                .map(RoleResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public Optional<RoleResponse> getRoleByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return Optional.empty();
        }
        return roleRepository.findByName(name.trim().toUpperCase())
                .map(RoleResponse::fromEntity);
    }

    public Optional<RoleResponse> getRoleById(Long id) {
        return roleRepository.findById(id)
                .map(RoleResponse::fromEntity);
    }

    @Transactional
    public RoleResponse updateRole(Long id, RoleRequest roleRequest) {
        if (roleRequest.getName() == null || roleRequest.getName().trim().isEmpty()) {
            throw new BadRequestException("El nombre del rol no puede estar vacío");
        }

        String newName = roleRequest.getName().trim().toUpperCase();
        
        // Verificar si ya existe otro rol con el mismo nombre (excluyendo el actual)
        roleRepository.findByName(newName)
            .filter(existingRole -> !existingRole.getId().equals(id))
            .ifPresent(role -> {
                throw new BadRequestException("Ya existe un rol con el nombre: " + newName);
            });

        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el rol con ID: " + id));

        role.setName(newName);
        role.setDetail(roleRequest.getDetail());
        Role updatedRole = roleRepository.save(role);
        return RoleResponse.fromEntity(updatedRole);
    }

    @Transactional
    public void deleteRole(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el rol con ID: " + id));
        
        // Verificar si el rol está siendo utilizado en alguna relación
        if (role.getSubcategorias() != null && !role.getSubcategorias().isEmpty()) {
            throw new IllegalStateException("No se puede eliminar el rol porque está siendo utilizado en una o más subcategorías");
        }
        
        roleRepository.delete(role);
    }
}
