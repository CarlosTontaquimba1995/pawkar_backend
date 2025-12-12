package pawkar.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pawkar.backend.entity.*;
import pawkar.backend.repository.*;
import pawkar.backend.request.*;
import pawkar.backend.response.EncuentroResponse;
import pawkar.backend.entity.ParticipacionEncuentro.ParticipacionEncuentroId;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class GeneracionEncuentroService {

    @Autowired
    private SerieRepository serieRepository;

    @Autowired
    private EquipoRepository equipoRepository;

    @Autowired
    private SubcategoriaRepository subcategoriaRepository;

    @Autowired
    private EncuentroRepository encuentroRepository;
    
    @Autowired
    private ParticipacionEncuentroRepository participacionEncuentroRepository;
    
    @Autowired
    private EstadioRepository estadioRepository;

    @Transactional
    public List<EncuentroResponse> generarEncuentros(GeneracionEncuentroRequest request) {
        // Obtener la subcategoría
        Subcategoria subcategoria = subcategoriaRepository.findById(request.getSubcategoriaId())
                .orElseThrow(() -> new RuntimeException("Subcategoría no encontrada"));

        // Obtener todas las series de la subcategoría
        List<Serie> series = serieRepository.findBySubcategoria_SubcategoriaId(subcategoria.getSubcategoriaId());

        List<Encuentro> encuentros = new ArrayList<>();

        // Generar encuentros según el tipo de generación
        switch (request.getTipoGeneracion()) {
            case TODOS_CONTRA_TODOS:
                encuentros = generarTodosContraTodos(series, request);
                break;
            case SELECCION_MANUAL:
                if (request.getEncuentrosManuales() == null || request.getEncuentrosManuales().isEmpty()) {
                    throw new RuntimeException("Se requiere la lista de encuentros manuales");
                }
                encuentros = generarEncuentrosManuales(request);
                break;
            // Agregar otros casos según sea necesario
            default:
                throw new UnsupportedOperationException("Tipo de generación no soportado");
        }

        // Guardar los encuentros generados
        List<Encuentro> savedEncuentros = encuentroRepository.saveAll(encuentros);

        // Convertir a respuestas
        // In the generarEncuentros method, update the mapping part:
        return savedEncuentros.stream()
                .map(encuentro -> {
                    EncuentroResponse response = new EncuentroResponse();
                    response.setId(encuentro.getId().longValue());
                    response.setTitulo(encuentro.getTitulo());
                    response.setFechaHora(encuentro.getFechaHora());
                    response.setEstadioLugar(encuentro.getEstadio().getNombre());
                    response.setEstado(encuentro.getEstado());

            // Add team information to response
            if (encuentro.getEquipoLocal() != null) {
                response.setEquipoLocalId(encuentro.getEquipoLocal().getEquipoId());
                response.setEquipoLocalNombre(encuentro.getEquipoLocal().getNombre());
            }

            if (encuentro.getEquipoVisitante() != null) {
                response.setEquipoVisitanteId(encuentro.getEquipoVisitante().getEquipoId());
                response.setEquipoVisitanteNombre(encuentro.getEquipoVisitante().getNombre());
            }

            if (encuentro.getSubcategoria() != null) {
                response.setSubcategoriaId(encuentro.getSubcategoria().getSubcategoriaId().longValue());
                response.setSubcategoriaNombre(encuentro.getSubcategoria().getNombre());
            }
            return response;
        })
        .collect(Collectors.toList());
    }

    private List<Encuentro> generarTodosContraTodos(List<Serie> series, GeneracionEncuentroRequest request) {
        List<Encuentro> encuentros = new ArrayList<>();

        // Obtener todos los estadios activos
        List<Estadio> estadios = estadioRepository.findAll();
        if (estadios.isEmpty()) {
            throw new IllegalStateException("No hay estadios disponibles en el sistema");
        }

    // Mapa para rastrear los horarios ocupados por estadio y fecha
    Map<Long, Map<LocalDate, List<LocalTime>>> horariosOcupados = new HashMap<>();

    // Inicializar el mapa con los estadios
    for (Estadio estadio : estadios) {
        horariosOcupados.put(estadio.getId().longValue(), new HashMap<>());
    }

    // Horarios posibles para los partidos
    List<LocalTime> horarios = List.of(
            LocalTime.of(8, 0), // 8:00 AM
            LocalTime.of(10, 0), // 10:00 AM
            LocalTime.of(12, 0), // 12:00 PM
            LocalTime.of(14, 0), // 2:00 PM
            LocalTime.of(16, 0) // 4:00 PM
    );

    // Fecha actual para la programación
    LocalDate fechaActual = request.getFechaInicio();

    for (Serie serie : series) {
        // Obtener equipos de la serie
        List<Equipo> equipos = equipoRepository.findBySerie_SerieId(serie.getSerieId());

        // Generar combinaciones de partidos
        for (int i = 0; i < equipos.size(); i++) {
            for (int j = i + 1; j < equipos.size(); j++) {
                Equipo local = equipos.get(i);
                Equipo visitante = equipos.get(j);

                // Crear el encuentro
                Encuentro encuentro = new Encuentro();
                encuentro.setSubcategoria(serie.getSubcategoria());
                encuentro.setTitulo(String.format("%s vs %s", local.getNombre(), visitante.getNombre()));
                encuentro.setEstado("PROGRAMADO");

                // Asignar estadio y horario
                boolean horarioAsignado = false;
                while (!horarioAsignado) {
                    // Intentar asignar un estadio
                    for (Estadio estadio : estadios) {
                        Map<LocalDate, List<LocalTime>> fechasOcupadas = horariosOcupados
                                .get(estadio.getId().longValue());
                        List<LocalTime> horariosOcupadosHoy = fechasOcupadas.computeIfAbsent(fechaActual,
                                k -> new ArrayList<>());

                        // Buscar horario disponible
                        for (LocalTime hora : horarios) {
                            if (!horariosOcupadosHoy.contains(hora)) {
                                // Asignar estadio
                                encuentro.setEstadio(estadio);

                                // Establecer fecha y hora del partido
                                LocalDateTime fechaHora = LocalDateTime.of(fechaActual, hora);
                                encuentro.setFechaHora(fechaHora);

                                // Marcar horario como ocupado
                                horariosOcupadosHoy.add(hora);
                                horarioAsignado = true;
                                break;
                            }
                        }
                        if (horarioAsignado)
                            break;
                    }

                    // Si no se pudo asignar horario, pasar al siguiente día
                    if (!horarioAsignado) {
                        fechaActual = fechaActual.plusDays(1);
                        // Reiniciar horarios ocupados para el nuevo día
                        for (Map<LocalDate, List<LocalTime>> fechas : horariosOcupados.values()) {
                            fechas.remove(fechaActual);
                        }
                    }
                }

                // Guardar el encuentro
                Encuentro savedEncuentro = encuentroRepository.save(encuentro);

                // Crear y guardar participaciones (local y visitante)
                // ... (código existente para crear participaciones)

                encuentros.add(savedEncuentro);
            }
        }
    }

    return encuentros;
}

private boolean tieneConflictoHorario(Equipo equipo, LocalDateTime fechaHora, Integer encuentroId) {
    // Buscar si el equipo ya tiene un encuentro programado en el mismo horario
    List<Encuentro> encuentrosExistentes = encuentroRepository.findByEquipoAndFechaHora(equipo, fechaHora);

    // Si hay encuentros programados, verificar si es el mismo encuentro (para
    // actualizaciones)
    if (!encuentrosExistentes.isEmpty()) {
        if (encuentroId != null) {
            // Si estamos actualizando, ignorar el encuentro actual
            return encuentrosExistentes.stream()
                    .anyMatch(e -> !e.getId().equals(encuentroId));
        }
        return true;
    }
    return false;
}

    private List<Encuentro> generarEncuentrosManuales(GeneracionEncuentroRequest request) {
        List<Encuentro> encuentros = new ArrayList<>();
        Subcategoria subcategoria = subcategoriaRepository.findById(request.getSubcategoriaId())
                .orElseThrow(() -> new RuntimeException("Subcategoría no encontrada"));

        // Procesar cada encuentro manual
        for (GeneracionEncuentroRequest.EncuentroManualRequest manualRequest : request.getEncuentrosManuales()) {
            // Verificar que los equipos existan
            Equipo local = equipoRepository.findById(manualRequest.getEquipoLocalId())
                    .orElseThrow(() -> new RuntimeException("Equipo local no encontrado"));
            
            Equipo visitante = equipoRepository.findById(manualRequest.getEquipoVisitanteId())
                    .orElseThrow(() -> new RuntimeException("Equipo visitante no encontrado"));

            // Verificar que no sean el mismo equipo
            if (local.getEquipoId().equals(visitante.getEquipoId())) {
                throw new RuntimeException(
                        "El equipo local no puede ser el mismo que el visitante: " + local.getNombre());
            }

            // Buscar el estadio por nombre
            Estadio estadio = estadioRepository.findById(manualRequest.getEstadioId())
                    .orElseThrow(() -> new RuntimeException("Estadio no encontrado: " + manualRequest.getEstadioId()));

            LocalDateTime fechaHora = LocalDateTime.of(manualRequest.getFecha(), manualRequest.getHora());

            // Verificar conflictos de horario para ambos equipos
            if (tieneConflictoHorario(local, fechaHora, null)) {
                throw new RuntimeException(String.format(
                        "El equipo %s ya tiene un partido programado para %s a las %s",
                        local.getNombre(),
                        fechaHora.toLocalDate(),
                        fechaHora.toLocalTime()));
            }

            if (tieneConflictoHorario(visitante, fechaHora, null)) {
                throw new RuntimeException(String.format(
                        "El equipo %s ya tiene un partido programado para %s a las %s",
                        visitante.getNombre(),
                        fechaHora.toLocalDate(),
                        fechaHora.toLocalTime()));
            }

            // Crear el encuentro
            Encuentro encuentro = new Encuentro();
            encuentro.setSubcategoria(subcategoria);
            encuentro.setTitulo(String.format("%s vs %s", local.getNombre(), visitante.getNombre()));
            encuentro.setEstado("PROGRAMADO");
            encuentro.setEstadio(estadio);
            encuentro.setFechaHora(fechaHora);
            encuentro.setEquipoLocal(local);
            encuentro.setEquipoVisitante(visitante);

            // Guardar el encuentro
            Encuentro savedEncuentro = encuentroRepository.save(encuentro);
            
            // Crear y guardar participaciones
            saveParticipacion(savedEncuentro, local, true);
            saveParticipacion(savedEncuentro, visitante, false);
            
            encuentros.add(savedEncuentro);
        }
    
        return encuentros;
}
    
    /**
     * Saves multiple Encuentro entities in a batch operation
     * 
     * @param encuentros List of Encuentro entities to save
     * @return List of saved Encuentro entities
     */
    @Transactional
    public List<Encuentro> saveAllEncuentros(List<Encuentro> encuentros) {
        if (encuentros == null || encuentros.isEmpty()) {
            return Collections.emptyList();
        }
        return encuentroRepository.saveAll(encuentros);
    }
    
    private void saveParticipacion(Encuentro encuentro, Equipo equipo, boolean esLocal) {
        ParticipacionEncuentro participacion = new ParticipacionEncuentro();
        ParticipacionEncuentroId participacionId = new ParticipacionEncuentroId();
        participacionId.setEncuentroId(encuentro.getId());
        participacionId.setEquipoId(equipo.getEquipoId());
        participacion.setId(participacionId);
        participacion.setEncuentro(encuentro);
        participacion.setEquipo(equipo);
        participacion.setEsLocal(esLocal);
        participacion.setGolesPuntos(0);
        
        participacionEncuentroRepository.save(participacion);
    }
}
