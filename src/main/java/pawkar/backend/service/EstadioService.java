package pawkar.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pawkar.backend.entity.Estadio;
import pawkar.backend.exception.BadRequestException;
import pawkar.backend.exception.ResourceNotFoundException;
import pawkar.backend.repository.EstadioRepository;
import pawkar.backend.request.BulkEstadioRequest;
import pawkar.backend.request.EstadioRequest;
import pawkar.backend.response.EstadioResponse;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EstadioService {

    private final EstadioRepository estadioRepository;

    @Autowired
    public EstadioService(EstadioRepository estadioRepository) {
        this.estadioRepository = estadioRepository;
    }

    @Transactional(readOnly = true)
    public List<EstadioResponse> getAllEstadios() {
        return estadioRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EstadioResponse getEstadioById(Long id) {
        Estadio estadio = estadioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Estadio no encontrado con id: " + id));
        return convertToResponse(estadio);
    }

    @Transactional
    public EstadioResponse createEstadio(EstadioRequest estadioRequest) {
        if (estadioRepository.existsByNombre(estadioRequest.getNombre())) {
            throw new BadRequestException("El nombre del estadio ya está en uso");
        }

        Estadio estadio = new Estadio();
        estadio.setNombre(estadioRequest.getNombre());
        estadio.setDetalle(estadioRequest.getDetalle());

        if (estadioRequest.getActivo() != null) {
            estadio.setActivo(estadioRequest.getActivo());
        }

        Estadio savedEstadio = estadioRepository.save(estadio);
        return convertToResponse(savedEstadio);
    }

    @Transactional
    public EstadioResponse updateEstadio(Long id, EstadioRequest estadioRequest) {
        Estadio estadio = estadioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Estadio no encontrado con id: " + id));

        if (estadioRequest.getNombre() != null && !estadioRequest.getNombre().equals(estadio.getNombre())) {
            if (estadioRepository.existsByNombre(estadioRequest.getNombre())) {
                throw new BadRequestException("El nombre del estadio ya está en uso");
            }
            estadio.setNombre(estadioRequest.getNombre());
        }

        if (estadioRequest.getDetalle() != null) {
            estadio.setDetalle(estadioRequest.getDetalle());
        }

        if (estadioRequest.getActivo() != null) {
            estadio.setActivo(estadioRequest.getActivo());
        }

        Estadio updatedEstadio = estadioRepository.save(estadio);
        return convertToResponse(updatedEstadio);
    }

    @Transactional
    public void deleteEstadio(Long id) {
        if (!estadioRepository.existsById(id)) {
            throw new ResourceNotFoundException("Estadio no encontrado con id: " + id);
        }
        estadioRepository.deleteById(id);
    }

    @Transactional
    public List<EstadioResponse> crearEstadiosEnLote(BulkEstadioRequest request) {
        // Verificar que la lista de estadios no esté vacía
        if (request.getEstadios() == null || request.getEstadios().isEmpty()) {
            throw new BadRequestException("La lista de estadios no puede estar vacía");
        }

        // Verificar nombres duplicados en la solicitud
        long uniqueEstadiosCount = request.getEstadios().stream()
                .map(EstadioRequest::getNombre)
                .distinct()
                .count();

        if (uniqueEstadiosCount < request.getEstadios().size()) {
            throw new BadRequestException("No se permiten nombres de estadios duplicados");
        }

        // Verificar duplicados en la base de datos
        List<String> nombresEstadios = request.getEstadios().stream()
                .map(EstadioRequest::getNombre)
                .toList();

        List<String> estadiosExistentes = estadioRepository.findByNombreIn(nombresEstadios).stream()
                .map(Estadio::getNombre)
                .toList();

        if (!estadiosExistentes.isEmpty()) {
            throw new BadRequestException(
                    "Los siguientes estadios ya existen: " + String.join(", ", estadiosExistentes));
        }

        // Mapear y guardar todos los estadios
        List<Estadio> estadios = request.getEstadios().stream()
                .map(estadioRequest -> {
                    Estadio estadio = new Estadio();
                    estadio.setNombre(estadioRequest.getNombre());
                    estadio.setDetalle(estadioRequest.getDetalle());
                    if (estadioRequest.getActivo() != null) {
                        estadio.setActivo(estadioRequest.getActivo());
                    }
                    return estadio;
                })
                .toList();

        List<Estadio> estadiosGuardados = estadioRepository.saveAll(estadios);
        return estadiosGuardados.stream()
                .map(this::convertToResponse)
                .toList();
    }

    private EstadioResponse convertToResponse(Estadio estadio) {
        EstadioResponse response = new EstadioResponse();
        response.setId(estadio.getId());
        response.setNombre(estadio.getNombre());
        response.setDetalle(estadio.getDetalle());
        response.setEstado(estadio.getEstado());
        response.setActivo(estadio.isActivo());
        return response;
    }
}
