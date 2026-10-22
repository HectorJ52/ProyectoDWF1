package sv.edu.udb.controller.response;

import lombok.*;
import java.math.BigDecimal;

@Getter @Setter @Builder
@AllArgsConstructor @NoArgsConstructor
public class ServicioCateringResponse {
    private Long id;
    private String nombre;
    private String descripcion;
    private BigDecimal precioPersona;
    private Boolean activo;
}