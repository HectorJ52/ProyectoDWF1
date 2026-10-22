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
import sv.edu.udb.controller.response.CursoResponse;
import sv.edu.udb.model.Curso;
import sv.edu.udb.service.CursoService;

@RestController
@RequestMapping("/api/cursos")
@RequiredArgsConstructor
@Tag(name = "Cursos", description = "Endpoints para el mantenimiento y control de Cursos Académicos")
public class CursoController {

    private final CursoService cursoService;

    @GetMapping
    @Operation(summary = "Listar cursos ofreciendo soporte a filtros (Categoría/Modalidad), ordenamiento y paginación")
    public ResponseEntity<Page<CursoResponse>> getAllCursos(
            @RequestParam(required = false) Long idCategoria,
            @RequestParam(required = false) Long idModalidad,
            @RequestParam(defaultValue = "true") Boolean activo,
            @PageableDefault(size = 10, sort = "nombre") Pageable pageable) {
        return ResponseEntity.ok(cursoService.findAllPaginated(idCategoria, idModalidad, activo, pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar los detalles de un curso por su ID único")
    public ResponseEntity<CursoResponse> getCursoById(@PathVariable Long id) {
        return ResponseEntity.ok(cursoService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Registrar una nueva oferta de curso (Módulo Académico)")
    public ResponseEntity<CursoResponse> createCurso(@Valid @RequestBody Curso curso) {
        return new ResponseEntity<>(cursoService.save(curso), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modificar un curso existente por su ID")
    public ResponseEntity<CursoResponse> updateCurso(@PathVariable Long id, @Valid @RequestBody Curso curso) {
        return ResponseEntity.ok(cursoService.update(id, curso));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Inactivar un curso (Borrado lógico por estado)")
    public ResponseEntity<Void> deleteCurso(@PathVariable Long id) {
        cursoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}