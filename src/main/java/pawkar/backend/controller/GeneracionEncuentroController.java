package pawkar.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import pawkar.backend.request.GeneracionEncuentroRequest;
import pawkar.backend.response.ApiResponseStandard;
import pawkar.backend.response.EncuentroResponse;
import pawkar.backend.service.GeneracionEncuentroService;

import java.util.List;

@RestController
@RequestMapping("/generacion-encuentros")
public class GeneracionEncuentroController {

    @Autowired
    private GeneracionEncuentroService generacionEncuentroService;

    @PostMapping
    public ApiResponseStandard<List<EncuentroResponse>> generarEncuentros(
            @RequestBody GeneracionEncuentroRequest request) {

        List<EncuentroResponse> encuentrosGenerados = generacionEncuentroService.generarEncuentros(request);
        return ApiResponseStandard.success(encuentrosGenerados, "Encuentros generados exitosamente");
    }
}
