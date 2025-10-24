package pawkar.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pawkar.backend.entity.Categoria;
import pawkar.backend.exception.BadRequestException;
import pawkar.backend.exception.ResourceNotFoundException;
import pawkar.backend.repository.CategoriaRepository;
import pawkar.backend.request.BulkCategoriaRequest;
import pawkar.backend.request.CategoriaRequest;
import pawkar.backend.response.CategoriaResponse;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    @Autowired
    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @Transactional(readOnly = true)
    public List<CategoriaResponse> getAllCategorias() {
        return categoriaRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CategoriaResponse getCategoriaById(Long id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria no encontrada con id: " + id));
        return convertToResponse(categoria);
    }

    @Transactional
    public CategoriaResponse createCategoria(CategoriaRequest categoriaRequest) {
        if (categoriaRepository.existsByNombre(categoriaRequest.getNombre())) {
            throw new BadRequestException("El nombre de la categoría ya está en uso");
        }

        Categoria categoria = new Categoria();
        categoria.setNombre(categoriaRequest.getNombre());

        Categoria savedCategoria = categoriaRepository.save(categoria);
        return convertToResponse(savedCategoria);
    }

    @Transactional
    public List<CategoriaResponse> createBulkCategorias(BulkCategoriaRequest bulkRequest) {
        // Verificar nombres duplicados en la solicitud
        long uniqueNames = bulkRequest.getCategorias().stream()
                .map(CategoriaRequest::getNombre)
                .distinct()
                .count();

        if (uniqueNames != bulkRequest.getCategorias().size()) {
            throw new BadRequestException("No se permiten nombres de categorías duplicados en la solicitud");
        }

        // Verificar nombres que ya existen en la base de datos
        List<String> existingNames = categoriaRepository.findByNombreIn(
                bulkRequest.getCategorias().stream()
                        .map(CategoriaRequest::getNombre)
                        .collect(Collectors.toList()))
                .stream()
                .map(Categoria::getNombre)
                .collect(Collectors.toList());

        if (!existingNames.isEmpty()) {
            throw new BadRequestException("Las siguientes categorías ya existen: " + String.join(", ", existingNames));
        }

        // Guardar todas las categorías
        List<Categoria> categorias = bulkRequest.getCategorias().stream()
                .map(req -> {
                    Categoria cat = new Categoria();
                    cat.setNombre(req.getNombre());
                    return cat;
                })
                .collect(Collectors.toList());

        List<Categoria> savedCategorias = categoriaRepository.saveAll(categorias);
        return savedCategorias.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public CategoriaResponse updateCategoria(Long id, CategoriaRequest categoriaRequest) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria no encontrada con id: " + id));

        if (!categoria.getNombre().equals(categoriaRequest.getNombre()) &&
                categoriaRepository.existsByNombre(categoriaRequest.getNombre())) {
            throw new BadRequestException("El nombre de la categoría ya está en uso");
        }

        categoria.setNombre(categoriaRequest.getNombre());
        Categoria updatedCategoria = categoriaRepository.save(categoria);
        return convertToResponse(updatedCategoria);
    }

    @Transactional
    public void deleteCategoria(Long id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria no encontrada con id: " + id));
        categoriaRepository.delete(categoria);
    }

    private CategoriaResponse convertToResponse(Categoria categoria) {
        CategoriaResponse response = new CategoriaResponse();
        response.setCategoriaId(categoria.getCategoriaId());
        response.setNombre(categoria.getNombre());
        response.setEstado(categoria.isEstado());
        return response;
    }
}
