package sv.edu.udb.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "detalle_cotizacion")
@Getter @Setter @Builder
@AllArgsConstructor @NoArgsConstructor
public class DetalleCotizacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle")
    private Long id;

    @NotNull(message = "La cotización es obligatoria")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cotizacion", nullable = false)
    private Cotizacion cotizacion;

    @NotNull(message = "El tipo de ítem es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_item", nullable = false, length = 30)
    private TipoItem tipoItem;

    @NotNull(message = "La referencia del ítem es obligatoria")
    @Positive(message = "La referencia del ítem debe ser positiva")
    @Column(name = "id_referencia", nullable = false)
    private Long idReferencia;

    @NotNull(message = "La cantidad es obligatoria")
    @Positive(message = "La cantidad debe ser positiva")
    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    @NotNull(message = "El precio unitario es obligatorio")
    @DecimalMin(value = "0.0", inclusive = true, message = "El precio unitario no puede ser negativo")
    @Column(name = "precio_unitario", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioUnitario;

    @NotNull(message = "El subtotal es obligatorio")
    @DecimalMin(value = "0.0", inclusive = true, message = "El subtotal no puede ser negativo")
    @Column(name = "subtotal", nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal;

    public enum TipoItem {
        CURSO,
        DIPLOMADO,
        ESPACIO,
        CATERING
    }
}