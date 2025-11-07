package pawkar.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pawkar.backend.entity.Equipo;
import pawkar.backend.entity.Subcategoria;
import pawkar.backend.entity.TablaPosicion;
import pawkar.backend.exception.ResourceNotFoundException;
import pawkar.backend.repository.EquipoRepository;
import pawkar.backend.repository.SubcategoriaRepository;
import pawkar.backend.repository.TablaPosicionRepository;
import pawkar.backend.request.TablaPosicionRequest;
import pawkar.backend.request.TablaPosicionSearchRequest;
import pawkar.backend.response.TablaPosicionResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import jakarta.persistence.criteria.Predicate;

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
    public TablaPosicionResponse getTablaPosicion(Integer subcategoriaId, Integer equipoId) {
        // Get the position for the specific team and category
        TablaPosicion posicion = tablaPosicionRepository
                .findBySubcategoriaSubcategoriaIdAndEquipoEquipoId(subcategoriaId, equipoId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No se encontró la posición para la subcategoría ID: " + subcategoriaId + 
                        " y equipo ID: " + equipoId));
        
        // Get all positions for the subcategory to calculate ranking
        List<TablaPosicion> posiciones = tablaPosicionRepository
                .findBySubcategoriaSubcategoriaIdOrderByPuntosDesc(subcategoriaId);
        
        // Find the team's position in the ranking
        int posicionEnTabla = 1;
        for (TablaPosicion p : posiciones) {
            if (p.getEquipo().getEquipoId().equals(equipoId)) {
                break;
            }
            posicionEnTabla++;
        }
        
        // Create and return the response DTO
        return new TablaPosicionResponse(
                posicion.getSubcategoria().getSubcategoriaId(),
                posicion.getEquipo().getEquipoId(),
                posicion.getEquipo().getNombre(),
                posicion.getPartidosJugados(),
                posicion.getVictorias(),
                posicion.getDerrotas(),
                posicion.getEmpates(),
                posicion.getGolesAFavor(),
                posicion.getGolesEnContra(),
                posicion.getGolesAFavor() - posicion.getGolesEnContra(),
                posicion.getPuntos(),
                posicionEnTabla
        );
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
                            tp.getGolesAFavor(),
                            tp.getGolesEnContra(),
                            tp.getGolesAFavor() - tp.getGolesEnContra(), // diferenciaGoles
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

    @Transactional(readOnly = true)
    public List<TablaPosicionResponse> searchTablaPosicion(TablaPosicionSearchRequest searchRequest) {
        // Obtener todas las posiciones que coincidan con los filtros
        List<TablaPosicion> posiciones;
        
        // Verificar si hay algún filtro activo
        boolean hasFilters = searchRequest.getSubcategoriaId() != null ||
                           searchRequest.getCategoriaId() != null ||
                           searchRequest.getEquipoId() != null ||
                           searchRequest.getSerieId() != null ||
                           (searchRequest.getNombreEquipo() != null && !searchRequest.getNombreEquipo().isEmpty());
        
        if (!hasFilters) {
            // Si no hay filtros, obtener todos los registros
            posiciones = tablaPosicionRepository.findAll();
        } else {
            // Si hay filtros, aplicarlos
            posiciones = tablaPosicionRepository.findAll((root, query, criteriaBuilder) -> {
                List<Predicate> predicates = new ArrayList<>();

                // Filtrar por subcategoría si está presente
                if (searchRequest.getSubcategoriaId() != null) {
                    predicates.add(criteriaBuilder.equal(
                            root.get("subcategoria").get("subcategoriaId"),
                            searchRequest.getSubcategoriaId()));
                }

                // Filtrar por categoría si está presente
                if (searchRequest.getCategoriaId() != null) {
                    predicates.add(criteriaBuilder.equal(
                            root.get("subcategoria").get("categoria").get("categoriaId"),
                            searchRequest.getCategoriaId()));
                }

                // Filtrar por equipo si está presente
                if (searchRequest.getEquipoId() != null) {
                    predicates.add(criteriaBuilder.equal(
                            root.get("equipo").get("equipoId"),
                            searchRequest.getEquipoId()));
                }

                // Filtrar por nombre de equipo (búsqueda por coincidencia parcial)
                if (searchRequest.getNombreEquipo() != null && !searchRequest.getNombreEquipo().isEmpty()) {
                    predicates.add(criteriaBuilder.like(
                            criteriaBuilder.lower(root.get("equipo").get("nombre")),
                            "%" + searchRequest.getNombreEquipo().toLowerCase() + "%"));
                }

                // Filtrar por serie si está presente
                if (searchRequest.getSerieId() != null) {
                    // Asumiendo que hay una relación entre Equipo y Serie
                    predicates.add(criteriaBuilder.equal(
                            root.get("equipo").get("serie").get("serieId"),
                            searchRequest.getSerieId()));
                }

                // Ordenar por puntos (descendente) y diferencia de goles (descendente)
                query.orderBy(
                        criteriaBuilder.desc(root.get("puntos")),
                        criteriaBuilder.desc(root.get("diferenciaGoles")),
                        criteriaBuilder.desc(root.get("golesAFavor")),
                        criteriaBuilder.asc(root.get("equipo").get("nombre")));

                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            });
        }
        
        // Convertir a DTOs de respuesta con posición
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
                            tp.getGolesAFavor(),
                            tp.getGolesEnContra(),
                            tp.getDiferenciaGoles(),
                            tp.getPuntos(),
                            posicion.getAndIncrement());

                    // Agregar información adicional si es necesario
                    if (tp.getEquipo().getSerie() != null) {
                        response.setSerieId(tp.getEquipo().getSerie().getSerieId());
                        response.setSerieNombre(tp.getEquipo().getSerie().getNombreSerie());
                    }

                    if (tp.getSubcategoria() != null && tp.getSubcategoria().getCategoria() != null) {
                        response.setCategoriaId(tp.getSubcategoria().getCategoria().getCategoriaId());
                        response.setCategoriaNombre(tp.getSubcategoria().getCategoria().getNombre());
                    }   

                    return response;
                })
                .collect(Collectors.toList());
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
            Integer golesVisitante, String estadoPartido) {
        // Obtener la subcategoría para determinar el deporte
        Subcategoria subcategoria = subcategoriaRepository.findById(subcategoriaId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Subcategoría no encontrada con ID: " + subcategoriaId));

        // Obtener configuración de puntos según el deporte
        Map<String, Integer> configuracionPuntos = obtenerConfiguracionPuntos(subcategoria.getNombre());

        // Actualizar equipos según el resultado
        if ("FINALIZADO".equals(estadoPartido)) {
            actualizarEquipoEnTabla(subcategoriaId, equipoLocalId, golesLocal, golesVisitante, configuracionPuntos);
            actualizarEquipoEnTabla(subcategoriaId, equipoVisitanteId, golesVisitante, golesLocal, configuracionPuntos);
        }
        // Para partidos suspendidos o aplazados, no se actualizan los puntos
    }

    private Map<String, Integer> obtenerConfiguracionPuntos(String nombreDeporte) {
        Map<String, Integer> configuracion = new HashMap<>();

        // Configuración por defecto (puedes ajustar según tus necesidades)
        String deporte = nombreDeporte.toUpperCase();
        if (deporte.contains("FUTBOL") || deporte.contains("FÚTBOL")) {
            configuracion.put("PUNTOS_VICTORIA", 3);
            configuracion.put("PUNTOS_EMPATE", 1);
            configuracion.put("PUNTOS_DERROTA", 0);
        } else if (deporte.contains("BALONCESTO")) {
            configuracion.put("PUNTOS_VICTORIA", 2);
            configuracion.put("PUNTOS_EMPATE", 1); // En baloncesto puede haber empate en algunos casos
            configuracion.put("PUNTOS_DERROTA", 0);
        } else if (deporte.contains("VOLEIBOL")) {
            configuracion.put("PUNTOS_VICTORIA", 2); // 2 puntos por victoria
            configuracion.put("PUNTOS_EMPATE", 1); // 1 punto por set ganado (puedes ajustar)
            configuracion.put("PUNTOS_DERROTA", 0);
        } else {
            // Configuración por defecto para otros deportes
            configuracion.put("PUNTOS_VICTORIA", 1);
            configuracion.put("PUNTOS_EMPATE", 1);
            configuracion.put("PUNTOS_DERROTA", 0);
        }
        
        return configuracion;
    }
    
    private void actualizarEquipoEnTabla(Integer subcategoriaId, Integer equipoId, 
                                       Integer golesAFavor, Integer golesEnContra,
            Map<String, Integer> configuracionPuntos) {
        // Encontrar o crear la posición del equipo en la tabla
        TablaPosicion posicion = tablaPosicionRepository
                .findBySubcategoriaSubcategoriaIdAndEquipoEquipoId(subcategoriaId, equipoId)
                .orElseGet(() -> {
                    TablaPosicion nuevaPosicion = new TablaPosicion();
                    nuevaPosicion.setSubcategoria(subcategoriaRepository.findById(subcategoriaId)
                            .orElseThrow(() -> new ResourceNotFoundException("Subcategoría no encontrada")));
                    nuevaPosicion.setEquipo(equipoRepository.findById(equipoId)
                            .orElseThrow(() -> new ResourceNotFoundException("Equipo no encontrado")));
                    return nuevaPosicion;
                });
        
        // Actualizar estadísticas
        posicion.setPartidosJugados(posicion.getPartidosJugados() + 1);
        
        // Actualizar goles
        if (posicion.getGolesAFavor() == null)
            posicion.setGolesAFavor(0);
        if (posicion.getGolesEnContra() == null)
            posicion.setGolesEnContra(0);

        posicion.setGolesAFavor(posicion.getGolesAFavor() + golesAFavor);
        posicion.setGolesEnContra(posicion.getGolesEnContra() + golesEnContra);
        posicion.setDiferenciaGoles(posicion.getGolesAFavor() - posicion.getGolesEnContra());

        // Determinar resultado y actualizar puntos
        if (golesAFavor > golesEnContra) {
            posicion.setVictorias(posicion.getVictorias() + 1);
            posicion.setPuntos(posicion.getPuntos() + configuracionPuntos.get("PUNTOS_VICTORIA"));
        } else if (golesAFavor.equals(golesEnContra)) {
            posicion.setEmpates(posicion.getEmpates() + 1);
            posicion.setPuntos(posicion.getPuntos() + configuracionPuntos.get("PUNTOS_EMPATE"));
        } else {
            posicion.setDerrotas(posicion.getDerrotas() + 1);
            posicion.setPuntos(posicion.getPuntos() + configuracionPuntos.get("PUNTOS_DERROTA"));
        }
        
        tablaPosicionRepository.save(posicion);
    }
}
