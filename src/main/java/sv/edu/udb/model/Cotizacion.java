package sv.edu.udb.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "cotizaciones")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Cotizacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cotizacion")
    private Long id;

    @NotNull(message = "El cliente es obligatorio")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;

    @NotNull(message = "La fecha de cotización es obligatoria")
    @Column(name = "fecha_cotizacion", nullable = false)
    private LocalDateTime fechaCotizacion;

    @NotNull(message = "El estado es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 30)
    private EstadoCotizacion estado;

    @NotNull(message = "El total es obligatorio")
    @DecimalMin(value = "0.0")
    @Column(name = "monto_total", nullable = false, precision = 12, scale = 2)
    private BigDecimal total;

    public enum EstadoCotizacion {
        PENDIENTE,
        EN_PROCESO,
        APROBADA,
        RECHAZADA
    }
}