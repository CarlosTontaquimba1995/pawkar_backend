package pawkar.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pawkar.backend.entity.Ubicacion;
import pawkar.backend.exception.BadRequestException;
import pawkar.backend.exception.ResourceNotFoundException;
import pawkar.backend.repository.UbicacionRepository;
import pawkar.backend.request.BulkUbicacionRequest;
import pawkar.backend.request.UbicacionRequest;
import pawkar.backend.response.UbicacionResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UbicacionService {

    private final UbicacionRepository ubicacionRepository;

    @Autowired
    public UbicacionService(UbicacionRepository ubicacionRepository) {
        this.ubicacionRepository = ubicacionRepository;
    }

    @Transactional
    public Ubicacion crearUbicacion(UbicacionRequest request) {
        // Verificar si ya existe una ubicación con el mismo nemonico
        if (request.getNemonico() != null && !request.getNemonico().trim().isEmpty() &&
                ubicacionRepository.existsByNemonicoIgnoreCase(request.getNemonico())) {
            throw new BadRequestException("Ya existe una ubicación con el nemonico: " + request.getNemonico());
        }

        Ubicacion ubicacion = new Ubicacion();
        ubicacion.setDescripcion(request.getDescripcion());
        ubicacion.setNemonico(request.getNemonico());
        ubicacion.setEstado(request.getEstado() != null ? request.getEstado() : true);
        ubicacion.setLatitud(request.getLatitud());
        ubicacion.setLongitud(request.getLongitud());
        ubicacion.setCreatedAt(LocalDateTime.now());
        ubicacion.setUpdatedAt(LocalDateTime.now());

        return ubicacionRepository.save(ubicacion);
    }

    @Transactional
    public List<Ubicacion> crearUbicacionesBulk(BulkUbicacionRequest request) {
        if (request.getUbicaciones() == null || request.getUbicaciones().isEmpty()) {
            throw new BadRequestException("La lista de ubicaciones no puede estar vacía");
        }

        // Verificar nemonicos duplicados en la solicitud
        long uniqueNemonicoCount = request.getUbicaciones().stream()
                .map(ubicacion -> ubicacion.getNemonico() != null ? ubicacion.getNemonico().toLowerCase() : "")
                .distinct()
                .count();

        if (uniqueNemonicoCount < request.getUbicaciones().size()) {
            throw new BadRequestException("No se permiten nemónicos duplicados en la misma solicitud");
        }

        // Verificar nemonicos existentes en la base de datos
        List<String> ubicacionesDuplicadas = request.getUbicaciones().stream()
                .filter(ubicacion -> ubicacion.getNemonico() != null &&
                        !ubicacion.getNemonico().trim().isEmpty() &&
                        ubicacionRepository.existsByNemonicoIgnoreCase(ubicacion.getNemonico()))
                .map(UbicacionRequest::getNemonico)
                .collect(Collectors.toList());

        if (!ubicacionesDuplicadas.isEmpty()) {
            throw new BadRequestException("Los siguientes nemónicos ya existen: " +
                    String.join(", ", ubicacionesDuplicadas));
        }

        // Crear y guardar las ubicaciones
        List<Ubicacion> ubicaciones = request.getUbicaciones().stream()
                .map(ubicacionRequest -> {
                    Ubicacion ubicacion = new Ubicacion();
                    ubicacion.setDescripcion(ubicacionRequest.getDescripcion());
                    ubicacion.setNemonico(ubicacionRequest.getNemonico());
                    ubicacion.setEstado(ubicacionRequest.getEstado() != null ? ubicacionRequest.getEstado() : true);
                    ubicacion.setLatitud(ubicacionRequest.getLatitud());
                    ubicacion.setLongitud(ubicacionRequest.getLongitud());
                    ubicacion.setCreatedAt(LocalDateTime.now());
                    ubicacion.setUpdatedAt(LocalDateTime.now());
                    return ubicacion;
                })
                .collect(Collectors.toList());

        return ubicacionRepository.saveAll(ubicaciones);
    }

    public List<Ubicacion> listarUbicaciones() {
        return ubicacionRepository.findAll();
    }

    public Ubicacion obtenerUbicacionPorId(Long id) {
        return ubicacionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ubicación no encontrada con ID: " + id));
    }

    public Ubicacion obtenerUbicacionPorNemonico(String nemonico) {
        return ubicacionRepository.findByNemonicoIgnoreCase(nemonico)
                .orElseThrow(() -> new ResourceNotFoundException("Ubicación no encontrada con nemonico: " + nemonico));
    }

    // Mapea una entidad Ubicacion a un DTO UbicacionResponse
    public UbicacionResponse toUbicacionResponse(Ubicacion ubicacion) {
        UbicacionResponse response = new UbicacionResponse();
        response.setId(ubicacion.getId());
        response.setDescripcion(ubicacion.getDescripcion());
        response.setNemonico(ubicacion.getNemonico());
        response.setEstado(ubicacion.getEstado());
        response.setLatitud(ubicacion.getLatitud());
        response.setLongitud(ubicacion.getLongitud());
        response.setCreatedAt(ubicacion.getCreatedAt());
        response.setUpdatedAt(ubicacion.getUpdatedAt());
        return response;
    }
}
