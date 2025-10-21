package pawkar.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pawkar.backend.entity.Categoria;
import pawkar.backend.entity.Subcategoria;
import pawkar.backend.repository.CategoriaRepository;
import pawkar.backend.repository.SubcategoriaRepository;
import pawkar.backend.request.BulkSubcategoriaRequest;
import pawkar.backend.request.SubcategoriaRequest;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubcategoriaService {

    @Autowired
    private SubcategoriaRepository subcategoriaRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

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

        return subcategoriaRepository.save(subcategoria);
    }

    @Transactional
    public List<Subcategoria> crearSubcategoriasBulk(BulkSubcategoriaRequest request) {
        // Verificar nombres duplicados en la solicitud
        List<String> nombres = request.getSubcategorias().stream()
                .map(SubcategoriaRequest::getNombre)
                .map(String::toLowerCase)
                .toList();
        
        // Verificar duplicados en la solicitud
        if (nombres.size() != nombres.stream().distinct().count()) {
            throw new RuntimeException("No se permiten nombres de subcategorías duplicados en la misma solicitud");
        }
        
        // Verificar duplicados en la base de datos
        for (String nombre : nombres) {
            if (subcategoriaRepository.existsByNombreIgnoreCase(nombre)) {
                throw new RuntimeException("Ya existe una subcategoría con el nombre: " + nombre);
            }
        }
        
        return request.getSubcategorias().stream()
                .map(subcategoriaRequest -> {
                    Categoria categoria = categoriaRepository.findById(subcategoriaRequest.getCategoriaId())
                            .orElseThrow(() -> new RuntimeException(
                                    "Categoría no encontrada con ID: " + subcategoriaRequest.getCategoriaId()));

                    Subcategoria subcategoria = new Subcategoria();
                    subcategoria.setCategoria(categoria);
                    subcategoria.setNombre(subcategoriaRequest.getNombre());
                    subcategoria.setDescripcion(subcategoriaRequest.getDescripcion());

                    return subcategoriaRepository.save(subcategoria);
                })
                .collect(Collectors.toList());
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

    public void eliminarSubcategoria(Integer id) {
        if (!subcategoriaRepository.existsById(id)) {
            throw new RuntimeException("Subcategoría no encontrada");
        }
        subcategoriaRepository.deleteById(id);
    }
}
