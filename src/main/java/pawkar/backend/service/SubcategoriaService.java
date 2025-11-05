package pawkar.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pawkar.backend.entity.Categoria;
import pawkar.backend.entity.Subcategoria;
import pawkar.backend.exception.BadRequestException;
import pawkar.backend.exception.ResourceNotFoundException;
import jakarta.persistence.EntityManager;
import pawkar.backend.repository.CategoriaRepository;
import pawkar.backend.repository.SubcategoriaRepository;
import pawkar.backend.request.BulkSubcategoriaRequest;
import pawkar.backend.request.SubcategoriaRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubcategoriaService {

        private final SubcategoriaRepository subcategoriaRepository;
        private final CategoriaRepository categoriaRepository;
        private final EntityManager entityManager;
        private final CategoriaService categoriaService;

    @Autowired
    public SubcategoriaService(SubcategoriaRepository subcategoriaRepository, CategoriaRepository categoriaRepository,
                    EntityManager entityManager, CategoriaService categoriaService) {
            this.subcategoriaRepository = subcategoriaRepository;
            this.categoriaRepository = categoriaRepository;
            this.entityManager = entityManager;
            this.categoriaService = categoriaService;
    }

    public Subcategoria crearSubcategoria(SubcategoriaRequest request) {
        // Verificar si ya existe una subcategoría con el mismo nombre (ignorando mayúsculas/minúsculas)
        if (subcategoriaRepository.existsByNombreIgnoreCase(request.getNombre())) {
            throw new RuntimeException("Ya existe una subcategoría con el nombre: " + request.getNombre());
        }

        Categoria categoria = categoriaRepository.findById(request.getCategoriaId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        Subcategoria subcategoria = new Subcategoria();
        subcategoria.setCategoria(categoria);
        subcategoria.setNombre(request.getNombre());
        subcategoria.setDescripcion(request.getDescripcion());
        subcategoria.setFechaHora(request.getFechaHora());

        return subcategoriaRepository.save(subcategoria);
    }

    @Transactional(rollbackFor = Exception.class)
    public List<Subcategoria> crearSubcategoriasBulk(BulkSubcategoriaRequest request) throws BadRequestException {
        // Verificar que la lista de subcategorías no esté vacía
        if (request.getSubcategorias() == null || request.getSubcategorias().isEmpty()) {
            throw new BadRequestException("La lista de subcategorías no puede estar vacía");
        }

        // Verificar nombres duplicados en la misma categoría dentro de la solicitud
        long uniqueSubcategoriasCount = request.getSubcategorias().stream()
                .map(sc -> sc.getCategoriaId() + "_" + sc.getNombre().toLowerCase())
                .distinct()
                .count();

        if (uniqueSubcategoriasCount < request.getSubcategorias().size()) {
            throw new BadRequestException("No se permiten nombres de subcategorías duplicados en la misma categoría");
        }

        // Verificar que las categorías existan
        List<Long> categoriaIds = request.getSubcategorias().stream()
                .map(subcategoria -> subcategoria.getCategoriaId().longValue())
                .distinct()
                .toList();

        List<Categoria> categorias = categoriaRepository.findAllById(categoriaIds);

        if (categorias.size() != categoriaIds.size()) {
            List<Long> categoriasNoEncontradas = categoriaIds.stream()
                    .filter(id -> categorias.stream()
                            .mapToInt(Categoria::getCategoriaId)
                            .noneMatch(catId -> catId == id))
                    .collect(Collectors.toList());

            throw new ResourceNotFoundException(
                    "Las siguientes categorías no existen: " + categoriasNoEncontradas);
        }

        // Verificar duplicados en la base de datos
        List<String> subcategoriasDuplicadas = new ArrayList<>();
        for (SubcategoriaRequest subcategoriaRequest : request.getSubcategorias()) {
            if (subcategoriaRepository.existsByCategoriaCategoriaIdAndNombreIgnoreCase(
                    subcategoriaRequest.getCategoriaId().intValue(), subcategoriaRequest.getNombre())) {
                // Obtener el nombre de la categoría
                final Long categoriaId = subcategoriaRequest.getCategoriaId();
                String nombreCategoria = categorias.stream()
                        .filter(c -> c.getCategoriaId().equals(categoriaId.intValue()))
                        .findFirst()
                        .map(Categoria::getNombre)
                        .orElse("Desconocida");

                subcategoriasDuplicadas.add(String.format("'%s' en la categoría '%s'",
                        subcategoriaRequest.getNombre(),
                        nombreCategoria));
            }
        }

        if (!subcategoriasDuplicadas.isEmpty()) {
            throw new BadRequestException(
                    "Las siguientes subcategorías ya existen: " + String.join(", ", subcategoriasDuplicadas));
        }

        // Mapear y guardar todas las subcategorías
        List<Subcategoria> subcategorias = request.getSubcategorias().stream()
                .map(subcategoriaRequest -> {
                    Categoria categoria = categorias.stream()
                            .filter(c -> c.getCategoriaId().equals(subcategoriaRequest.getCategoriaId().intValue()))
                            .findFirst()
                            .orElseThrow();

                    Subcategoria subcategoria = new Subcategoria();
                    subcategoria.setCategoria(categoria);
                    subcategoria.setNombre(subcategoriaRequest.getNombre());
                    subcategoria.setDescripcion(subcategoriaRequest.getDescripcion());
                                subcategoria.setFechaHora(subcategoriaRequest.getFechaHora());
                    return subcategoria;
                })
                .toList();

        return subcategoriaRepository.saveAll(subcategorias);
    }

    public List<Subcategoria> listarSubcategorias() {
        return subcategoriaRepository.findAll();
    }

    public List<Subcategoria> listarSubcategoriasPorCategoria(Integer categoriaId) {
        return subcategoriaRepository.findByCategoria_CategoriaId(categoriaId);
    }

    public Subcategoria obtenerSubcategoriaPorId(Integer id) {
        return subcategoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subcategoría no encontrada"));
    }

    public Subcategoria actualizarSubcategoria(Integer id, SubcategoriaRequest request) {
        Subcategoria subcategoria = subcategoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subcategoría no encontrada"));

        Categoria categoria = categoriaRepository.findById(request.getCategoriaId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        subcategoria.setCategoria(categoria);
        subcategoria.setNombre(request.getNombre());
        subcategoria.setDescripcion(request.getDescripcion());

        return subcategoriaRepository.save(subcategoria);
    }

    @Transactional
    public List<Subcategoria> findByCategoria_CategoriaIdAndProximo(Integer categoriaId, boolean proximo) {
            return subcategoriaRepository.findByCategoria_CategoriaIdAndProximo(categoriaId, proximo);
    }

    @Transactional(readOnly = true)
    public List<Subcategoria> findProximosEventos() {
            Categoria categoria = categoriaService.findEventosCategoria()
                            .orElseThrow(() -> new ResourceNotFoundException("No se encontró la categoría de eventos"));
            return findByCategoria_CategoriaIdAndProximo(categoria.getCategoriaId(), true);
    }

    @Transactional(readOnly = true)
    public List<Subcategoria> findEventosPasados() {
            Categoria categoria = categoriaService.findEventosCategoria()
                            .orElseThrow(() -> new ResourceNotFoundException("No se encontró la categoría de eventos"));
            return findByCategoria_CategoriaIdAndProximo(categoria.getCategoriaId(), false);
    }

    @Transactional
    public void eliminarSubcategoria(Integer id) {
        // Cargar la entidad con la relación de encuentros
        Subcategoria subcategoria = subcategoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subcategoría no encontrada con id: " + id));

        // Forzar la carga de los encuentros asociados
        entityManager.refresh(subcategoria);

        // Verificar si hay encuentros asociados usando una consulta directa
        Long countEncuentros = (Long) entityManager.createQuery(
                "SELECT COUNT(e) FROM Encuentro e WHERE e.subcategoria.id = :subcategoriaId")
                .setParameter("subcategoriaId", id)
                .getSingleResult();

        if (countEncuentros > 0) {
            throw new BadRequestException(String.format(
                    "No se puede eliminar la subcategoría '%s' porque está siendo utilizada por %d encuentro(s)",
                    subcategoria.getNombre(),
                    countEncuentros));
        }

        subcategoriaRepository.delete(subcategoria);
    }
}
