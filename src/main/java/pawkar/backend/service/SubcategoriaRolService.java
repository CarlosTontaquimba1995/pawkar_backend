package pawkar.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pawkar.backend.entity.Role;
import pawkar.backend.entity.Subcategoria;
import pawkar.backend.entity.SubcategoriaRol;
import pawkar.backend.repository.RoleRepository;
import pawkar.backend.repository.SubcategoriaRolRepository;
import pawkar.backend.repository.SubcategoriaRepository;

import java.util.*;
import java.util.stream.Collectors;
import pawkar.backend.request.BulkSubcategoriaRolRequest;
import pawkar.backend.dto.SubcategoriaRolResponseDto;
import pawkar.backend.dto.SubcategoriaRolDto;

@Service
public class SubcategoriaRolService {

    private final SubcategoriaRolRepository subcategoriaRolRepository;
    private final SubcategoriaRepository subcategoriaRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public SubcategoriaRolService(SubcategoriaRolRepository subcategoriaRolRepository,
            SubcategoriaRepository subcategoriaRepository,
            RoleRepository roleRepository) {
        this.subcategoriaRolRepository = subcategoriaRolRepository;
        this.subcategoriaRepository = subcategoriaRepository;
        this.roleRepository = roleRepository;
    }

    @Transactional
    public Map<String, Object> asignarRolASubcategoria(Integer subcategoriaId, Long rolId) {
        if (subcategoriaRolRepository.existsBySubcategoria_SubcategoriaIdAndRol_Id(subcategoriaId, rolId)) {
            throw new RuntimeException("El rol ya está asignado a esta subcategoría");
        }

        Subcategoria subcategoria = subcategoriaRepository.findById(subcategoriaId)
                .orElseThrow(() -> new RuntimeException("Subcategoría no encontrada"));

        Role rol = roleRepository.findById(rolId)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

        SubcategoriaRol subcategoriaRol = new SubcategoriaRol(subcategoria, rol);
        subcategoriaRolRepository.save(subcategoriaRol);

        Map<String, Object> response = new HashMap<>();
        response.put("subcategoriaId", subcategoriaId);
        response.put("rolId", rolId);

        return response;
    }

    @Transactional
    public void eliminarRolDeSubcategoria(Integer subcategoriaId, Long rolId) {
        if (!subcategoriaRolRepository.existsBySubcategoria_SubcategoriaIdAndRol_Id(subcategoriaId, rolId)) {
            throw new RuntimeException("No se encontró la relación entre la subcategoría y el rol");
        }
        subcategoriaRolRepository.deleteBySubcategoria_SubcategoriaIdAndRol_Id(subcategoriaId, rolId);
    }

    @Transactional(readOnly = true)
    public List<SubcategoriaRolDto> obtenerRolesPorSubcategoria(Integer subcategoriaId) {
        if (!subcategoriaRepository.existsById(subcategoriaId)) {
            throw new RuntimeException("Subcategoría no encontrada");
        }

        return subcategoriaRolRepository.findBySubcategoria_SubcategoriaId(subcategoriaId).stream()
                .map(sr -> new SubcategoriaRolDto(
                        sr.getRol().getId(),
                        sr.getRol().getName().name(),
                        sr.getRol().getDetail(),
                        sr.getSubcategoria().getSubcategoriaId(),
                        sr.getSubcategoria().getNombre()))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<SubcategoriaRolResponseDto> obtenerRolesPorNombreSubcategoria(String nombreSubcategoria) {
        List<SubcategoriaRol> roles = subcategoriaRolRepository.findBySubcategoriaNombre(nombreSubcategoria);
        if (roles.isEmpty()) {
            throw new RuntimeException("No se encontraron roles para la subcategoría: " + nombreSubcategoria);
        }
        
        // Convert to DTO with only required fields
        return roles.stream()
            .map(rol -> new SubcategoriaRolResponseDto(
                rol.getSubcategoria().getCategoria().getNombre(),
                rol.getSubcategoria().getNombre(),
                rol.getRol().getDetail()
            ))
            .collect(Collectors.toList());
    }

    @Transactional
    public Map<String, Object> asignarRolesASubcategoria(BulkSubcategoriaRolRequest request) {
        Integer subcategoriaId = request.getSubcategoriaId();
        List<Long> rolIds = request.getRoles();

        // Verificar que la subcategoría existe
        Subcategoria subcategoria = subcategoriaRepository.findById(subcategoriaId)
                .orElseThrow(() -> new RuntimeException("Subcategoría no encontrada"));

        // Verificar que todos los roles existen
        List<Role> roles = roleRepository.findAllById(rolIds);
        if (roles.size() != rolIds.size()) {
            List<Long> foundRoleIds = roles.stream()
                    .map(Role::getId)
                    .collect(Collectors.toList());

            List<Long> missingRoleIds = rolIds.stream()
                    .filter(id -> !foundRoleIds.contains(id))
                    .collect(Collectors.toList());

            throw new RuntimeException("Los siguientes IDs de roles no existen: " + missingRoleIds);
        }

        // Filtrar relaciones que ya existen
        List<SubcategoriaRol> existingRelations = subcategoriaRolRepository
                .findBySubcategoria_SubcategoriaIdAndRol_IdIn(subcategoriaId, rolIds);

        Set<Long> existingRoleIds = existingRelations.stream()
                .map(sr -> sr.getRol().getId())
                .collect(Collectors.toSet());

        // Crear nuevas relaciones
        List<SubcategoriaRol> newRelations = roles.stream()
                .filter(role -> !existingRoleIds.contains(role.getId()))
                .map(role -> new SubcategoriaRol(subcategoria, role))
                .collect(Collectors.toList());

        // Guardar nuevas relaciones
        if (!newRelations.isEmpty()) {
            subcategoriaRolRepository.saveAll(newRelations);
        }

        // Preparar respuesta
        Map<String, Object> response = new HashMap<>();
        response.put("subcategoriaId", subcategoriaId);
        response.put("totalRolesAsignados", rolIds.size());
        response.put("nuevasAsignaciones", newRelations.size());
        response.put("asignacionesExistentes", existingRelations.size());
        response.put("rolesAsignados", rolIds);

        return response;
    }
}
