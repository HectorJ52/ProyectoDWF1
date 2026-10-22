package sv.edu.udb.controller.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InscripcionResponse {

    private Long id;
    private Long idCliente;
    private String nombreCliente;
    private Long idCurso;
    private String nombreCurso;
    private Long idDiplomado;
    private String nombreDiplomado;
    private LocalDateTime fechaInscripcion;
    private String estado;
    private BigDecimal monto;
    private Boolean activo;
}