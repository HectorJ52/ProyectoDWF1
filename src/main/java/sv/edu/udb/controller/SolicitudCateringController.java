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

import sv.edu.udb.controller.response.SolicitudCateringResponse;
import sv.edu.udb.model.SolicitudCatering;
import sv.edu.udb.model.SolicitudCatering.EstadoSolicitud;
import sv.edu.udb.service.SolicitudCateringService;

@RestController
@RequestMapping("/api/solicitudes-catering")
@RequiredArgsConstructor
@Tag(
    name = "Solicitudes de Catering",
    description = "Gestión de solicitudes de servicio de alimentación"
)
public class SolicitudCateringController {

    private final SolicitudCateringService service;

    @GetMapping
    @Operation(summary = "Listar solicitudes de catering con filtros")
    public ResponseEntity<Page<SolicitudCateringResponse>> getAll(
            @RequestParam(required = false) EstadoSolicitud estado,
            @RequestParam(required = false) Long idCliente,
            @RequestParam(required = false) Long idServicio,
            @PageableDefault(size = 10, sort = "id")
            Pageable pageable) {

        return ResponseEntity.ok(
                service.findAll(
                        estado,
                        idCliente,
                        idServicio,
                        pageable
                )
        );
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar solicitud de catering por ID")
    public ResponseEntity<SolicitudCateringResponse> getById(
            @PathVariable Long id) {

        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    @Operation(summary = "Registrar una solicitud de catering")
    public ResponseEntity<SolicitudCateringResponse> create(
            @Valid @RequestBody SolicitudCatering solicitud) {

        return new ResponseEntity<>(
                service.save(solicitud),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una solicitud de catering")
    public ResponseEntity<SolicitudCateringResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody SolicitudCatering solicitud) {

        return ResponseEntity.ok(
                service.update(id, solicitud)
        );
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una solicitud de catering")
    public ResponseEntity<Void> delete(@PathVariable Long id) {

        service.delete(id);

        return ResponseEntity.noContent().build();
    }
}