package sv.edu.udb.controller.response;

import lombok.*;
import sv.edu.udb.model.DetalleCotizacion;

import java.math.BigDecimal;

@Getter @Setter @Builder
@AllArgsConstructor @NoArgsConstructor
public class DetalleCotizacionResponse {
    private Long id;
    private Long idCotizacion;
    private DetalleCotizacion.TipoItem tipoItem;
    private Long idReferencia;
    private Integer cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;
}
