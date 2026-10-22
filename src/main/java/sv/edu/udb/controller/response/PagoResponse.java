package sv.edu.udb.controller.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PagoResponse {

    private Long id;
    private Long idInscripcion;
    private Long idCotizacion;
    private BigDecimal monto;
    private LocalDateTime fechaPago;
    private String metodoPago;
    private String estado;
}