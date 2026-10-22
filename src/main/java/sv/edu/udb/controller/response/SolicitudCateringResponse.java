package sv.edu.udb.controller.response;

import lombok.*;
import sv.edu.udb.model.SolicitudCatering.EstadoSolicitud;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SolicitudCateringResponse {

    private Long id;
    private Long idCliente;
    private Long idServicio;
    private String nombreServicio;
    private Integer numeroAsistentes;
    private String menu;
    private LocalDate fecha;
    private LocalTime hora;
    private String lugar;
    private EstadoSolicitud estado;
    private BigDecimal precioTotal;
    private Long idReservaEspacio;
    private Long idCotizacion;
    private LocalDateTime fechaRegistro;
}