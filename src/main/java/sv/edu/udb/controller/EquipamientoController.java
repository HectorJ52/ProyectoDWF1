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
import sv.edu.udb.controller.response.EquipamientoResponse;
import sv.edu.udb.model.Equipamiento;
import sv.edu.udb.service.EquipamientoService;

@RestController
@RequestMapping("/api/equipamientos")
@RequiredArgsConstructor
@Tag(name = "Equipamientos", description = "Catálogo de equipamiento de los espacios")
public class EquipamientoController {

    private final EquipamientoService service;

    @GetMapping
    @Operation(summary = "Listar equipamientos")
    public ResponseEntity<Page<EquipamientoResponse>> getAll(
            @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return ResponseEntity.ok(service.findAll(pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener equipamiento por ID")
    public ResponseEntity<EquipamientoResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    @Operation(summary = "Registrar un nuevo equipamiento")
    public ResponseEntity<EquipamientoResponse> create(@Valid @RequestBody Equipamiento equipamiento) {
        return new ResponseEntity<>(service.save(equipamiento), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un equipamiento")
    public ResponseEntity<EquipamientoResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody Equipamiento equipamiento) {
        return ResponseEntity.ok(service.update(id, equipamiento));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un equipamiento")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}