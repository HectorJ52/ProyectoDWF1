package sv.edu.udb.controller.response;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter @Setter @Builder
@AllArgsConstructor @NoArgsConstructor
public class CursoResponse {
    private Long id;
    private String codigo;
    private String nombre;
    private String descripcion;
    private Long idCategoria;
    private String nombreCategoria;
    private Long idModalidad;
    private String nombreModalidad;
    private Long idDocente;
    private String nombreCompletoDocente;
    private Integer cupoMaximo;
    private BigDecimal costo;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String horario;
    private Boolean activo;
}