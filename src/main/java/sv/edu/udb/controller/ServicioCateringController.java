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
import sv.edu.udb.controller.response.ServicioCateringResponse;
import sv.edu.udb.model.ServicioCatering;
import sv.edu.udb.service.ServicioCateringService;

@RestController
@RequestMapping("/api/servicios-catering")
@RequiredArgsConstructor
@Tag(name = "Servicios de Catering", description = "Catálogo de servicios de alimentación")
public class ServicioCateringController {

    private final ServicioCateringService service;

    @GetMapping
    @Operation(summary = "Listar servicios de catering")
    public ResponseEntity<Page<ServicioCateringResponse>> getAll(
            @RequestParam(required = false) Boolean soloActivos,
            @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return ResponseEntity.ok(service.findAll(soloActivos, pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener servicio de catering por ID")
    public ResponseEntity<ServicioCateringResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    @Operation(summary = "Registrar un nuevo servicio de catering")
    public ResponseEntity<ServicioCateringResponse> create(@Valid @RequestBody ServicioCatering servicio) {
        return new ResponseEntity<>(service.save(servicio), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un servicio de catering")
    public ResponseEntity<ServicioCateringResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody ServicioCatering servicio) {
        return ResponseEntity.ok(service.update(id, servicio));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un servicio de catering")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}