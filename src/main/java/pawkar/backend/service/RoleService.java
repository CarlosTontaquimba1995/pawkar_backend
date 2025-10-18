package pawkar.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pawkar.backend.request.RoleRequest;
import pawkar.backend.response.RoleResponse;
import pawkar.backend.entity.Role;
import pawkar.backend.enums.ERole;
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
}
