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
import sv.edu.udb.controller.response.EspacioResponse;
import sv.edu.udb.model.Espacio;
import sv.edu.udb.model.Espacio.TipoEspacio;
import sv.edu.udb.service.EspacioService;

@RestController
@RequestMapping("/api/espacios")
@RequiredArgsConstructor
@Tag(name = "Espacios", description = "Gestión de espacios físicos del CFC")
public class EspacioController {

    private final EspacioService service;

    @GetMapping
    @Operation(summary = "Listar espacios con filtros y paginación")
    public ResponseEntity<Page<EspacioResponse>> getAll(
            @RequestParam(required = false) TipoEspacio tipoEspacio,
            @RequestParam(required = false) Boolean soloActivos,
            @RequestParam(required = false) Integer capacidadMinima,
            @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return ResponseEntity.ok(service.findAll(tipoEspacio, soloActivos, capacidadMinima, pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener espacio por ID")
    public ResponseEntity<EspacioResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    @Operation(summary = "Registrar un nuevo espacio")
    public ResponseEntity<EspacioResponse> create(@Valid @RequestBody Espacio espacio) {
        return new ResponseEntity<>(service.save(espacio), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un espacio")
    public ResponseEntity<EspacioResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody Espacio espacio) {
        return ResponseEntity.ok(service.update(id, espacio));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un espacio")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}