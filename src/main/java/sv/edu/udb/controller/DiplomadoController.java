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
import sv.edu.udb.controller.response.DiplomadoResponse;
import sv.edu.udb.model.Diplomado;
import sv.edu.udb.service.DiplomadoService;

@RestController
@RequestMapping("/api/diplomados")
@RequiredArgsConstructor
@Tag(name = "Diplomados", description = "Endpoints para el mantenimiento y control de Diplomados")
public class DiplomadoController {

    private final DiplomadoService diplomadoService;

    @GetMapping
    @Operation(summary = "Listar diplomados ofreciendo soporte a filtros (Categoría/Modalidad), ordenamiento y paginación")
    public ResponseEntity<Page<DiplomadoResponse>> getAllDiplomados(
            @RequestParam(required = false) Long idCategoria,
            @RequestParam(required = false) Long idModalidad,
            @RequestParam(defaultValue = "true") Boolean activo,
            @PageableDefault(size = 10, sort = "nombre") Pageable pageable) {
        return ResponseEntity.ok(diplomadoService.findAllPaginated(idCategoria, idModalidad, activo, pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar los detalles de un diplomado por su ID único")
    public ResponseEntity<DiplomadoResponse> getDiplomadoById(@PathVariable Long id) {
        return ResponseEntity.ok(diplomadoService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Registrar un nuevo programa de diplomado")
    public ResponseEntity<DiplomadoResponse> createDiplomado(@Valid @RequestBody Diplomado diplomado) {
        return new ResponseEntity<>(diplomadoService.save(diplomado), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modificar un diplomado existente por su ID")
    public ResponseEntity<DiplomadoResponse> updateDiplomado(@PathVariable Long id, @Valid @RequestBody Diplomado diplomado) {
        return ResponseEntity.ok(diplomadoService.update(id, diplomado));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Inactivar un diplomado (Borrado lógico por estado)")
    public ResponseEntity<Void> deleteDiplomado(@PathVariable Long id) {
        diplomadoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}