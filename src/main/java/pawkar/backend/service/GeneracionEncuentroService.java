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

    private EncuentroService encuentroService;

    @Autowired
    private EncuentroRepository encuentroRepository;
    
    @Autowired
    private ParticipacionEncuentroRepository participacionEncuentroRepository;

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
        return savedEncuentros.stream()
                .map(encuentro -> {
                    EncuentroResponse response = new EncuentroResponse();
                    response.setId(encuentro.getId().longValue());
                    response.setTitulo(encuentro.getTitulo());
                    response.setFechaHora(encuentro.getFechaHora());
                    response.setEstadioLugar(encuentro.getEstadioLugar());
                    response.setEstado(encuentro.getEstado());
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

        // Mapa para rastrear los horarios ocupados por estadio y fecha
        Map<String, Map<LocalDate, List<LocalTime>>> horariosOcupados = new HashMap<>();

        // Inicializar el mapa con los estadios
        List<String> estadios = List.of("Peguche", "Agato", "La Bolsa");
        for (String estadio : estadios) {
            horariosOcupados.put(estadio, new HashMap<>());
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

                    // La fecha y hora ya se establecieron en la lógica de asignación de estadio

                    // Estado inicial del encuentro
                    encuentro.setEstado("Programado");

                    // Asignar estadio según la distribución deseada
                    String estadio;
                    boolean horarioDisponible;
                    LocalTime horaPartido;

                    // Intentar asignar estadio y horario hasta encontrar uno disponible
                    do {
                        // Elegir estadio según la distribución
                        double random = Math.random();
                        if (random < 0.6) { // 60% de probabilidad
                            estadio = "Peguche";
                        } else if (random < 0.7) { // 30% de probabilidad
                            estadio = "Agato";
                        } else { // 10% de probabilidad
                            estadio = "La Bolsa";
                        }

                        // Verificar horarios disponibles para este estadio y fecha
                        Map<LocalDate, List<LocalTime>> fechasOcupadas = horariosOcupados.get(estadio);
                        List<LocalTime> horariosOcupadosHoy = fechasOcupadas.getOrDefault(fechaActual,
                                new ArrayList<>());

                        // Encontrar el primer horario disponible
                        horarioDisponible = false;
                        horaPartido = null;

                        for (LocalTime hora : horarios) {
                            if (!horariosOcupadosHoy.contains(hora)) {
                                horaPartido = hora;
                                horarioDisponible = true;
                                break;
                            }
                        }

                        // Si no hay horario disponible, pasar al siguiente día
                        if (!horarioDisponible) {
                            fechaActual = fechaActual.plusDays(1);
                        }

                    } while (!horarioDisponible);

                    // Asignar estadio
                    encuentro.setEstadioLugar(estadio);

                    // Registrar el horario como ocupado
                    Map<LocalDate, List<LocalTime>> fechasOcupadas = horariosOcupados.get(estadio);
                    List<LocalTime> horariosOcupadosHoy = fechasOcupadas.computeIfAbsent(fechaActual,
                            k -> new ArrayList<>());
                    horariosOcupadosHoy.add(horaPartido);

                    // Establecer fecha y hora del partido
                    LocalDateTime fechaHora = LocalDateTime.of(fechaActual, horaPartido);
                    encuentro.setFechaHora(fechaHora);

                    // Guardar el encuentro para obtener el ID generado
                    Encuentro savedEncuentro = encuentroRepository.save(encuentro);
                    
                    // Crear participación para el equipo local
                    ParticipacionEncuentro participacionLocal = new ParticipacionEncuentro();
                    ParticipacionEncuentroId participacionLocalId = new ParticipacionEncuentroId();
                    participacionLocalId.setEncuentroId(savedEncuentro.getId());
                    participacionLocalId.setEquipoId(local.getEquipoId());
                    participacionLocal.setId(participacionLocalId);
                    participacionLocal.setEncuentro(savedEncuentro);
                    participacionLocal.setEquipo(local);
                    participacionLocal.setEsLocal(true);
                    participacionLocal.setGolesPuntos(0);
                    
                    // Crear participación para el equipo visitante
                    ParticipacionEncuentro participacionVisitante = new ParticipacionEncuentro();
                    ParticipacionEncuentroId participacionVisitanteId = new ParticipacionEncuentroId();
                    participacionVisitanteId.setEncuentroId(savedEncuentro.getId());
                    participacionVisitanteId.setEquipoId(visitante.getEquipoId());
                    participacionVisitante.setId(participacionVisitanteId);
                    participacionVisitante.setEncuentro(savedEncuentro);
                    participacionVisitante.setEquipo(visitante);
                    participacionVisitante.setEsLocal(false);
                    participacionVisitante.setGolesPuntos(0);
                    
                    // Guardar las participaciones
                    participacionEncuentroRepository.save(participacionLocal);
                    participacionEncuentroRepository.save(participacionVisitante);
                    
                    // Agregar a la lista
                    encuentros.add(savedEncuentro);

                    // Verificar si todos los horarios del día están ocupados para todos los
                    // estadios
                    boolean todosEstadiosLlenos = true;
                    for (String est : estadios) {
                        Map<LocalDate, List<LocalTime>> fechasEstadio = horariosOcupados.get(est);
                        List<LocalTime> horariosOcupadosEstadio = fechasEstadio.getOrDefault(fechaActual,
                                new ArrayList<>());
                        if (horariosOcupadosEstadio.size() < horarios.size()) {
                            todosEstadiosLlenos = false;
                            break;
                        }
                    }

                    if (todosEstadiosLlenos) {
                        // Pasar al siguiente día si todos los estadios están llenos para el día actual
                        fechaActual = fechaActual.plusDays(1);
                    }
                }
            }
        }

        return encuentros;
    }

    private List<Encuentro> generarEncuentrosManuales(GeneracionEncuentroRequest request) {
        return request.getEncuentrosManuales().stream()
                .map(manual -> {
                    Equipo local = equipoRepository.findById(manual.getEquipoLocalId())
                            .orElseThrow(() -> new RuntimeException(
                                    "Equipo local no encontrado: " + manual.getEquipoLocalId()));

                    Equipo visitante = equipoRepository.findById(manual.getEquipoVisitanteId())
                            .orElseThrow(() -> new RuntimeException(
                                    "Equipo visitante no encontrado: " + manual.getEquipoVisitanteId()));

                    Encuentro encuentro = new Encuentro();
                    encuentro.setSubcategoria(local.getSubcategoria());
                    encuentro.setTitulo(String.format("%s vs %s", local.getNombre(), visitante.getNombre()));
                    encuentro.setFechaHora(LocalDateTime.of(manual.getFecha(), manual.getHora()));
                    encuentro.setEstadioLugar("Por definir");
                    encuentro.setEstado("Pendiente");

                    return encuentro;
                })
                .collect(Collectors.toList());
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
}
