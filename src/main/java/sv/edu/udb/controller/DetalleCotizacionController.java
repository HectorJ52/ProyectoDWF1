package sv.edu.udb.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sv.edu.udb.controller.response.DetalleCotizacionResponse;
import sv.edu.udb.model.DetalleCotizacion;
import sv.edu.udb.service.DetalleCotizacionService;

@RestController
@RequestMapping("/api/detalles-cotizacion")
@RequiredArgsConstructor
@Tag(name = "Detalles de Cotización", description = "Endpoints para el mantenimiento de los detalles de cotización")
public class DetalleCotizacionController {

    private final DetalleCotizacionService detalleService;

    @GetMapping
    @Operation(summary = "Listar detalles de cotización con paginación y ordenamiento")
    public ResponseEntity<Page<DetalleCotizacionResponse>> getAll(
            @RequestParam(required = false) Long idCotizacion,
            @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return ResponseEntity.ok(detalleService.findAll(idCotizacion, pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener detalle de cotización por ID")
    public ResponseEntity<DetalleCotizacionResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(detalleService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Registrar un detalle de cotización")
    public ResponseEntity<DetalleCotizacionResponse> create(@Valid @RequestBody DetalleCotizacion detalle) { // 🟢 Usa DetalleCotizacion entidad
        return new ResponseEntity<>(detalleService.save(detalle), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un detalle de cotización")
    public ResponseEntity<DetalleCotizacionResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody DetalleCotizacion detalle) {
        return ResponseEntity.ok(detalleService.update(id, detalle));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un detalle de cotización")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        detalleService.delete(id);
        return ResponseEntity.noContent().build();
    }
}