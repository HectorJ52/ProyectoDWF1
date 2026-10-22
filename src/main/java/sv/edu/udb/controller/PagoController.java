package sv.edu.udb.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sv.edu.udb.controller.response.PagoResponse;
import sv.edu.udb.model.Pago;
import sv.edu.udb.service.PagoService;

@RestController
@RequestMapping("/api/pagos")
@RequiredArgsConstructor
@Tag(name = "Pagos", description = "Endpoints para la gestión de pagos")
public class PagoController {
    private final PagoService pagoService;

    @GetMapping
    @Operation(summary = "Listar todos los pagos")
    public ResponseEntity<Page<PagoResponse>> getAll(
            @ParameterObject @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return ResponseEntity.ok(pagoService.findAll(pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar un pago por ID")
    public ResponseEntity<PagoResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(pagoService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Registrar un nuevo pago")
    public ResponseEntity<PagoResponse> create(@Valid @RequestBody Pago pago) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pagoService.save(pago));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un pago")
    public ResponseEntity<PagoResponse> update(@PathVariable Long id, @Valid @RequestBody Pago pago) {
        return ResponseEntity.ok(pagoService.update(id, pago));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un pago")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        pagoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}