package pawkar.backend.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pawkar.backend.entity.Role;
import pawkar.backend.enums.ERole;
import pawkar.backend.repositories.RoleRepository;

@Configuration
public class DataInitializer {
    
    @Bean
    public CommandLineRunner loadData(RoleRepository roleRepository) {
        return args -> {
            // Create roles if they don't exist
            for (ERole role : ERole.values()) {
                if (!roleRepository.existsByName(role)) {
                    Role newRole = new Role();
                    newRole.setName(role);
                    roleRepository.save(newRole);
                }
            }
        };
    }
}
