package pawkar.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pawkar.backend.dto.SancionRequest;
import pawkar.backend.dto.SancionResponse;
import pawkar.backend.entity.Encuentro;
import pawkar.backend.entity.Jugador;
import pawkar.backend.entity.Sancion;
import pawkar.backend.exception.ResourceNotFoundException;
import pawkar.backend.repository.EncuentroRepository;
import pawkar.backend.repository.JugadorRepository;
import pawkar.backend.repository.SancionRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SancionService {

    @Autowired
    private SancionRepository sancionRepository;

    @Autowired
    private JugadorRepository jugadorRepository;

    @Autowired
    private EncuentroRepository encuentroRepository;

    @Transactional(readOnly = true)
    public List<SancionResponse> getAllSanciones() {
        return sancionRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public SancionResponse getSancionById(Long id) {
        Sancion sancion = sancionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sancion no encontrada con id: " + id));
        return convertToResponse(sancion);
    }

    @Transactional
    public SancionResponse createSancion(SancionRequest sancionRequest) {
        Sancion sancion = new Sancion();
        
        Jugador jugador = jugadorRepository.findById(sancionRequest.getJugadorId().intValue())
                .orElseThrow(() -> new ResourceNotFoundException("Jugador no encontrado con id: " + sancionRequest.getJugadorId()));
        
        sancion.setJugador(jugador);
        
        if (sancionRequest.getEncuentroId() != null) {
            Encuentro encuentro = encuentroRepository.findById(sancionRequest.getEncuentroId())
                    .orElseThrow(() -> new ResourceNotFoundException("Encuentro no encontrado con id: " + sancionRequest.getEncuentroId()));
            sancion.setEncuentro(encuentro);
        }
        
        sancion.setTipoSancion(sancionRequest.getTipoSancion());
        sancion.setMotivo(sancionRequest.getMotivo());
        sancion.setDetalleSancion(sancionRequest.getDetalleSancion());
        
        if (sancionRequest.getFechaRegistro() != null) {
            sancion.setFechaRegistro(sancionRequest.getFechaRegistro());
        }
        
        Sancion savedSancion = sancionRepository.save(sancion);
        return convertToResponse(savedSancion);
    }

    @Transactional
    public SancionResponse updateSancion(Long id, SancionRequest sancionRequest) {
        Sancion sancion = sancionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sancion no encontrada con id: " + id));
        
        Jugador jugador = jugadorRepository.findById(sancionRequest.getJugadorId().intValue())
                .orElseThrow(() -> new ResourceNotFoundException("Jugador no encontrado con id: " + sancionRequest.getJugadorId()));
        
        sancion.setJugador(jugador);
        
        if (sancionRequest.getEncuentroId() != null) {
            Encuentro encuentro = encuentroRepository.findById(sancionRequest.getEncuentroId())
                    .orElseThrow(() -> new ResourceNotFoundException("Encuentro no encontrado con id: " + sancionRequest.getEncuentroId()));
            sancion.setEncuentro(encuentro);
        } else {
            sancion.setEncuentro(null);
        }
        
        sancion.setTipoSancion(sancionRequest.getTipoSancion());
        sancion.setMotivo(sancionRequest.getMotivo());
        sancion.setDetalleSancion(sancionRequest.getDetalleSancion());
        
        if (sancionRequest.getFechaRegistro() != null) {
            sancion.setFechaRegistro(sancionRequest.getFechaRegistro());
        }
        
        Sancion updatedSancion = sancionRepository.save(sancion);
        return convertToResponse(updatedSancion);
    }

    @Transactional
    public void deleteSancion(Long id) {
        if (!sancionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Sancion no encontrada con id: " + id);
        }
        sancionRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<SancionResponse> getSancionesByJugadorId(Long jugadorId) {
        return sancionRepository.findByJugadorId(jugadorId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<SancionResponse> getSancionesByEncuentroId(Integer encuentroId) {
        return sancionRepository.findByEncuentroId(encuentroId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<SancionResponse> getSancionesByTipo(String tipoSancion) {
        return sancionRepository.findByTipoSancion(tipoSancion).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    private SancionResponse convertToResponse(Sancion sancion) {
        SancionResponse response = new SancionResponse();
        response.setId(sancion.getId());
        response.setJugadorId(sancion.getJugador().getId().longValue());
        response.setJugadorNombre(sancion.getJugador().getNombre() + " " + sancion.getJugador().getApellido());
        
        if (sancion.getEncuentro() != null) {
            response.setEncuentroId(sancion.getEncuentro().getId());
            response.setEncuentroTitulo(sancion.getEncuentro().getTitulo());
        }
        
        response.setTipoSancion(sancion.getTipoSancion());
        response.setMotivo(sancion.getMotivo());
        response.setDetalleSancion(sancion.getDetalleSancion());
        response.setFechaRegistro(sancion.getFechaRegistro());
        
        return response;
    }
}
