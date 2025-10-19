package pawkar.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pawkar.backend.request.BulkEquipoRequest;
import pawkar.backend.request.EquipoRequest;
import pawkar.backend.response.EquipoResponse;
import pawkar.backend.entity.Equipo;
import pawkar.backend.entity.Serie;
import pawkar.backend.entity.Subcategoria;
import pawkar.backend.exception.BadRequestException;
import pawkar.backend.exception.ResourceNotFoundException;
import pawkar.backend.repository.EquipoRepository;
import pawkar.backend.repository.SerieRepository;
import pawkar.backend.repository.SubcategoriaRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EquipoService {

    @Autowired
    private EquipoRepository equipoRepository;
    
    @Autowired
    private SubcategoriaRepository subcategoriaRepository;
    
    @Autowired
    private SerieRepository serieRepository;

    @Transactional(readOnly = true)
    public List<EquipoResponse> obtenerEquiposPorSerie(Integer serieId) {
        return equipoRepository.findBySerie_SerieId(serieId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<EquipoResponse> obtenerEquiposPorSubcategoria(Integer subcategoriaId) {
        return equipoRepository.findBySubcategoriaId(subcategoriaId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EquipoResponse obtenerEquipoPorId(Integer id) {
        Equipo equipo = equipoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Equipo no encontrado con ID: " + id));
        return mapToResponse(equipo);
    }

    @Transactional
    public EquipoResponse crearEquipo(EquipoRequest request) {
        // Verificar si ya existe un equipo con el mismo nombre en la misma serie
        if (equipoRepository.existsByNombreAndSerieId(request.getNombre(), request.getSerieId())) {
            throw new BadRequestException("Ya existe un equipo con el mismo nombre en esta serie");
        }
        
        // Obtener la subcategoría
        Subcategoria subcategoria = subcategoriaRepository.findById(request.getSubcategoriaId())
                .orElseThrow(() -> new ResourceNotFoundException("Subcategoría no encontrada con ID: " + request.getSubcategoriaId()));
        
        // Obtener la serie
        Serie serie = serieRepository.findById(request.getSerieId())
                .orElseThrow(() -> new ResourceNotFoundException("Serie no encontrada con ID: " + request.getSerieId()));
        
        // Crear y guardar el equipo
        Equipo equipo = new Equipo();
        equipo.setSubcategoria(subcategoria);
        equipo.setSerie(serie);
        equipo.setNombre(request.getNombre());
        equipo.setFundacion(request.getFundacion());
        
        Equipo equipoGuardado = equipoRepository.save(equipo);
        return mapToResponse(equipoGuardado);
    }

    @Transactional
    public EquipoResponse actualizarEquipo(Integer id, EquipoRequest request) {
        // Verificar si el equipo existe
        Equipo equipo = equipoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Equipo no encontrado con ID: " + id));
        
        // Verificar si ya existe otro equipo con el mismo nombre en la misma serie
        if (equipoRepository.existsByNombreAndSerieIdExcludingId(
                request.getNombre(), request.getSerieId(), id)) {
            throw new BadRequestException("Ya existe otro equipo con el mismo nombre en esta serie");
        }
        
        // Obtener la subcategoría
        Subcategoria subcategoria = subcategoriaRepository.findById(request.getSubcategoriaId())
                .orElseThrow(() -> new ResourceNotFoundException("Subcategoría no encontrada con ID: " + request.getSubcategoriaId()));
        
        // Obtener la serie
        Serie serie = serieRepository.findById(request.getSerieId())
                .orElseThrow(() -> new ResourceNotFoundException("Serie no encontrada con ID: " + request.getSerieId()));
        
        // Actualizar el equipo
        equipo.setSubcategoria(subcategoria);
        equipo.setSerie(serie);
        equipo.setNombre(request.getNombre());
        equipo.setFundacion(request.getFundacion());
        
        Equipo equipoActualizado = equipoRepository.save(equipo);
        return mapToResponse(equipoActualizado);
    }

    @Transactional
    public List<EquipoResponse> crearEquiposEnLote(BulkEquipoRequest request) {
        // Verificar que la lista de equipos no esté vacía
        if (request.getEquipos() == null || request.getEquipos().isEmpty()) {
            throw new BadRequestException("La lista de equipos no puede estar vacía");
        }

        // Verificar nombres duplicados en la misma serie dentro de la solicitud
        long uniqueEquiposCount = request.getEquipos().stream()
                .map(equipo -> equipo.getSerieId() + "_" + equipo.getNombre())
                .distinct()
                .count();

        if (uniqueEquiposCount != request.getEquipos().size()) {
            throw new BadRequestException(
                    "No se permiten equipos duplicados en la misma serie dentro de la misma solicitud");
        }

        // Verificar si ya existen equipos con los mismos nombres en las series
        // correspondientes
        for (EquipoRequest equipoReq : request.getEquipos()) {
            if (equipoRepository.existsByNombreAndSerieId(equipoReq.getNombre(), equipoReq.getSerieId())) {
                throw new BadRequestException("Ya existe un equipo con el nombre '" +
                        equipoReq.getNombre() + "' en la serie con ID: " + equipoReq.getSerieId());
            }
        }

        // Crear y guardar los equipos
        return request.getEquipos().stream()
                .map(this::crearEquipo)
                .collect(Collectors.toList());
    }

    @Transactional
    public void eliminarEquipo(Integer id) {
        if (!equipoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Equipo no encontrado con ID: " + id);
        }
        equipoRepository.deleteById(id);
    }
    
    @Transactional(readOnly = true)
    public boolean existenEquiposRegistrados() {
        return equipoRepository.count() > 0;
    }

    private EquipoResponse mapToResponse(Equipo equipo) {
        EquipoResponse response = new EquipoResponse();
        response.setEquipoId(equipo.getEquipoId());
        response.setSubcategoriaId(equipo.getSubcategoria().getSubcategoriaId());
        response.setSubcategoriaNombre(equipo.getSubcategoria().getNombre());
        response.setSerieId(equipo.getSerie().getSerieId());
        response.setSerieNombre(equipo.getSerie().getNombreSerie());
        response.setNombre(equipo.getNombre());
        response.setFundacion(equipo.getFundacion());
        return response;
    }
}
