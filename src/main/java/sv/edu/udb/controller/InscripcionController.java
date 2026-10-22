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
import sv.edu.udb.controller.response.InscripcionResponse;
import sv.edu.udb.model.Inscripcion; // 🟢 Importamos el modelo centralizado
import sv.edu.udb.service.InscripcionService;

@RestController
@RequestMapping("/api/inscripciones")
@RequiredArgsConstructor
@Tag(name = "Inscripciones", description = "Endpoints para el registro y control de Inscripciones")
public class InscripcionController {

    private final InscripcionService inscripcionService;

    @GetMapping
    @Operation(summary = "Listar inscripciones de forma paginada, con filtro opcional por cliente")
    public ResponseEntity<Page<InscripcionResponse>> getAll(
            @RequestParam(defaultValue = "true") Boolean activo,
            @RequestParam(required = false) Long idCliente,
            @PageableDefault(size = 10, sort = "fechaInscripcion") Pageable pageable) {
        return ResponseEntity.ok(inscripcionService.findAllPaginated(activo, idCliente, pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener inscripción por ID")
    public ResponseEntity<InscripcionResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(inscripcionService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Registrar una nueva inscripción (debe apuntar a un Curso o a un Diplomado, no ambos)")
    public ResponseEntity<InscripcionResponse> create(@Valid @RequestBody Inscripcion inscripcion) {
        return new ResponseEntity<>(inscripcionService.save(inscripcion), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una inscripción existente")
    public ResponseEntity<InscripcionResponse> update(@PathVariable Long id, @Valid @RequestBody Inscripcion inscripcion) {
        return ResponseEntity.ok(inscripcionService.update(id, inscripcion));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Inactivar una inscripción (borrado lógico)")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        inscripcionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}