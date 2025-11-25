package pawkar.backend.controller;

import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pawkar.backend.dto.ConfiguracionDTO;
import pawkar.backend.response.ApiResponseStandard;
import pawkar.backend.service.ConfiguracionService;

@RestController
@RequestMapping("/configuracion")
public class ConfiguracionController {

    private final ConfiguracionService configuracionService;

    public ConfiguracionController(ConfiguracionService configuracionService) {
        this.configuracionService = configuracionService;
    }

    @GetMapping
    public ApiResponseStandard<ConfiguracionDTO> obtenerConfiguracion() {
        ConfiguracionDTO configuracion = configuracionService.obtenerConfiguracion();
        return ApiResponseStandard.success(configuracion, "Configuración obtenida exitosamente");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping
    public ApiResponseStandard<ConfiguracionDTO> actualizarConfiguracion(
            @Valid @RequestBody ConfiguracionDTO configuracionDTO) {
        ConfiguracionDTO configuracionActualizada = configuracionService.actualizarConfiguracion(configuracionDTO);
        return ApiResponseStandard.success(configuracionActualizada, "Configuración actualizada exitosamente");
    }
}
