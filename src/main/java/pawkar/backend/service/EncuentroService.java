package pawkar.backend.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pawkar.backend.request.EncuentroRequest;
import pawkar.backend.request.EncuentroResponse;
import pawkar.backend.request.EncuentroSearchRequest;
import pawkar.backend.entity.Encuentro;
import pawkar.backend.entity.Subcategoria;
import pawkar.backend.repository.EncuentroRepository;
import pawkar.backend.repository.SubcategoriaRepository;

import java.time.LocalDateTime;
import java.util.Collections;
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
    public EncuentroResponse getEncuentroById(Integer id) {
        Encuentro encuentro = encuentroRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Encuentro no encontrado"));
        return mapToResponse(encuentro);
    }

    @Transactional(readOnly = true)
    public List<EncuentroResponse> getEncuentrosBySubcategoria(Integer subcategoriaId) {
        return encuentroRepository.findBySubcategoria_SubcategoriaId(subcategoriaId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public Page<EncuentroResponse> searchEncuentros(
            Integer subcategoriaId,
            LocalDateTime fechaInicio,
            LocalDateTime fechaFin,
            String estadioLugar,
            String estado,
            Integer equipoId,
            Pageable pageable) {

        Page<Encuentro> encuentrosPage = encuentroRepository.searchEncuentros(
                subcategoriaId,
                fechaInicio,
                fechaFin,
                estadioLugar,
                estado,
                equipoId,
                pageable);

        return encuentrosPage.map(this::mapToResponse);
    }

    public List<EncuentroResponse> searchEncuentrosWithoutPagination(
            Integer subcategoriaId,
            LocalDateTime fechaInicio,
            LocalDateTime fechaFin,
            String estadioLugar,
            String estado,
            Integer equipoId) {

        List<Encuentro> encuentros = encuentroRepository.searchEncuentrosWithoutPagination(
                subcategoriaId,
                fechaInicio,
                fechaFin,
                estadioLugar,
                estado,
                equipoId);

        return encuentros.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Método de compatibilidad para mantener la funcionalidad existente
    public Page<EncuentroResponse> searchEncuentros(EncuentroSearchRequest searchRequest) {
        LocalDateTime fechaInicio = searchRequest.getFechaInicio() != null
                ? searchRequest.getFechaInicio().atStartOfDay()
                : null;

        LocalDateTime fechaFin = searchRequest.getFechaFin() != null
                ? searchRequest.getFechaFin().atTime(23, 59, 59)
                : null;

        return searchEncuentros(
            searchRequest.getSubcategoriaId(),
            fechaInicio,
            fechaFin,
            searchRequest.getEstadioLugar(),
            searchRequest.getEstado(),
            searchRequest.getEquipoId(),
            searchRequest.toPageable()
        );
    }

    @Transactional
    public EncuentroResponse updateEncuentro(Integer id, EncuentroRequest request) {
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
    public void deleteEncuentro(Integer id) {
        if (!encuentroRepository.existsById(id)) {
            throw new EntityNotFoundException("Encuentro no encontrado");
        }
        encuentroRepository.deleteById(id);
    }

    private Encuentro mapToEntity(EncuentroRequest request) {
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

        return encuentro;
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

    /**
     * Saves multiple Encuentro entities in a batch operation
     * 
     * @param requests List of EncuentroRequest objects to be saved
     * @return List of saved EncuentroResponse objects
     */
    @Transactional
    public List<EncuentroResponse> saveAllEncuentros(List<EncuentroRequest> requests) {
        if (requests == null || requests.isEmpty()) {
            return Collections.emptyList();
        }

        // Convert requests to entities and save them
        List<Encuentro> encuentros = requests.stream()
                .map(this::mapToEntity)
                .collect(Collectors.toList());

        List<Encuentro> savedEncuentros = encuentroRepository.saveAll(encuentros);

        // Convert saved entities to responses
        return savedEncuentros.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
}
