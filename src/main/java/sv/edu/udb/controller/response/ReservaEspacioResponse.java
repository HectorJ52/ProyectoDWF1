package sv.edu.udb.controller.response;

import lombok.*;
import sv.edu.udb.model.ReservaEspacio.EstadoReserva;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservaEspacioResponse {

    private Long id;
    private Long idCliente;
    private Long idEspacio;
    private String nombreEspacio;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private String motivo;
    private BigDecimal montoTotal;
    private EstadoReserva estado;
    private LocalDateTime fechaRegistro;
}