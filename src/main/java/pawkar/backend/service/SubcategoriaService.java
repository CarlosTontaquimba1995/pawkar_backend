package pawkar.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pawkar.backend.entity.Artista;
import pawkar.backend.entity.Categoria;
import pawkar.backend.entity.Subcategoria;
import pawkar.backend.exception.BadRequestException;
import pawkar.backend.exception.ResourceNotFoundException;
import jakarta.persistence.EntityManager;
import pawkar.backend.repository.ArtistaRepository;
import pawkar.backend.repository.CategoriaRepository;
import pawkar.backend.repository.SubcategoriaRepository;
import pawkar.backend.request.ArtistaRequest;
import pawkar.backend.request.BulkSubcategoriaRequest;
import pawkar.backend.request.SubcategoriaRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SubcategoriaService {

        private final SubcategoriaRepository subcategoriaRepository;
        private final CategoriaRepository categoriaRepository;
        private final ArtistaRepository artistaRepository;
        private final EntityManager entityManager;
        private final CategoriaService categoriaService;

        @Autowired
        public SubcategoriaService(SubcategoriaRepository subcategoriaRepository,
                        CategoriaRepository categoriaRepository,
                        ArtistaRepository artistaRepository,
                        EntityManager entityManager,
                        CategoriaService categoriaService) {
                this.subcategoriaRepository = subcategoriaRepository;
                this.categoriaRepository = categoriaRepository;
                this.artistaRepository = artistaRepository;
                this.entityManager = entityManager;
                this.categoriaService = categoriaService;
        }

        @Transactional
        public Subcategoria crearSubcategoria(SubcategoriaRequest request) {
                // Verificar si ya existe una subcategoría con el mismo nombre en la misma
                // categoría
                if (subcategoriaRepository.existsByCategoriaCategoriaIdAndNombreIgnoreCase(
                                request.getCategoriaId().intValue(), request.getNombre())) {
                        throw new BadRequestException(
                                        "Ya existe una subcategoría con el nombre '" + request.getNombre()
                                                        + "' en esta categoría");
                }

                // Obtener la categoría
                Categoria categoria = categoriaRepository.findById(request.getCategoriaId())
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Categoría no encontrada con ID: " + request.getCategoriaId()));

                // Crear la subcategoría
                Subcategoria subcategoria = new Subcategoria();
                subcategoria.setCategoria(categoria);
                subcategoria.setNombre(request.getNombre());
                subcategoria.setDescripcion(request.getDescripcion());
                subcategoria.setFechaHora(request.getFechaHora());
                subcategoria.setProximo(request.getProximo() != null ? request.getProximo() : true);
                subcategoria.setUbicacion(request.getUbicacion());
                subcategoria.setLatitud(request.getLatitud());
                subcategoria.setLongitud(request.getLongitud());
                subcategoria.setPrecio(request.getPrecio());

                // Generar y establecer el nemonico
                String nemonico = generateNemonico(request.getNombre());
                subcategoria.setNemonico(nemonico);

                // Manejar artistas
                if (request.getArtistas() != null && !request.getArtistas().isEmpty()) {
                        Set<Artista> artistas = new HashSet<>();
                        for (ArtistaRequest artistaRequest : request.getArtistas()) {
                                Artista artista = new Artista();
                                artista.setNombre(artistaRequest.getNombre());
                                artista.setGenero(artistaRequest.getGenero());
                                artistas.add(artista);
                        }
                        subcategoria.setArtistas(artistas);
                }

                return subcategoriaRepository.save(subcategoria);
        }

        @Transactional
        public List<Subcategoria> crearSubcategoriasBulk(BulkSubcategoriaRequest request) {
                List<Subcategoria> subcategorias = new ArrayList<>();

                // Validar nombres duplicados en la misma categoría
                Map<String, Set<String>> categoriaNombres = new HashMap<>();
                for (SubcategoriaRequest subcategoriaRequest : request.getSubcategorias()) {
                        String categoriaKey = String.valueOf(subcategoriaRequest.getCategoriaId());
                        categoriaNombres.computeIfAbsent(categoriaKey, k -> new HashSet<>())
                                        .add(subcategoriaRequest.getNombre().toLowerCase());
                }

                // Verificar duplicados en la solicitud
                for (Map.Entry<String, Set<String>> entry : categoriaNombres.entrySet()) {
                        if (entry.getValue().size() != request.getSubcategorias().stream()
                                        .filter(s -> s.getCategoriaId().toString().equals(entry.getKey()))
                                        .count()) {
                                throw new BadRequestException(
                                                "No se permiten nombres de subcategorías duplicados en la misma categoría");
                        }
                }

                // Obtener todas las categorías necesarias
                Set<Long> categoriaIds = request.getSubcategorias().stream()
                                .map(SubcategoriaRequest::getCategoriaId)
                                .collect(Collectors.toSet());

                Map<Long, Categoria> categorias = categoriaRepository.findAllById(categoriaIds).stream()
                                .collect(Collectors.toMap(
                                                c -> Long.valueOf(c.getCategoriaId()),
                                                c -> c,
                                                (a, b) -> a));

                // Verificar que todas las categorías existen
                Set<Long> missingCategorias = categoriaIds.stream()
                                .filter(id -> !categorias.containsKey(id))
                                .collect(Collectors.toSet());

                if (!missingCategorias.isEmpty()) {
                        throw new ResourceNotFoundException(
                                        "No se encontraron las siguientes categorías: " + missingCategorias);
                }

                // Crear las subcategorías
                for (SubcategoriaRequest subcategoriaRequest : request.getSubcategorias()) {
                        // Verificar si ya existe una subcategoría con el mismo nombre en la misma
                        // categoría
                        if (subcategoriaRepository.existsByCategoriaCategoriaIdAndNombreIgnoreCase(
                                        subcategoriaRequest.getCategoriaId().intValue(),
                                        subcategoriaRequest.getNombre())) {
                                throw new BadRequestException(
                                                "Ya existe una subcategoría con el nombre '"
                                                                + subcategoriaRequest.getNombre()
                                                                + "' en la categoría con ID "
                                                                + subcategoriaRequest.getCategoriaId());
                        }

                        Subcategoria subcategoria = new Subcategoria();
                        Categoria categoria = categorias.get(subcategoriaRequest.getCategoriaId());
                        subcategoria.setCategoria(categoria);
                        subcategoria.setNombre(subcategoriaRequest.getNombre());
                        subcategoria.setDescripcion(subcategoriaRequest.getDescripcion());
                        subcategoria.setFechaHora(subcategoriaRequest.getFechaHora());
                        subcategoria.setProximo(
                                        subcategoriaRequest.getProximo() != null ? subcategoriaRequest.getProximo()
                                                        : true);
                        subcategoria.setUbicacion(subcategoriaRequest.getUbicacion());
                        subcategoria.setLatitud(subcategoriaRequest.getLatitud());
                        subcategoria.setLongitud(subcategoriaRequest.getLongitud());
                        subcategoria.setPrecio(subcategoriaRequest.getPrecio());

                        // Generar y establecer el nemonico
                        String nemonico = generateNemonico(subcategoriaRequest.getNombre());
                        subcategoria.setNemonico(nemonico);

                        // Manejar artistas
                        if (subcategoriaRequest.getArtistas() != null && !subcategoriaRequest.getArtistas().isEmpty()) {
                                Set<Artista> artistas = new HashSet<>();
                                for (ArtistaRequest artistaRequest : subcategoriaRequest.getArtistas()) {
                                        // Find the existing artist or create a new one and save it
                                        Artista artista = artistaRepository
                                                        .findByNombreIgnoreCase(artistaRequest.getNombre())
                                                        .orElseGet(() -> {
                                                                Artista newArtista = new Artista();
                                                                newArtista.setNombre(artistaRequest.getNombre());
                                                                newArtista.setGenero(artistaRequest.getGenero());
                                                                return artistaRepository.save(newArtista); // Save the
                                                                                                           // new artist
                                                                                                           // immediately
                                                        });
                                        artistas.add(artista);
                                }
                                subcategoria.setArtistas(artistas);
                        }

                        subcategorias.add(subcategoria);
                }

                return subcategoriaRepository.saveAll(subcategorias);
        }

        private String generateNemonico(String nombre) {
                if (nombre == null) {
                        return null;
                }
                // Normalize to NFD to separate base characters from diacritics
                String normalized = java.text.Normalizer.normalize(nombre.trim(), java.text.Normalizer.Form.NFD)
                                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");

                // Convert to uppercase and replace spaces and special characters with
                // underscores
                return normalized.toUpperCase()
                                .replaceAll("[^A-Z0-9]", "_")
                                .replaceAll("_+", "_")
                                .replaceAll("^_|_$", "");
        }

        public List<Subcategoria> listarSubcategorias() {
                return subcategoriaRepository.findAll();
        }

        public List<Subcategoria> listarSubcategoriasPorCategoria(Integer categoriaId) {
                return subcategoriaRepository.findByCategoria_CategoriaId(categoriaId);
        }

        public Subcategoria obtenerSubcategoriaPorNemonico(String nemonico) {
                return subcategoriaRepository.findByNemonico(nemonico)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Subcategoría no encontrada con nemonico: " + nemonico));
        }

        public Subcategoria obtenerSubcategoriaPorId(Integer id) {
                return subcategoriaRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Subcategoría no encontrada"));
        }

        @Transactional
        public Subcategoria actualizarSubcategoria(Integer id, SubcategoriaRequest request) {
                Subcategoria subcategoria = subcategoriaRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Subcategoría no encontrada con ID: " + id));

                // Verificar si el nombre ya existe en la misma categoría (excluyendo la actual)
                if (subcategoriaRepository.existsByCategoriaCategoriaIdAndNombreIgnoreCaseAndSubcategoriaIdNot(
                                request.getCategoriaId().intValue(),
                                request.getNombre(),
                                id)) {
                        throw new BadRequestException(
                                        "Ya existe una subcategoría con el nombre '" + request.getNombre()
                                                        + "' en esta categoría");
                }

                // Actualizar campos básicos
                subcategoria.setNombre(request.getNombre());
                subcategoria.setDescripcion(request.getDescripcion());
                subcategoria.setFechaHora(request.getFechaHora());
                subcategoria.setProximo(
                                request.getProximo() != null ? request.getProximo() : subcategoria.getProximo());
                subcategoria.setUbicacion(request.getUbicacion());
                subcategoria.setLatitud(request.getLatitud());
                subcategoria.setLongitud(request.getLongitud());

                // Actualizar categoría si es necesario
                if (!subcategoria.getCategoria().getCategoriaId().equals(request.getCategoriaId())) {
                        Categoria nuevaCategoria = categoriaRepository.findById(request.getCategoriaId())
                                        .orElseThrow(() -> new ResourceNotFoundException(
                                                        "Categoría no encontrada con ID: " + request.getCategoriaId()));
                        subcategoria.setCategoria(nuevaCategoria);
                }

                // Manejar artistas
                if (request.getArtistas() != null) {
                        // Limpiar artistas existentes
                        subcategoria.getArtistas().clear();

                        // Agregar nuevos artistas
                        for (ArtistaRequest artistaRequest : request.getArtistas()) {
                                Artista artista = new Artista();
                                artista.setNombre(artistaRequest.getNombre());
                                artista.setGenero(artistaRequest.getGenero());
                                subcategoria.addArtista(artista);
                        }
                }

                return subcategoriaRepository.save(subcategoria);
        }

        @Transactional
        public List<Subcategoria> findByCategoria_CategoriaIdAndProximo(Integer categoriaId, boolean proximo) {
                return subcategoriaRepository.findByCategoria_CategoriaIdAndProximo(categoriaId, proximo);
        }

        @Transactional(readOnly = true)
        public List<Subcategoria> findProximosEventos() {
                Categoria categoria = categoriaService.findEventosCategoria()
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "No se encontró la categoría de eventos"));
                return findByCategoria_CategoriaIdAndProximo(categoria.getCategoriaId(), true);
        }

        @Transactional(readOnly = true)
        public List<Subcategoria> findEventosPasados() {
                Categoria categoria = categoriaService.findEventosCategoria()
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "No se encontró la categoría de eventos"));
                return findByCategoria_CategoriaIdAndProximo(categoria.getCategoriaId(), false);
        }

        @Transactional
        public void eliminarSubcategoria(Integer id) {
                // Cargar la entidad con la relación de encuentros
                Subcategoria subcategoria = subcategoriaRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Subcategoría no encontrada con id: " + id));

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
