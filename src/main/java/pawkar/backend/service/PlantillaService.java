package pawkar.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import pawkar.backend.request.BulkPlantillaRequest;
import pawkar.backend.request.PlantillaRequest;
import pawkar.backend.response.PlantillaResponse;
import pawkar.backend.entity.*;
import pawkar.backend.exception.ResourceNotFoundException;
import pawkar.backend.repository.EquipoRepository;
import pawkar.backend.repository.JugadorRepository;
import pawkar.backend.repository.PlantillaRepository;
import pawkar.backend.repository.RoleRepository;
import pawkar.backend.repository.SancionRepository;
import pawkar.backend.exception.DuplicatePlantillaException;

@Service
@RequiredArgsConstructor
public class PlantillaService {

    private final PlantillaRepository plantillaRepository;
    private final EquipoRepository equipoRepository;
    private final JugadorRepository jugadorRepository;
    private final RoleRepository roleRepository;
    private final SancionRepository sancionRepository;

    @Transactional
    public List<PlantillaResponse> crearPlantillasBulk(BulkPlantillaRequest request) {
        List<PlantillaResponse> respuestas = new ArrayList<>();
        List<String> errores = new ArrayList<>();
        Map<Integer, Set<Integer>> equipoJerseyMap = new HashMap<>();

        // Primera pasada: validar duplicados en la solicitud
        for (PlantillaRequest plantillaRequest : request.getJugadores()) {
            if (plantillaRequest.getNumeroCamiseta() != null) {
                int equipoId = plantillaRequest.getEquipoId();
                int numeroCamiseta = plantillaRequest.getNumeroCamiseta();

                // Verificar duplicados en la misma solicitud
                if (equipoJerseyMap.containsKey(equipoId) &&
                        equipoJerseyMap.get(equipoId).contains(numeroCamiseta)) {
                    errores.add("Error en jugador ID " + plantillaRequest.getJugadorId() +
                            ": El número de camiseta " + numeroCamiseta +
                            " está duplicado en la solicitud para el equipo con ID: " + equipoId);
                    continue;
                }

                // Verificar si el número ya existe en la base de datos para este equipo
                try {
                    if (existsByEquipoIdAndNumeroCamiseta(equipoId, numeroCamiseta)) {
                        errores.add("Error en jugador ID " + plantillaRequest.getJugadorId() +
                                ": El número de camiseta " + numeroCamiseta +
                                " ya está en uso en el equipo con ID: " + equipoId);
                        continue;
                    }
                } catch (Exception e) {
                    errores.add("Error validando jugador ID " + plantillaRequest.getJugadorId() +
                            ": " + e.getMessage());
                    continue;
                }

                // Si llegamos aquí, la validación fue exitosa
                equipoJerseyMap.computeIfAbsent(equipoId, k -> new HashSet<>()).add(numeroCamiseta);
            }
        }

        // Si hay errores de validación, lanzar excepción con todos los errores
        if (!errores.isEmpty()) {
            throw new IllegalStateException("Errores de validación: " + String.join("; ", errores));
        }

        // Segunda pasada: crear las plantillas
        for (PlantillaRequest plantillaRequest : request.getJugadores()) {
            try {
                PlantillaResponse response = crearPlantilla(plantillaRequest);
                respuestas.add(response);
            } catch (Exception e) {
                // Si hay un error, agregar a la lista de errores pero continuar con los demás
                errores.add("Error al crear plantilla para jugador ID " +
                        plantillaRequest.getJugadorId() + ": " + e.getMessage());
            }
        }

        // Si hubo errores en la creación, lanzar excepción con los errores
        if (!errores.isEmpty()) {
            throw new IllegalStateException("Algunas plantillas no pudieron ser creadas: " +
                    String.join("; ", errores));
        }

        return respuestas;
    }

    @Transactional(readOnly = true)
    public boolean existsByEquipoIdAndNumeroCamiseta(Integer equipoId, Integer numeroCamiseta) {
        return plantillaRepository.existsByEquipo_EquipoIdAndNumeroCamiseta(equipoId, numeroCamiseta);
    }

