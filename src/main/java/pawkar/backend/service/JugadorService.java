package pawkar.backend.service;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pawkar.backend.request.JugadorRequest;
import pawkar.backend.response.JugadorResponse;
import pawkar.backend.request.JugadorBulkRequest;
import pawkar.backend.entity.Jugador;
import pawkar.backend.exception.BadRequestException;
import pawkar.backend.exception.ResourceNotFoundException;
import pawkar.backend.repository.JugadorRepository;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JugadorService {

    private final JugadorRepository jugadorRepository;

    @Autowired
    public JugadorService(JugadorRepository jugadorRepository) {
        this.jugadorRepository = jugadorRepository;
    }

    @Transactional(readOnly = true)
    public Page<JugadorResponse> obtenerTodosLosJugadores(Pageable pageable) {
        return jugadorRepository.findByEstado(true, pageable)
                .map(this::mapToResponse);
    }
    
    @Transactional(readOnly = true)
    public Page<JugadorResponse> buscarJugadoresPorNombreOApellido(String busqueda, Pageable pageable) {
        return jugadorRepository.buscarPorNombreOApellidoYEstado(busqueda, true, pageable)
                .map(this::mapToResponse);
    }
    
    @Transactional
    public void softDeleteJugador(Integer id) {
        if (!jugadorRepository.existsByIdAndEstadoTrue(id)) {
            throw new ResourceNotFoundException("Jugador no encontrado con ID: " + id);
        }
        jugadorRepository.softDelete(id);
    }

    @Transactional(readOnly = true)
    public JugadorResponse obtenerJugadorPorId(Integer id) {
        Jugador jugador = jugadorRepository.findByIdAndEstadoTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Jugador no encontrado con ID: " + id));
        return mapToResponse(jugador);
    }

    @Transactional
    public JugadorResponse crearJugador(JugadorRequest request) {
        if (jugadorRepository.existsByDocumentoIdentidadAndEstadoTrue(request.getDocumentoIdentidad())) {
            throw new BadRequestException(
                    "Ya existe un jugador con el documento de identidad: " + request.getDocumentoIdentidad());
        }

        Jugador jugador = new Jugador();
        jugador.setNombre(request.getNombre());
        jugador.setApellido(request.getApellido());
        jugador.setFechaNacimiento(request.getFechaNacimiento());
        jugador.setDocumentoIdentidad(request.getDocumentoIdentidad());

        // Note: equipo relationship is not stored in the database
        // The equipo field is marked as @Transient in the entity

        Jugador jugadorGuardado = jugadorRepository.save(jugador);
        return mapToResponse(jugadorGuardado);
    }

    @Transactional
    public JugadorResponse actualizarJugador(Integer id, JugadorRequest request) {
        Jugador jugador = jugadorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Jugador no encontrado con ID: " + id));

        // Verificar si el documento de identidad ya existe para otro jugador
        if (!jugador.getDocumentoIdentidad().equals(request.getDocumentoIdentidad()) &&
                jugadorRepository.existsByDocumentoIdentidad(request.getDocumentoIdentidad())) {
            throw new BadRequestException(
                    "Ya existe un jugador con el documento de identidad: " + request.getDocumentoIdentidad());
        }

        jugador.setNombre(request.getNombre());
        jugador.setApellido(request.getApellido());
        jugador.setFechaNacimiento(request.getFechaNacimiento());
        jugador.setDocumentoIdentidad(request.getDocumentoIdentidad());

        // Note: equipo relationship is not stored in the database
        // The equipo field is marked as @Transient in the entity

        Jugador jugadorActualizado = jugadorRepository.save(jugador);
        return mapToResponse(jugadorActualizado);
    }

    @Transactional
    public void eliminarJugador(Integer id) {
        softDeleteJugador(id);
    }

    @Transactional
    public List<JugadorResponse> crearJugadoresEnLote(JugadorBulkRequest request) {
        // Check for duplicate document numbers in the request
        List<String> documentos = request.getJugadores().stream()
                .map(JugadorRequest::getDocumentoIdentidad)
                .collect(Collectors.toList());

        if (documentos.size() != new HashSet<>(documentos).size()) {
            throw new BadRequestException("No se permiten documentos de identidad duplicados en la misma solicitud");
        }

        // Check for existing documents in the database
        List<String> documentosExistentes = jugadorRepository.findByDocumentoIdentidadIn(documentos)
                .stream()
                .map(Jugador::getDocumentoIdentidad)
                .collect(Collectors.toList());

        if (!documentosExistentes.isEmpty()) {
            throw new BadRequestException("Ya existen jugadores con los siguientes documentos de identidad: " +
                    String.join(", ", documentosExistentes));
        }

        // Create and save all players
        return request.getJugadores().stream()
                .map(jugadorRequest -> {
                    Jugador jugador = new Jugador();
                    jugador.setNombre(jugadorRequest.getNombre());
                    jugador.setApellido(jugadorRequest.getApellido());
                    jugador.setFechaNacimiento(jugadorRequest.getFechaNacimiento());
                    jugador.setDocumentoIdentidad(jugadorRequest.getDocumentoIdentidad());

                    // Note: equipo relationship is not stored in the database
                    // The equipo field is marked as @Transient in the entity

                    return jugadorRepository.save(jugador);
                })
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private JugadorResponse mapToResponse(Jugador jugador) {
        if (jugador == null) {
            return null;
        }

        JugadorResponse response = new JugadorResponse();
        response.setId(jugador.getId());
        response.setNombre(jugador.getNombre());
        response.setApellido(jugador.getApellido());
        response.setFechaNacimiento(jugador.getFechaNacimiento());
        response.setDocumentoIdentidad(jugador.getDocumentoIdentidad());

        return response;
    }
}
