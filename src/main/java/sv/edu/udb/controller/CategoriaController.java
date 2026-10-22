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
import sv.edu.udb.model.Categoria;
import sv.edu.udb.repository.CategoriaRepository;

@RestController
@RequestMapping("/api/categorias")
@RequiredArgsConstructor
@Tag(name = "Categorías", description = "Endpoints para el mantenimiento de Categorías Académicas")
public class CategoriaController {

    private final CategoriaRepository categoriaRepository;

    @GetMapping
    @Operation(summary = "Listar categorías de forma paginada y ordenada")
    public ResponseEntity<Page<Categoria>> getAll(
            @RequestParam(defaultValue = "true") Boolean activo,
            @PageableDefault(size = 10, sort = "nombre") Pageable pageable) {
        return ResponseEntity.ok(categoriaRepository.findByActivo(activo, pageable));
    }

    @PostMapping
    @Operation(summary = "Crear una nueva categoría")
    public ResponseEntity<Categoria> create(@Valid @RequestBody Categoria categoria) {
        categoria.setActivo(true);
        return new ResponseEntity<>(categoriaRepository.save(categoria), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener categoría por ID")
    public ResponseEntity<Categoria> getById(@PathVariable Long id) {
        return ResponseEntity.ok(categoriaRepository.findById(id)
                .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("Categoría no encontrada")));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modificar una categoría existente por su ID")
    public ResponseEntity<Categoria> update(@PathVariable Long id, @Valid @RequestBody Categoria categoriaActualizada) {
        return categoriaRepository.findById(id).map(categoria -> {
            categoria.setNombre(categoriaActualizada.getNombre());
            categoria.setDescripcion(categoriaActualizada.getDescripcion());
            return ResponseEntity.ok(categoriaRepository.save(categoria));
        }).orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("Categoría no encontrada"));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Inactivar una categoría (Borrado lógico por estado)")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return categoriaRepository.findById(id).map(categoria -> {
            categoria.setActivo(false);
            categoriaRepository.save(categoria);
            return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
        }).orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("Categoría no encontrada"));
    }
}