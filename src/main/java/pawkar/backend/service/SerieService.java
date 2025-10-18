package pawkar.backend.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pawkar.backend.dto.SerieRequest;
import pawkar.backend.dto.SerieResponse;
import pawkar.backend.entity.Serie;
import pawkar.backend.entity.Subcategoria;
import pawkar.backend.exception.ResourceNotFoundException;
import pawkar.backend.repository.SerieRepository;
import pawkar.backend.repository.SubcategoriaRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SerieService {

    private final SerieRepository serieRepository;
    private final SubcategoriaRepository subcategoriaRepository;

    @Autowired
    public SerieService(SerieRepository serieRepository, SubcategoriaRepository subcategoriaRepository) {
        this.serieRepository = serieRepository;
        this.subcategoriaRepository = subcategoriaRepository;
    }

    @Transactional
    public SerieResponse crearSerie(SerieRequest request) {
        // Verificar si ya existe una serie con el mismo nombre en la misma subcategoría
        if (serieRepository.findBySubcategoriaAndNombreSerie(
                request.getSubcategoriaId(), request.getNombreSerie()).isPresent()) {
            throw new IllegalArgumentException("Ya existe una serie con este nombre en la subcategoría especificada");
        }

        // Obtener la subcategoría
        Subcategoria subcategoria = subcategoriaRepository.findById(request.getSubcategoriaId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Subcategoría no encontrada con ID: " + request.getSubcategoriaId()));

        // Crear y guardar la nueva serie
        Serie serie = new Serie();
        serie.setSubcategoria(subcategoria);
        serie.setNombreSerie(request.getNombreSerie());

        Serie serieGuardada = serieRepository.save(serie);
        return mapToResponse(serieGuardada);
    }

    public List<SerieResponse> obtenerSeriesPorSubcategoria(Integer subcategoriaId) {
        return serieRepository.findBySubcategoria_SubcategoriaId(subcategoriaId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public SerieResponse obtenerSeriePorId(Integer id) {
        Serie serie = serieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Serie no encontrada con ID: " + id));
        return mapToResponse(serie);
    }

    @Transactional
    public SerieResponse actualizarSerie(Integer id, SerieRequest request) {
        Serie serie = serieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Serie no encontrada con ID: " + id));

        // Verificar si ya existe otra serie con el mismo nombre en la misma
        // subcategoría
        if (serieRepository.existsBySubcategoriaAndNombreSerieExcludingId(
                request.getSubcategoriaId(), request.getNombreSerie(), id)) {
            throw new IllegalArgumentException("Ya existe otra serie con este nombre en la subcategoría especificada");
        }

        // Actualizar solo si la subcategoría es diferente
        if (!serie.getSubcategoria().getSubcategoriaId().equals(request.getSubcategoriaId())) {
            Subcategoria subcategoria = subcategoriaRepository.findById(request.getSubcategoriaId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Subcategoría no encontrada con ID: " + request.getSubcategoriaId()));
            serie.setSubcategoria(subcategoria);
        }

        serie.setNombreSerie(request.getNombreSerie());

        return mapToResponse(serieRepository.save(serie));
    }

    @Transactional
    public void eliminarSerie(Integer id) {
        if (!serieRepository.existsById(id)) {
            throw new ResourceNotFoundException("Serie no encontrada con ID: " + id);
        }
        serieRepository.deleteById(id);
    }

    private SerieResponse mapToResponse(Serie serie) {
        return new SerieResponse(
                serie.getSerieId(),
                serie.getSubcategoria().getSubcategoriaId(),
                serie.getSubcategoria().getNombre(),
                serie.getNombreSerie());
    }
}
