package sv.edu.udb.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import sv.edu.udb.model.Docente;
import sv.edu.udb.repository.DocenteRepository;

@RestController
@RequestMapping("/api/docentes")
@RequiredArgsConstructor
@Tag(name = "Docentes", description = "Gestión de docentes")
public class DocenteController {

    private final DocenteRepository docenteRepository;

    @GetMapping
    @Operation(summary = "Listar docentes")
    public ResponseEntity<Page<Docente>> getAll(
            @PageableDefault(size = 10, sort = "apellidos")
            Pageable pageable) {

        return ResponseEntity.ok(docenteRepository.findAll(pageable));
    }

    @PostMapping
    @Operation(summary = "Registrar un docente")
    public ResponseEntity<Docente> create(
            @Valid @RequestBody Docente docente) {

        return new ResponseEntity<>(
                docenteRepository.save(docente),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener docente por ID")
    public ResponseEntity<Docente> getById(@PathVariable Long id) {

        Docente docente = docenteRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Docente no encontrado con ID: " + id
                        )
                );

        return ResponseEntity.ok(docente);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un docente")
    public ResponseEntity<Docente> update(
            @PathVariable Long id,
            @Valid @RequestBody Docente actualizado) {

        Docente docente = docenteRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Docente no encontrado con ID: " + id
                        )
                );

        docente.setNombres(actualizado.getNombres());
        docente.setApellidos(actualizado.getApellidos());
        docente.setDocumento(actualizado.getDocumento());
        docente.setCorreo(actualizado.getCorreo());

        return ResponseEntity.ok(docenteRepository.save(docente));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un docente")
    public ResponseEntity<Void> delete(@PathVariable Long id) {

        if (!docenteRepository.existsById(id)) {
            throw new EntityNotFoundException(
                    "Docente no encontrado con ID: " + id
            );
        }

        docenteRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}