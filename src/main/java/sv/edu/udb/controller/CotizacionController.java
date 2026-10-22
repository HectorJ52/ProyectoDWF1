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
import sv.edu.udb.controller.response.CotizacionResponse;
import sv.edu.udb.model.Cotizacion;
import sv.edu.udb.service.CotizacionService;

@RestController
@RequestMapping("/api/cotizaciones")
@RequiredArgsConstructor
@Tag(name = "Cotizaciones", description = "Endpoints para el mantenimiento de cotizaciones")
public class CotizacionController {

    private final CotizacionService cotizacionService;

    @GetMapping
    @Operation(summary = "Listar cotizaciones con paginación, ordenamiento y filtros")
    public ResponseEntity<Page<CotizacionResponse>> getAll(
            @RequestParam(required = false) Cotizacion.EstadoCotizacion estado,
            @RequestParam(required = false) Long idCliente,
            @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return ResponseEntity.ok(cotizacionService.findAll(estado, idCliente, pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener cotización por ID")
    public ResponseEntity<CotizacionResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(cotizacionService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Registrar una cotización")
    public ResponseEntity<CotizacionResponse> create(@Valid @RequestBody Cotizacion cotizacion) {
        return new ResponseEntity<>(cotizacionService.save(cotizacion), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una cotización")
    public ResponseEntity<CotizacionResponse> update(
            @PathVariable Long id, 
            @Valid @RequestBody Cotizacion cotizacion) {
        return ResponseEntity.ok(cotizacionService.update(id, cotizacion));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una cotización")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        cotizacionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}