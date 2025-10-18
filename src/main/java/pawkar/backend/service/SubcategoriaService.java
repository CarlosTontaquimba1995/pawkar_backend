package pawkar.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pawkar.backend.entity.Categoria;
import pawkar.backend.entity.Subcategoria;
import pawkar.backend.repository.CategoriaRepository;
import pawkar.backend.repository.SubcategoriaRepository;
import pawkar.backend.request.SubcategoriaRequest;
import pawkar.backend.response.ApiResponseStandard;
import pawkar.backend.response.SubcategoriaResponse;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SubcategoriaService {

    @Autowired
    private SubcategoriaRepository subcategoriaRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    public Subcategoria crearSubcategoria(SubcategoriaRequest request) {
        Categoria categoria = categoriaRepository.findById(request.getCategoriaId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        Subcategoria subcategoria = new Subcategoria();
        subcategoria.setCategoria(categoria);
        subcategoria.setNombre(request.getNombre());
        subcategoria.setDescripcion(request.getDescripcion());

        return subcategoriaRepository.save(subcategoria);
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
