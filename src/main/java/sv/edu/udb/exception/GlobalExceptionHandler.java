package sv.edu.udb.exception;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleEntityNotFound(
            EntityNotFoundException ex
    ) {

        Map<String, Object> response = new HashMap<>();

        response.put("fecha", LocalDateTime.now());
        response.put("estado", HttpStatus.NOT_FOUND.value());
        response.put("error", "Recurso no encontrado");
        response.put("mensaje", ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(response);
    }

    @ExceptionHandler(SaldoInsuficienteException.class)
    public ResponseEntity<Map<String, Object>> handleSaldoInsuficiente(
            SaldoInsuficienteException ex
    ) {

        Map<String, Object> response = new HashMap<>();

        response.put("fecha", LocalDateTime.now());
        response.put("estado", HttpStatus.BAD_REQUEST.value());
        response.put("error", "Pago inválido");
        response.put("mensaje", ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(
            IllegalArgumentException ex
    ) {

        Map<String, Object> response = new HashMap<>();

        response.put("fecha", LocalDateTime.now());
        response.put("estado", HttpStatus.BAD_REQUEST.value());
        response.put("error", "Solicitud inválida");
        response.put("mensaje", ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationErrors(
            MethodArgumentNotValidException ex
    ) {

        Map<String, String> errores = new HashMap<>();

        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        errores.put(
                                error.getField(),
                                error.getDefaultMessage()
                        )
                );

        Map<String, Object> response = new HashMap<>();

        response.put("fecha", LocalDateTime.now());
        response.put("estado", HttpStatus.BAD_REQUEST.value());
        response.put("error", "Error de validación");
        response.put("campos", errores);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneral(
            Exception ex
    ) {

        Map<String, Object> response = new HashMap<>();

        response.put("fecha", LocalDateTime.now());
        response.put(
                "estado",
                HttpStatus.INTERNAL_SERVER_ERROR.value()
        );
        response.put("error", "Error interno del servidor");
        response.put("mensaje", ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }
}