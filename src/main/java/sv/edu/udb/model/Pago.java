package sv.edu.udb.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pagos")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pago")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_inscripcion")
    private Inscripcion inscripcion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cotizacion")
    private Cotizacion cotizacion;

    @NotNull
    @DecimalMin(value = "0.0")
    @Column(name = "monto", nullable = false, precision = 10, scale = 2)
    private BigDecimal monto;

    @NotNull
    @Column(name = "fecha_pago", nullable = false)
    private LocalDateTime fechaPago;

    @NotBlank
    @Column(name = "metodo_pago", nullable = false, length = 30)
    private String metodoPago;

    @NotBlank
    @Column(name = "estado", nullable = false, length = 30)
    private String estado;
}