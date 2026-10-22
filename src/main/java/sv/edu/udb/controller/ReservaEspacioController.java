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
import sv.edu.udb.controller.response.ReservaEspacioResponse;
import sv.edu.udb.model.ReservaEspacio; // 🟢 Importamos el modelo centralizado
import sv.edu.udb.model.ReservaEspacio.EstadoReserva;
import sv.edu.udb.service.ReservaEspacioService;
import java.time.LocalDate;
import java.time.LocalTime;

@RestController
@RequestMapping("/api/reservas-espacio")
@RequiredArgsConstructor
@Tag(name = "Reservas de Espacio", description = "Gestión de alquileres y reservas de espacios físicos")
public class ReservaEspacioController {
    private final ReservaEspacioService service;

    @GetMapping
    @Operation(summary = "Listar reservas con filtros y paginación")
    public ResponseEntity<Page<ReservaEspacioResponse>> getAll(
            @RequestParam(required = false) EstadoReserva estado,
            @RequestParam(required = false) Long idEspacio,
            @RequestParam(required = false) Long idCliente,
            @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return ResponseEntity.ok(service.findAll(estado, idEspacio, idCliente, pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener reserva por ID")
    public ResponseEntity<ReservaEspacioResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    @Operation(summary = "Crear una nueva reserva (valida solapamientos de horario)")
    public ResponseEntity<ReservaEspacioResponse> create(@Valid @RequestBody ReservaEspacio reserva) { // 🟢 Usa la Entidad directo
        return new ResponseEntity<>(service.save(reserva), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una reserva")
    public ResponseEntity<ReservaEspacioResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody ReservaEspacio reserva) { // 🟢 Usa la Entidad directo
        return ResponseEntity.ok(service.update(id, reserva));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una reserva")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/disponibilidad")
    @Operation(summary = "Verificar si un espacio está disponible en un rango de fechas y horas")
    public ResponseEntity<Boolean> verificarDisponibilidad(
            @RequestParam Long idEspacio,
            @RequestParam LocalDate fechaInicio,
            @RequestParam LocalDate fechaFin,
            @RequestParam LocalTime horaInicio,
            @RequestParam LocalTime horaFin) {
        return ResponseEntity.ok(
                service.verificarDisponibilidad(idEspacio, fechaInicio, fechaFin, horaInicio, horaFin)
        );
    }
}