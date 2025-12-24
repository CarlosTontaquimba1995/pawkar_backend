package pawkar.backend.config;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pawkar.backend.entity.Role;
import pawkar.backend.enums.ERole;
import pawkar.backend.repository.RoleRepository;

@Configuration
public class DataInitializer {
    
    @Bean
    public CommandLineRunner loadData(RoleRepository roleRepository) {
        return args -> {
            // First, get all existing roles from the database
            List<Role> existingRoles = roleRepository.findAll();

            // Create a set of existing role names for quick lookup
            Set<String> existingRoleNames = existingRoles.stream()
                    .map(Role::getName)
                    .collect(Collectors.toSet());

            // Only create roles that are defined in the ERole enum and don't exist in the
            // database
            for (ERole role : ERole.values()) {
                if (!existingRoleNames.contains(role.name())) {
                    String detail = getRoleDetail(role);
                    Role newRole = new Role(role.name(), detail);
                    roleRepository.save(newRole);
                }
            }

            // Log any roles in the database that aren't in the ERole enum
            existingRoles.stream()
                    .filter(role -> !Arrays.asList(ERole.values()).stream()
                            .anyMatch(e -> e.name().equals(role.getName())))
                    .forEach(role -> System.out
                            .println("Warning: Role in database not found in ERole enum: " + role.getName()));
        };
    }

    private String getRoleDetail(ERole role) {
        return switch (role) {
            case ROLE_ADMIN -> "Administrador con acceso total al sistema";
            case ROLE_USER -> "Usuario estándar con acceso básico";
            case PORTERO -> "Jugador en la posición de portero";
            case DEFENSA_CENTRAL -> "Defensa central";
            case DEFENSA_LATERAL_DERECHO -> "Lateral derecho";
            case DEFENSA_LATERAL_IZQUIERDO -> "Lateral izquierdo";
            case CARRILERO_DERECHO -> "Carrilero derecho";
            case CARRILERO_IZQUIERDO -> "Carrilero izquierdo";
            case LIBERO -> "Líbero";
            case MEDIOCENTRO_DEFENSIVO -> "Mediocentro defensivo";
            case MEDIOCENTRO_MIXTO -> "Mediocentro mixto";
            case MEDIOCENTRO_OFENSIVO -> "Mediocentro ofensivo";
            case INTERIOR_DERECHO -> "Interior derecho";
            case INTERIOR_IZQUIERDO -> "Interior izquierdo";
            case EXTREMO_DERECHO -> "Extremo derecho";
            case EXTREMO_IZQUIERDO -> "Extremo izquierdo";
            case SEGUNDO_DELANTERO -> "Segundo delantero";
            default -> "Rol sin descripción específica: " + role;
        };
    }
}