    @Transactional
    public PlantillaResponse crearPlantilla(PlantillaRequest request) {
        // Verificar si el número de camiseta ya está en uso en este equipo
        if (request.getNumeroCamiseta() != null) {
            if (existsByEquipoIdAndNumeroCamiseta(request.getEquipoId(), request.getNumeroCamiseta())) {
                throw new DuplicatePlantillaException("El número de camiseta " + request.getNumeroCamiseta()
                        + " ya está siendo utilizado en este equipo");
            }
        }

        // Verificar si ya existe una plantilla para este equipo y jugador
        if (plantillaRepository.existsByEquipo_EquipoIdAndJugador_Id(
                request.getEquipoId(), request.getJugadorId())) {
            throw new DuplicatePlantillaException("Ya existe una plantilla para este equipo y jugador");
        }

        // Obtener las entidades relacionadas
        Equipo equipo = equipoRepository.findById(request.getEquipoId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Equipo no encontrado con ID: " + request.getEquipoId()));

        Jugador jugador = jugadorRepository.findById(request.getJugadorId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Jugador no encontrado con ID: " + request.getJugadorId()));

        // Verificar si el jugador está activo
        if (!jugador.isEstado()) {
            throw new IllegalStateException(
                    "No se puede agregar al jugador con ID: " + request.getJugadorId() + " porque está inactivo");
        }

        Role rol = roleRepository.findById(request.getRolId())
                .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado con ID: " + request.getRolId()));

        // Crear y guardar la nueva plantilla
        Plantilla plantilla = new Plantilla();
        plantilla.setEquipo(equipo);
        plantilla.setJugador(jugador);
        plantilla.setRol(rol);
        plantilla.setNumeroCamiseta(request.getNumeroCamiseta());

        Plantilla plantillaGuardada = plantillaRepository.save(plantilla);
        return mapToPlantillaResponse(plantillaGuardada);
    }

    @Transactional(readOnly = true)
    public List<PlantillaResponse> obtenerTodasLasPlantillas() {
        return plantillaRepository.findAll().stream()
                .sorted(Comparator.comparing(p -> p.getJugador().getApellido()))
                .map(this::mapToPlantillaResponse)
                .collect(java.util.stream.Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PlantillaResponse obtenerPlantilla(Integer equipoId, Integer jugadorId) {
        Plantilla plantilla = plantillaRepository.findById(new PlantillaId(equipoId, jugadorId))
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Plantilla no encontrada para equipoId: %d y jugadorId: %d", equipoId,
                                jugadorId)));
        return mapToPlantillaResponse(plantilla);
    }

    @Transactional
    public void eliminarPlantilla(Integer equipoId, Integer jugadorId) {
        PlantillaId id = new PlantillaId(equipoId, jugadorId);
        if (!plantillaRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    String.format("Plantilla no encontrada para equipoId: %d y jugadorId: %d", equipoId, jugadorId));
        }
        plantillaRepository.deleteById(id);
    }

    private PlantillaResponse mapToPlantillaResponse(Plantilla plantilla) {
        // Obtener sanciones activas del jugador
        List<Sancion> sanciones = sancionRepository
                .findByJugadorId(plantilla.getJugador().getId().longValue());

        // Mapear las sanciones a SancionInfo
        List<PlantillaResponse.SancionInfo> sancionesInfo = sanciones.stream()
                .map(sancion -> PlantillaResponse.SancionInfo.builder()
                        .sancionId(sancion.getId())
                        .tipoSancion(sancion.getTipoSancion())
                        .motivo(sancion.getMotivo())
                        .detalleSancion(sancion.getDetalleSancion())
                        .fechaRegistro(sancion.getFechaRegistro().toString())
                        .build())
                .collect(java.util.stream.Collectors.toList());

        return PlantillaResponse.builder()
                .equipoId(plantilla.getEquipo().getEquipoId())
                .equipoNombre(plantilla.getEquipo().getNombre())
                .jugadorId(plantilla.getJugador().getId())
                .jugadorNombreCompleto(plantilla.getJugador().getNombre() + " " + plantilla.getJugador().getApellido())
                .numeroCamiseta(plantilla.getNumeroCamiseta())
                .rolId(plantilla.getRol().getId())
                .rolNombre(plantilla.getRol().getName())
                .tieneSancion(!sanciones.isEmpty())
                .sanciones(sancionesInfo)
                .build();
    }
    
    @Transactional(readOnly = true)
    public List<PlantillaResponse> obtenerPlantillaPorEquipo(Integer equipoId) {
        return plantillaRepository.findByEquipo_EquipoId(equipoId).stream()
                .sorted(Comparator.comparing(Plantilla::getNumeroCamiseta))
                .map(this::mapToPlantillaResponse)
                .collect(java.util.stream.Collectors.toList());
    }
}
