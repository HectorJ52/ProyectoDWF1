package sv.edu.udb.controller.response;

import lombok.*;
import sv.edu.udb.model.Cotizacion;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CotizacionResponse {

    private Long id;
    private Long idCliente;
    private LocalDateTime fechaCotizacion;
    private Cotizacion.EstadoCotizacion estado;
    private BigDecimal total;
}