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
import sv.edu.udb.controller.response.ClienteResponse;
import sv.edu.udb.model.Cliente;
import sv.edu.udb.service.ClienteService;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
@Tag(name = "Clientes", description = "Endpoints para el mantenimiento de Clientes")
public class ClienteController {

    private final ClienteService clienteService;

    @GetMapping
    @Operation(summary = "Listar clientes de forma paginada y ordenada")
    public ResponseEntity<Page<ClienteResponse>> getAll(
            @RequestParam(defaultValue = "true") Boolean activo,
            @PageableDefault(size = 10, sort = "apellidos") Pageable pageable) {
        return ResponseEntity.ok(clienteService.findAllPaginated(activo, pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener cliente por ID")
    public ResponseEntity<ClienteResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(clienteService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Registrar un nuevo cliente")
    public ResponseEntity<ClienteResponse> create(@Valid @RequestBody Cliente cliente) {
        return new ResponseEntity<>(clienteService.save(cliente), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un cliente existente")
    public ResponseEntity<ClienteResponse> update(@PathVariable Long id, @Valid @RequestBody Cliente cliente) {
        return ResponseEntity.ok(clienteService.update(id, cliente));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Inactivar un cliente (borrado lógico)")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        clienteService.delete(id);
        return ResponseEntity.noContent().build();
    }
}