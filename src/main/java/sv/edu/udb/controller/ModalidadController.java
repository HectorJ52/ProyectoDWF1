package sv.edu.udb.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sv.edu.udb.model.Modalidad;
import sv.edu.udb.service.ModalidadService;
import java.util.List;

@RestController
@RequestMapping("/api/modalidades")
@RequiredArgsConstructor
@Tag(name = "Modalidades", description = "Endpoints para el mantenimiento de Modalidades de Estudio")
public class ModalidadController {

    private final ModalidadService modalidadService;

    @GetMapping
    @Operation(summary = "Listar todas las modalidades disponibles")
    public ResponseEntity<List<Modalidad>> getAll() {
        return ResponseEntity.ok(modalidadService.findAll());
    }

    @PostMapping
    @Operation(summary = "Registrar una nueva modalidad de estudio en el sistema")
    public ResponseEntity<Modalidad> create(@RequestBody Modalidad modalidad) {
        return new ResponseEntity<>(modalidadService.save(modalidad), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener modalidad por ID")
    public ResponseEntity<Modalidad> getById(@PathVariable Long id) {
        return ResponseEntity.ok(modalidadService.findById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modificar una modalidad existente por su ID")
    public ResponseEntity<Modalidad> update(@PathVariable Long id, @RequestBody Modalidad modalidadActualizada) { // 🟢 Redirigido al Service
        return ResponseEntity.ok(modalidadService.update(id, modalidadActualizada));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar físicamente una modalidad del catálogo")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        modalidadService.delete(id);
        return ResponseEntity.noContent().build();
    }
}