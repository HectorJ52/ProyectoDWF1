package sv.edu.udb.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sv.edu.udb.controller.response.UsuarioResponse;
import sv.edu.udb.model.Usuario;
import sv.edu.udb.service.UsuarioService;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
@Tag(name = "Usuarios", description = "Endpoints para la gestión de usuarios")
public class UsuarioController {
    private final UsuarioService usuarioService;

    @GetMapping
    @Operation(summary = "Listar todos los usuarios")
    public ResponseEntity<Page<UsuarioResponse>> getAll(
            @ParameterObject @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return ResponseEntity.ok(usuarioService.findAll(pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar un usuario por ID")
    public ResponseEntity<UsuarioResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Registrar un nuevo usuario")
    public ResponseEntity<UsuarioResponse> create(@Valid @RequestBody Usuario usuario) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.save(usuario));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un usuario")
    public ResponseEntity<UsuarioResponse> update(@PathVariable Long id, @Valid @RequestBody Usuario usuario) {
        return ResponseEntity.ok(usuarioService.update(id, usuario));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Desactivar un usuario")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        usuarioService.delete(id);
        return ResponseEntity.noContent().build();
    }
}