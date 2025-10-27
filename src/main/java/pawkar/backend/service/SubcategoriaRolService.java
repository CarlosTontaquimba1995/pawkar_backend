package pawkar.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pawkar.backend.exception.BadRequestException;
import pawkar.backend.exception.ResourceNotFoundException;
import pawkar.backend.entity.Role;
import pawkar.backend.entity.Subcategoria;
import pawkar.backend.entity.SubcategoriaRol;
import pawkar.backend.repository.RoleRepository;
import pawkar.backend.repository.SubcategoriaRepository;
import pawkar.backend.repository.SubcategoriaRolRepository;

import java.util.*;
import java.util.stream.Collectors;
import pawkar.backend.request.BulkSubcategoriaRolRequest;
import pawkar.backend.dto.SubcategoriaRolResponseDto;
import pawkar.backend.dto.SubcategoriaRolDto;
import pawkar.backend.request.SubcategoriaRolRequest;

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
                        sr.getRol().getName(),
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
    public Map<String, Object> actualizarRolSubcategoria(Integer id, SubcategoriaRolRequest request) {
        // Primero necesitamos obtener la relación actual usando los campos compuestos
        // Como no tenemos un ID directo, necesitamos una forma de identificar la
        // relación
        // Vamos a asumir que el ID es el ID de la subcategoría para este ejemplo
        // En un caso real, necesitaríamos una mejor forma de identificar la relación

        // Verificar que la nueva subcategoría existe
        Subcategoria nuevaSubcategoria = subcategoriaRepository.findById(request.getSubcategoriaId())
                .orElseThrow(() -> new RuntimeException(
                        "Subcategoría no encontrada con ID: " + request.getSubcategoriaId()));

        // Verificar que el nuevo rol existe
        Role nuevoRol = roleRepository.findById(request.getRolId())
                .orElseThrow(() -> new RuntimeException("Rol no encontrado con ID: " + request.getRolId()));

        // Verificar si ya existe la misma relación (subcategoría + rol)
        if (subcategoriaRolRepository.existsBySubcategoria_SubcategoriaIdAndRol_Id(
                request.getSubcategoriaId(), request.getRolId())) {
            throw new RuntimeException("Ya existe una relación entre esta subcategoría y el rol");
        }

        // Actualizar la relación
        int updated = subcategoriaRolRepository.updateSubcategoriaRol(
                nuevaSubcategoria,
                nuevoRol,
                id, // ID de la subcategoría actual
                request.getRolId() // ID del rol actual
        );

        if (updated == 0) {
            throw new RuntimeException("No se pudo actualizar la relación. Verifique que exista la relación original.");
        }

        // Preparar respuesta
        Map<String, Object> response = new HashMap<>();
        response.put("subcategoriaId", nuevaSubcategoria.getSubcategoriaId());
        response.put("subcategoriaNombre", nuevaSubcategoria.getNombre());
        response.put("rolId", nuevoRol.getId());
        response.put("rolName", nuevoRol.getName());
        response.put("mensaje", "Relación actualizada exitosamente");

        return response;
    }

    @Transactional
    public Map<String, Object> asignarRolesASubcategoria(BulkSubcategoriaRolRequest request) {
        // Validar que la lista de roles no esté vacía
        if (request.getRoles() == null || request.getRoles().isEmpty()) {
            throw new BadRequestException("La lista de roles no puede estar vacía");
        }

        Integer subcategoriaId = request.getSubcategoriaId();
        List<Long> rolIds = request.getRoles();

        // Verificar roles duplicados en la solicitud
        long uniqueRolesCount = rolIds.stream().distinct().count();
        if (uniqueRolesCount < rolIds.size()) {
            throw new BadRequestException("No se permiten roles duplicados en la solicitud");
        }

        // Verificar que la subcategoría existe
        Subcategoria subcategoria = subcategoriaRepository.findById(subcategoriaId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Subcategoría no encontrada con ID: " + subcategoriaId));

        // Verificar que todos los roles existen
        List<Role> roles = roleRepository.findAllById(rolIds);
        if (roles.size() != rolIds.size()) {
            List<Long> foundRoleIds = roles.stream()
                    .map(Role::getId)
                    .collect(Collectors.toList());

            List<Long> missingRoleIds = rolIds.stream()
                    .filter(id -> !foundRoleIds.contains(id))
                    .collect(Collectors.toList());

            throw new ResourceNotFoundException("Los siguientes IDs de roles no existen: " + missingRoleIds);
        }

        // Verificar roles ya asignados a la subcategoría
        List<SubcategoriaRol> existingRelations = subcategoriaRolRepository
                .findBySubcategoria_SubcategoriaIdAndRol_IdIn(subcategoriaId, rolIds);

        if (!existingRelations.isEmpty()) {
            List<String> existingRoleNames = existingRelations.stream()
                    .map(sr -> sr.getRol().getName())
                    .collect(Collectors.toList());
            throw new BadRequestException("La subcategoría, ya tiene asignado los siguientes roles: " +
                    String.join(", ", existingRoleNames));
        }

        // Crear y guardar nuevas relaciones
        List<SubcategoriaRol> newRelations = roles.stream()
                .map(role -> new SubcategoriaRol(subcategoria, role))
                .collect(Collectors.toList());

        subcategoriaRolRepository.saveAll(newRelations);

        // Obtener nombres de los roles asignados
        List<String> nombresRolesAsignados = newRelations.stream()
                .map(sr -> sr.getRol().getName())
                .collect(Collectors.toList());

        // Preparar respuesta
        Map<String, Object> response = new HashMap<>();
        response.put("subcategoriaId", subcategoriaId);
        response.put("totalRolesAsignados", nombresRolesAsignados.size());
        response.put("rolesAsignados", nombresRolesAsignados);

        return response;
    }
}
