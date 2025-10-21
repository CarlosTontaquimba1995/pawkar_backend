package pawkar.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pawkar.backend.repository.SerieRepository;
import pawkar.backend.repository.SubcategoriaRepository;
import pawkar.backend.response.ExistenRegistrosResponse;

@Service
public class VerificacionService {

    private final SubcategoriaRepository subcategoriaRepository;
    private final SerieRepository serieRepository;

    @Autowired
    public VerificacionService(SubcategoriaRepository subcategoriaRepository, SerieRepository serieRepository) {
        this.subcategoriaRepository = subcategoriaRepository;
        this.serieRepository = serieRepository;
    }

    @Transactional(readOnly = true)
    public ExistenRegistrosResponse verificarRegistros() {
        boolean existenSubcategorias = subcategoriaRepository.count() > 0;
        boolean existenSeries = serieRepository.count() > 0;

        return new ExistenRegistrosResponse(existenSubcategorias && existenSeries);
    }
}
