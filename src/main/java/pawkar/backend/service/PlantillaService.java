package pawkar.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import pawkar.backend.request.BulkPlantillaRequest;
import pawkar.backend.request.PlantillaRequest;
import pawkar.backend.response.PlantillaResponse;
import pawkar.backend.entity.*;
import pawkar.backend.exception.ResourceNotFoundException;
import pawkar.backend.repository.EquipoRepository;
import pawkar.backend.repository.JugadorRepository;
import pawkar.backend.repository.PlantillaRepository;
import pawkar.backend.repository.RoleRepository;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlantillaService {

    private final PlantillaRepository plantillaRepository;
    private final EquipoRepository equipoRepository;
    private final JugadorRepository jugadorRepository;
    private final RoleRepository roleRepository;

    @Transactional
    public List<PlantillaResponse> crearPlantillasBulk(BulkPlantillaRequest request) {
        List<PlantillaResponse> respuestas = new ArrayList<>();
        
        for (PlantillaRequest plantillaRequest : request.getJugadores()) {
            try {
                PlantillaResponse response = crearPlantilla(plantillaRequest);
                respuestas.add(response);
            } catch (Exception e) {
                // Si hay un error con algún jugador, continuamos con los demás
                // Podrías también implementar lógica de rollback si lo prefieres
                e.printStackTrace();
            }
        }
        
        return respuestas;
    }

    @Transactional
    public PlantillaResponse crearPlantilla(PlantillaRequest request) {
        // Verificar si ya existe una plantilla para este equipo y jugador
        if (plantillaRepository.existsByEquipo_EquipoIdAndJugador_Id(
                request.getEquipoId(), request.getJugadorId())) {
            throw new IllegalStateException("Ya existe una plantilla para este equipo y jugador");
        }

        // Obtener las entidades relacionadas
        Equipo equipo = equipoRepository.findById(request.getEquipoId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Equipo no encontrado con ID: " + request.getEquipoId()));

        Jugador jugador = jugadorRepository.findById(request.getJugadorId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Jugador no encontrado con ID: " + request.getJugadorId()));

        Role rol = roleRepository.findById(request.getRolId())
                .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado con ID: " + request.getRolId()));

        // Crear y guardar la nueva plantilla
        Plantilla plantilla = new Plantilla();
        plantilla.setEquipo(equipo);
        plantilla.setJugador(jugador);
        plantilla.setRol(rol);
        plantilla.setNumeroCamiseta(request.getNumeroCamiseta());

        Plantilla plantillaGuardada = plantillaRepository.save(plantilla);
        return mapToResponse(plantillaGuardada);
    }

    @Transactional(readOnly = true)
    public List<PlantillaResponse> obtenerTodasLasPlantillas() {
        return plantillaRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PlantillaResponse obtenerPlantilla(Integer equipoId, Integer jugadorId) {
        PlantillaId id = new PlantillaId(equipoId, jugadorId);
        Plantilla plantilla = plantillaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Plantilla no encontrada para equipoId: %d y jugadorId: %d", equipoId,
                                jugadorId)));
        return mapToResponse(plantilla);
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

    private PlantillaResponse mapToResponse(Plantilla plantilla) {
        return new PlantillaResponse(
                plantilla.getEquipo().getEquipoId(),
                plantilla.getEquipo().getNombre(),
                plantilla.getJugador().getId(),
                plantilla.getJugador().getNombre() + " " + plantilla.getJugador().getApellido(),
                plantilla.getNumeroCamiseta(),
                plantilla.getRol().getId(),
                plantilla.getRol().getName().name()
        );
    }
    
    @Transactional(readOnly = true)
    public List<PlantillaResponse> obtenerPlantillaPorEquipo(Integer equipoId) {
        return plantillaRepository.findByEquipo_EquipoId(equipoId).stream()
                .sorted(Comparator.comparing(Plantilla::getNumeroCamiseta))
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
}
