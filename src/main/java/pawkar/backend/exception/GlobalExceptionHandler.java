package pawkar.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import pawkar.backend.response.ApiResponseStandard;

import java.nio.file.AccessDeniedException;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseStandard<Object>> handleGlobalException(Exception ex, WebRequest request) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponseStandard.error(
                        "Ocurrió un error al procesar su solicitud",
                        request.getDescription(false),
                        ex.getMessage(),
                        HttpStatus.INTERNAL_SERVER_ERROR.value()
                ));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponseStandard<Object>> handleResourceNotFoundException(
            ResourceNotFoundException ex, WebRequest request) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ApiResponseStandard.error(
                        ex.getMessage(),
                        request.getDescription(false),
                        "Recurso no encontrado",
                        HttpStatus.NOT_FOUND.value()
                ));
    }

    @ExceptionHandler(DuplicatePlantillaException.class)
    public ResponseEntity<ApiResponseStandard<Object>> handleDuplicatePlantillaException(
                    DuplicatePlantillaException ex, WebRequest request) {
            return ResponseEntity
                            .badRequest()
                            .body(ApiResponseStandard.error(
                                            ex.getMessage(),
                                            request.getDescription(false),
                                            "Conflicto de datos",
                                            HttpStatus.BAD_REQUEST.value()));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiResponseStandard<Object>> handleIllegalStateException(
                    IllegalStateException ex, WebRequest request) {
            return ResponseEntity
                            .badRequest()
                            .body(ApiResponseStandard.error(
                                            ex.getMessage(),
                                            request.getDescription(false),
                                            "Datos inválidos",
                                            HttpStatus.BAD_REQUEST.value()));
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiResponseStandard<Object>> handleBadRequestException(
            BadRequestException ex, WebRequest request) {
        return ResponseEntity
                .badRequest()
                .body(ApiResponseStandard.error(
                        ex.getMessage(),
                        request.getDescription(false),
                        "Solicitud incorrecta",
                        HttpStatus.BAD_REQUEST.value()
                ));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponseStandard<Object>> handleAccessDeniedException(
            AccessDeniedException ex, WebRequest request) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(ApiResponseStandard.error(
                        "You don't have permission to access this resource",
                        request.getDescription(false),
                        "Access Denied",
                        HttpStatus.FORBIDDEN.value()
                ));
    }
}
