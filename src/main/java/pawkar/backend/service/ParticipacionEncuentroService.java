package pawkar.backend.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pawkar.backend.entity.*;
import pawkar.backend.repository.EncuentroRepository;
import pawkar.backend.repository.EquipoRepository;
import pawkar.backend.repository.ParticipacionEncuentroRepository;
import pawkar.backend.request.ParticipacionEncuentroRequest;
import pawkar.backend.response.ParticipacionEncuentroResponse;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ParticipacionEncuentroService {

    private final ParticipacionEncuentroRepository participacionEncuentroRepository;
    private final EncuentroRepository encuentroRepository;
    private final EquipoRepository equipoRepository;

    @Transactional
    public ParticipacionEncuentroResponse createParticipacion(ParticipacionEncuentroRequest request) {
        // Verificar si ya existe una participación para este encuentro y equipo
        if (participacionEncuentroRepository.existsByEncuentroIdAndEquipoId(
                request.getEncuentroId(), request.getEquipoId())) {
            throw new IllegalStateException("Ya existe una participación para este encuentro y equipo");
        }

        // Verificar que el encuentro existe
        Encuentro encuentro = encuentroRepository.findById(request.getEncuentroId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Encuentro no encontrado con ID: " + request.getEncuentroId()));

        // Verificar que el equipo existe
        Equipo equipo = equipoRepository.findById(request.getEquipoId())
                .orElseThrow(
                        () -> new EntityNotFoundException("Equipo no encontrado con ID: " + request.getEquipoId()));

        // Crear y guardar la participación
        ParticipacionEncuentro participacion = new ParticipacionEncuentro();
        participacion.setId(new ParticipacionEncuentro.ParticipacionEncuentroId(
                request.getEncuentroId(),
                request.getEquipoId()));
        participacion.setEncuentro(encuentro);
        participacion.setEquipo(equipo);
        participacion.setEsLocal(request.getEsLocal() != null ? request.getEsLocal() : false);
        participacion.setGolesPuntos(request.getGolesPuntos() != null ? request.getGolesPuntos() : 0);

        ParticipacionEncuentro saved = participacionEncuentroRepository.save(participacion);
        return mapToResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<ParticipacionEncuentroResponse> getParticipacionesByEncuentro(Integer encuentroId) {
        return participacionEncuentroRepository.findByEncuentroId(encuentroId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ParticipacionEncuentroResponse> getParticipacionesByEquipo(Integer equipoId) {
        return participacionEncuentroRepository.findByEquipoId(equipoId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public ParticipacionEncuentroResponse updateGolesPuntos(
            Integer encuentroId, Integer equipoId, Integer golesPuntos) {
        ParticipacionEncuentro.ParticipacionEncuentroId id = new ParticipacionEncuentro.ParticipacionEncuentroId(
                encuentroId, equipoId);

        ParticipacionEncuentro participacion = participacionEncuentroRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Participación no encontrada para encuentro: " + encuentroId + " y equipo: " + equipoId));

        participacion.setGolesPuntos(golesPuntos);
        ParticipacionEncuentro updated = participacionEncuentroRepository.save(participacion);
        return mapToResponse(updated);
    }

    @Transactional
    public void deleteParticipacion(Integer encuentroId, Integer equipoId) {
        ParticipacionEncuentro.ParticipacionEncuentroId id = new ParticipacionEncuentro.ParticipacionEncuentroId(
                encuentroId, equipoId);

        if (!participacionEncuentroRepository.existsById(id)) {
            throw new EntityNotFoundException(
                    "Participación no encontrada para encuentro: " + encuentroId + " y equipo: " + equipoId);
        }

        participacionEncuentroRepository.deleteById(id);
    }

    private ParticipacionEncuentroResponse mapToResponse(ParticipacionEncuentro participacion) {
        return new ParticipacionEncuentroResponse(
                participacion.getEncuentro().getId(),
                participacion.getEquipo().getEquipoId(),
                participacion.getEquipo().getNombre(),
                participacion.getEsLocal(),
                participacion.getGolesPuntos());
    }
}
