package pawkar.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.concurrent.atomic.AtomicInteger;
import pawkar.backend.entity.Equipo;
import pawkar.backend.entity.Subcategoria;
import pawkar.backend.entity.TablaPosicion;
import pawkar.backend.exception.ResourceNotFoundException;
import pawkar.backend.repository.EquipoRepository;
import pawkar.backend.repository.SubcategoriaRepository;
import pawkar.backend.repository.TablaPosicionRepository;
import pawkar.backend.request.TablaPosicionRequest;
import pawkar.backend.response.TablaPosicionResponse;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TablaPosicionService {

    private final TablaPosicionRepository tablaPosicionRepository;
    private final SubcategoriaRepository subcategoriaRepository;
    private final EquipoRepository equipoRepository;

    @Autowired
    public TablaPosicionService(TablaPosicionRepository tablaPosicionRepository,
                              SubcategoriaRepository subcategoriaRepository,
                              EquipoRepository equipoRepository) {
        this.tablaPosicionRepository = tablaPosicionRepository;
        this.subcategoriaRepository = subcategoriaRepository;
        this.equipoRepository = equipoRepository;
    }

    @Transactional(readOnly = true)
    public List<TablaPosicionResponse> getTablaPosicionBySubcategoria(Integer subcategoriaId) {
        // Get all positions for the subcategoria
        List<TablaPosicion> posiciones = tablaPosicionRepository
                .findBySubcategoriaSubcategoriaIdOrderByPuntosDesc(subcategoriaId);
        
        // Convert to response DTOs with position numbers
        AtomicInteger posicion = new AtomicInteger(1);
        return posiciones.stream()
                .map(tp -> {
                    TablaPosicionResponse response = new TablaPosicionResponse(
                            tp.getSubcategoria().getSubcategoriaId(),
                            tp.getEquipo().getEquipoId(),
                            tp.getEquipo().getNombre(),
                            tp.getPartidosJugados(),
                            tp.getVictorias(),
                            tp.getDerrotas(),
                            tp.getEmpates(),
                            tp.getPuntos(),
                            posicion.getAndIncrement()
                    );
                    return response;
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public TablaPosicion createOrUpdateTablaPosicion(TablaPosicionRequest request) {
        // Find subcategoria and equipo
        Subcategoria subcategoria = subcategoriaRepository.findById(request.getSubcategoriaId())
                .orElseThrow(() -> new ResourceNotFoundException("Subcategoría no encontrada con ID: " + request.getSubcategoriaId()));
        
        Equipo equipo = equipoRepository.findById(request.getEquipoId())
                .orElseThrow(() -> new ResourceNotFoundException("Equipo no encontrado con ID: " + request.getEquipoId()));
        
        // Check if position already exists
        Optional<TablaPosicion> existingPosicion = tablaPosicionRepository
                .findBySubcategoriaSubcategoriaIdAndEquipoEquipoId(
                        subcategoria.getSubcategoriaId(), 
                        equipo.getEquipoId()
                );
        
        TablaPosicion posicion;
        if (existingPosicion.isPresent()) {
            // Update existing position
            posicion = existingPosicion.get();
        } else {
            // Create new position
            posicion = new TablaPosicion();
            posicion.setSubcategoria(subcategoria);
            posicion.setEquipo(equipo);
        }
        
        // Update position data
        posicion.setPartidosJugados(request.getPartidosJugados());
        posicion.setVictorias(request.getVictorias());
        posicion.setDerrotas(request.getDerrotas());
        posicion.setEmpates(request.getEmpates());
        posicion.setPuntos(request.getPuntos());
        
        return tablaPosicionRepository.save(posicion);
    }

    @Transactional
    public void deleteTablaPosicion(Integer subcategoriaId, Integer equipoId) {
        // Check if position exists
        if (!tablaPosicionRepository.existsBySubcategoriaSubcategoriaIdAndEquipoEquipoId(subcategoriaId, equipoId)) {
            throw new ResourceNotFoundException("Posición no encontrada para la subcategoría ID: " + 
                    subcategoriaId + " y equipo ID: " + equipoId);
        }
        
        // Delete the position
        tablaPosicionRepository.deleteBySubcategoriaSubcategoriaIdAndEquipoEquipoId(subcategoriaId, equipoId);
    }

    @Transactional
    public void actualizarTablaPosicionDesdePartido(Integer subcategoriaId, Integer equipoLocalId, 
                                                  Integer equipoVisitanteId, Integer golesLocal, 
                                                  Integer golesVisitante, Integer puntosVictoria, 
                                                  Integer puntosEmpate) {
        // Update local team position
        actualizarEquipoEnTabla(subcategoriaId, equipoLocalId, golesLocal, golesVisitante, puntosVictoria, puntosEmpate);
        
        // Update visitor team position
        actualizarEquipoEnTabla(subcategoriaId, equipoVisitanteId, golesVisitante, golesLocal, puntosVictoria, puntosEmpate);
    }
    
    private void actualizarEquipoEnTabla(Integer subcategoriaId, Integer equipoId, 
                                       Integer golesAFavor, Integer golesEnContra,
                                       Integer puntosVictoria, Integer puntosEmpate) {
        // Find or create position for the team
        TablaPosicion posicion = tablaPosicionRepository
                .findBySubcategoriaSubcategoriaIdAndEquipoEquipoId(subcategoriaId, equipoId)
                .orElseGet(() -> {
                    TablaPosicion newPosicion = new TablaPosicion();
                    newPosicion.setSubcategoria(subcategoriaRepository.findById(subcategoriaId).orElseThrow());
                    newPosicion.setEquipo(equipoRepository.findById(equipoId).orElseThrow());
                    return newPosicion;
                });
        
        // Update statistics
        posicion.setPartidosJugados(posicion.getPartidosJugados() + 1);
        
        if (golesAFavor > golesEnContra) {
            posicion.setVictorias(posicion.getVictorias() + 1);
            posicion.setPuntos(posicion.getPuntos() + puntosVictoria);
        } else if (golesAFavor.equals(golesEnContra)) {
            posicion.setEmpates(posicion.getEmpates() + 1);
            posicion.setPuntos(posicion.getPuntos() + puntosEmpate);
        } else {
            posicion.setDerrotas(posicion.getDerrotas() + 1);
        }
        
        tablaPosicionRepository.save(posicion);
    }
}
