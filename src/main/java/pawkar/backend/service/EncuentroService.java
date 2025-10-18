package pawkar.backend.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pawkar.backend.dto.EncuentroRequest;
import pawkar.backend.dto.EncuentroResponse;
import pawkar.backend.entity.Encuentro;
import pawkar.backend.entity.Subcategoria;
import pawkar.backend.repository.EncuentroRepository;
import pawkar.backend.repository.SubcategoriaRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EncuentroService {

    private final EncuentroRepository encuentroRepository;
    private final SubcategoriaRepository subcategoriaRepository;

    public EncuentroService(EncuentroRepository encuentroRepository, 
                          SubcategoriaRepository subcategoriaRepository) {
        this.encuentroRepository = encuentroRepository;
        this.subcategoriaRepository = subcategoriaRepository;
    }

    @Transactional
    public EncuentroResponse createEncuentro(EncuentroRequest request) {
        Subcategoria subcategoria = subcategoriaRepository.findById(request.getSubcategoriaId())
            .orElseThrow(() -> new EntityNotFoundException("Subcategoría no encontrada"));

        Encuentro encuentro = new Encuentro();
        encuentro.setSubcategoria(subcategoria);
        encuentro.setTitulo(request.getTitulo());
        encuentro.setFechaHora(request.getFechaHora());
        encuentro.setEstadioLugar(request.getEstadioLugar());
        
        if (request.getEstado() != null && !request.getEstado().isBlank()) {
            encuentro.setEstado(request.getEstado());
        }

        Encuentro savedEncuentro = encuentroRepository.save(encuentro);
        return mapToResponse(savedEncuentro);
    }

    @Transactional(readOnly = true)
    public List<EncuentroResponse> getAllEncuentros() {
        return encuentroRepository.findAll().stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EncuentroResponse getEncuentroById(Long id) {
        Encuentro encuentro = encuentroRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Encuentro no encontrado"));
        return mapToResponse(encuentro);
    }

    @Transactional(readOnly = true)
    public List<EncuentroResponse> getEncuentrosBySubcategoria(Long subcategoriaId) {
        return encuentroRepository.findBySubcategoria_SubcategoriaId(subcategoriaId).stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }

    @Transactional
    public EncuentroResponse updateEncuentro(Long id, EncuentroRequest request) {
        Encuentro encuentro = encuentroRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Encuentro no encontrado"));

        if (request.getSubcategoriaId() != null) {
            Subcategoria subcategoria = subcategoriaRepository.findById(request.getSubcategoriaId())
                .orElseThrow(() -> new EntityNotFoundException("Subcategoría no encontrada"));
            encuentro.setSubcategoria(subcategoria);
        }

        if (request.getTitulo() != null) {
            encuentro.setTitulo(request.getTitulo());
        }
        if (request.getFechaHora() != null) {
            encuentro.setFechaHora(request.getFechaHora());
        }
        if (request.getEstadioLugar() != null) {
            encuentro.setEstadioLugar(request.getEstadioLugar());
        }
        if (request.getEstado() != null && !request.getEstado().isBlank()) {
            encuentro.setEstado(request.getEstado());
        }

        Encuentro updatedEncuentro = encuentroRepository.save(encuentro);
        return mapToResponse(updatedEncuentro);
    }

    @Transactional
    public void deleteEncuentro(Long id) {
        if (!encuentroRepository.existsById(id)) {
            throw new EntityNotFoundException("Encuentro no encontrado");
        }
        encuentroRepository.deleteById(id);
    }

    private EncuentroResponse mapToResponse(Encuentro encuentro) {
        EncuentroResponse response = new EncuentroResponse();
        response.setId(encuentro.getId());
        response.setSubcategoriaId(encuentro.getSubcategoria().getSubcategoriaId());
        response.setSubcategoriaNombre(encuentro.getSubcategoria().getNombre());
        response.setTitulo(encuentro.getTitulo());
        response.setFechaHora(encuentro.getFechaHora());
        response.setEstadioLugar(encuentro.getEstadioLugar());
        response.setEstado(encuentro.getEstado());
        return response;
    }
}
