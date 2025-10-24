package pawkar.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pawkar.backend.request.RoleRequest;
import pawkar.backend.response.RoleResponse;
import pawkar.backend.entity.Role;
import pawkar.backend.enums.ERole;
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
        Role role = roleRepository.findByName(roleRequest.getName())
                .map(existingRole -> {
                    existingRole.setDetail(roleRequest.getDetail());
                    return existingRole;
                })
                .orElseGet(() -> new Role(roleRequest.getName(), roleRequest.getDetail()));

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

    public Optional<RoleResponse> getRoleByName(ERole name) {
        return roleRepository.findByName(name)
                .map(RoleResponse::fromEntity);
    }

    public Optional<RoleResponse> getRoleById(Long id) {
        return roleRepository.findById(id)
                .map(RoleResponse::fromEntity);
    }

    @Transactional
    public RoleResponse updateRole(Long id, RoleRequest roleRequest) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el rol con ID: " + id));

        role.setName(roleRequest.getName());
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
