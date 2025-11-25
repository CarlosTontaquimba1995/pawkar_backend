package pawkar.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pawkar.backend.dto.ConfiguracionDTO;
import pawkar.backend.entity.Configuracion;
import pawkar.backend.exception.ResourceNotFoundException;
import pawkar.backend.repository.ConfiguracionRepository;

@Service
@RequiredArgsConstructor
public class ConfiguracionService {

    private static final String CACHE_NAME = "configuracion";
    private static final String CACHE_KEY = "'config'";

    private final ConfiguracionRepository configuracionRepository;

    @Cacheable(value = CACHE_NAME, key = CACHE_KEY, unless = "#result == null")
    public ConfiguracionDTO obtenerConfiguracion() {
        Configuracion configuracion = configuracionRepository.findFirst();
        if (configuracion == null) {
            throw new ResourceNotFoundException("No se encontró ninguna configuración en el sistema");
        }
        return convertToDTO(configuracion);
    }

    @Transactional
    @CachePut(value = CACHE_NAME, key = CACHE_KEY)
    public ConfiguracionDTO actualizarConfiguracion(ConfiguracionDTO configuracionDTO) {
        Configuracion configuracion = configuracionRepository.findFirst();

        if (configuracion == null) {
            configuracion = new Configuracion();
        }

        // Update fields
        configuracion.setPrimario(configuracionDTO.getPrimario());
        configuracion.setSecundario(configuracionDTO.getSecundario());
        configuracion.setAcento1(configuracionDTO.getAcento1());
        configuracion.setAcento2(configuracionDTO.getAcento2());

        Configuracion savedConfig = configuracionRepository.save(configuracion);
        return convertToDTO(savedConfig);
    }

    @CacheEvict(value = CACHE_NAME, key = CACHE_KEY)
    public void limpiarCache() {
        // Método para limpiar manualmente la caché si es necesario
    }

    private ConfiguracionDTO convertToDTO(Configuracion configuracion) {
        ConfiguracionDTO dto = new ConfiguracionDTO();
        dto.setConfiguracionId(configuracion.getConfiguracionId());
        dto.setPrimario(configuracion.getPrimario());
        dto.setSecundario(configuracion.getSecundario());
        dto.setAcento1(configuracion.getAcento1());
        dto.setAcento2(configuracion.getAcento2());
        return dto;
    }
}
