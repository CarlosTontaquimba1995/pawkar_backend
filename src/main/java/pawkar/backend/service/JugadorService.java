package pawkar.backend.service;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pawkar.backend.entity.Equipo;
import pawkar.backend.entity.Plantilla;
import pawkar.backend.entity.Role;
import pawkar.backend.exception.ResourceNotFoundException;
import pawkar.backend.repository.EquipoRepository;
import pawkar.backend.repository.PlantillaRepository;
import pawkar.backend.repository.RoleRepository;
import pawkar.backend.request.JugadorRequest;
import pawkar.backend.response.JugadorResponse;
import pawkar.backend.request.JugadorBulkRequest;
import pawkar.backend.entity.Jugador;
import pawkar.backend.exception.BadRequestException;
import pawkar.backend.repository.JugadorRepository;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class JugadorService {

    private final JugadorRepository jugadorRepository;
    private final EquipoRepository equipoRepository;
    private final RoleRepository roleRepository;
    private final PlantillaRepository plantillaRepository;

    @Autowired
    public JugadorService(JugadorRepository jugadorRepository,
            EquipoRepository equipoRepository,
            RoleRepository roleRepository,
            PlantillaRepository plantillaRepository) {
        this.jugadorRepository = jugadorRepository;
        this.equipoRepository = equipoRepository;
        this.roleRepository = roleRepository;
        this.plantillaRepository = plantillaRepository;
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
    
    @Transactional(readOnly = true)
    public boolean existsByDocumentoIdentidad(String documentoIdentidad) {
        return jugadorRepository.existsByDocumentoIdentidadAndEstadoTrue(documentoIdentidad);
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

        // Verificar si el número de camiseta ya está en uso en el equipo
        if (request.getNumeroCamiseta() != null && request.getEquipoId() != null) {
            if (plantillaRepository.existsByEquipo_EquipoIdAndNumeroCamiseta(
                    request.getEquipoId(), request.getNumeroCamiseta())) {
                throw new BadRequestException("El número de camiseta " + request.getNumeroCamiseta()
                        + " ya está siendo utilizado en este equipo");
            }
        }

        // Crear y guardar el jugador
        Jugador jugador = new Jugador();
        jugador.setNombre(request.getNombre());
        jugador.setApellido(request.getApellido());
        jugador.setFechaNacimiento(request.getFechaNacimiento());
        jugador.setDocumentoIdentidad(request.getDocumentoIdentidad());
        jugador.setEstado(true);

        jugador = jugadorRepository.save(jugador);

        // Crear la plantilla si se proporcionaron los datos necesarios
        if (request.getEquipoId() != null && request.getRolId() != null) {
            Equipo equipo = equipoRepository.findById(request.getEquipoId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Equipo no encontrado con ID: " + request.getEquipoId()));

            Role rol = roleRepository.findById(request.getRolId().longValue())
                    .orElseThrow(
                            () -> new ResourceNotFoundException("Rol no encontrado con ID: " + request.getRolId()));

            Plantilla plantilla = new Plantilla();
            plantilla.setEquipo(equipo);
            plantilla.setJugador(jugador);
            plantilla.setNumeroCamiseta(request.getNumeroCamiseta());
            plantilla.setRol(rol);

            plantillaRepository.save(plantilla);
        }

        return mapToResponse(jugador);
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

    @Transactional(rollbackFor = Exception.class)
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

        // Validate jersey numbers within the same team in the request
        Map<Integer, Set<Integer>> equipoJerseyMap = new HashMap<>();
        for (JugadorRequest jugadorRequest : request.getJugadores()) {
            if (jugadorRequest.getEquipoId() != null && jugadorRequest.getNumeroCamiseta() != null) {
                equipoJerseyMap
                        .computeIfAbsent(jugadorRequest.getEquipoId(), k -> new HashSet<>())
                        .add(jugadorRequest.getNumeroCamiseta());
            }
        }

        // Check for duplicate jersey numbers in the same team within the request
        for (Map.Entry<Integer, Set<Integer>> entry : equipoJerseyMap.entrySet()) {
            if (entry.getValue().size() < request.getJugadores().stream()
                    .filter(j -> j.getEquipoId() != null && j.getEquipoId().equals(entry.getKey())
                            && j.getNumeroCamiseta() != null)
                    .count()) {
                throw new BadRequestException("No se permiten números de camiseta duplicados en el mismo equipo");
            }
        }

        // Check for existing jersey numbers in the database
        for (JugadorRequest jugadorRequest : request.getJugadores()) {
            if (jugadorRequest.getEquipoId() != null && jugadorRequest.getNumeroCamiseta() != null) {
                if (plantillaRepository.existsByEquipo_EquipoIdAndNumeroCamiseta(
                        jugadorRequest.getEquipoId(), jugadorRequest.getNumeroCamiseta())) {
                    // Get team name for better error message
                    String nombreEquipo = equipoRepository.findById(jugadorRequest.getEquipoId())
                            .map(Equipo::getNombre)
                            .orElse("");
                    throw new BadRequestException("El número de camiseta " + jugadorRequest.getNumeroCamiseta()
                            + " ya está siendo utilizado en el equipo: " + nombreEquipo);
                }
            }
        }

        // Create and save all players with their Plantilla records
        return request.getJugadores().stream()
                .map(jugadorRequest -> {
                    // Create and save player
                    Jugador jugador = new Jugador();
                    jugador.setNombre(jugadorRequest.getNombre());
                    jugador.setApellido(jugadorRequest.getApellido());
                    jugador.setFechaNacimiento(jugadorRequest.getFechaNacimiento());
                    jugador.setDocumentoIdentidad(jugadorRequest.getDocumentoIdentidad());
                    jugador.setEstado(true);
                    jugador = jugadorRepository.save(jugador);

                    // Create and save Plantilla if team and role are provided
                    if (jugadorRequest.getEquipoId() != null && jugadorRequest.getRolId() != null) {
                        Equipo equipo = equipoRepository.findById(jugadorRequest.getEquipoId())
                                .orElseThrow(() -> new ResourceNotFoundException(
                                        "Equipo no encontrado con ID: " + jugadorRequest.getEquipoId()));

                        Role rol = roleRepository.findById(jugadorRequest.getRolId().longValue())
                                .orElseThrow(() -> new ResourceNotFoundException(
                                        "Rol no encontrado con ID: " + jugadorRequest.getRolId()));

                        Plantilla plantilla = new Plantilla();
                        plantilla.setEquipo(equipo);
                        plantilla.setJugador(jugador);
                        plantilla.setNumeroCamiseta(jugadorRequest.getNumeroCamiseta());
                        plantilla.setRol(rol);

                        plantillaRepository.save(plantilla);
                    }

                    return jugador;
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
