package pawkar.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pawkar.backend.response.ApiResponseStandard;
import pawkar.backend.response.ExistenRegistrosResponse;
import pawkar.backend.service.VerificacionService;

@RestController
@RequestMapping("/verificacion")
public class VerificacionController {

    private final VerificacionService verificacionService;

    @Autowired
    public VerificacionController(VerificacionService verificacionService) {
        this.verificacionService = verificacionService;
    }

    @GetMapping("/existen-registros")
    public ResponseEntity<ApiResponseStandard<ExistenRegistrosResponse>> verificarRegistros() {
        ExistenRegistrosResponse response = verificacionService.verificarRegistros();
        return ResponseEntity.ok(
                ApiResponseStandard.success(response, "Verificación de registros exitosa"));
    }
}
