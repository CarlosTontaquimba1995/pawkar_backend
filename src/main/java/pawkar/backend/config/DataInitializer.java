package pawkar.backend.config;

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
            // Create roles if they don't exist
            for (ERole role : ERole.values()) {
                if (!roleRepository.existsByName(role.name())) {
                    String detail = getRoleDetail(role);
                    Role newRole = new Role(role.name(), detail);
                    roleRepository.save(newRole);
                }
            }
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
            default -> "Rol: " + role.name();
        };
    }
}
