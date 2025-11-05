package pawkar.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pawkar.backend.entity.Subcategoria;
import pawkar.backend.repository.SubcategoriaRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SubcategoriaScheduler {

    @Autowired
    private SubcategoriaRepository subcategoriaRepository;

    // Run every minute to check for subcategorias that need updating
    @Scheduled(fixedRate = 3_600_000) // 60,000 ms = 1 minute (for hourly: 3_600_000 = 1 hour)
    @Transactional
    public void updateProximoStatus() {
        // Get current date and time
        LocalDateTime now = LocalDateTime.now();

        // Find all subcategorias where proximo is true and fechaHora is in the past
        List<Subcategoria> subcategorias = subcategoriaRepository.findByProximoTrueAndFechaHoraBefore(now);

        if (!subcategorias.isEmpty()) {
            // Update all matching subcategorias
            subcategorias.forEach(subcategoria -> {
                subcategoria.setProximo(false);
                subcategoriaRepository.save(subcategoria);
            });

            // Log the update
            System.out.println("Updated " + subcategorias.size() + " subcategorias: Set proximo to false");
        }
    }
}
