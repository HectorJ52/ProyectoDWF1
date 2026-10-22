package sv.edu.udb.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

@Entity
@Table(name = "solicitudes_catering")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SolicitudCatering {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_solicitud")
    private Long id;

    @NotNull(message = "El cliente es obligatorio")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;

    @NotNull(message = "El servicio de catering es obligatorio")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_servicio", nullable = false)
    private ServicioCatering servicio;

    @NotNull(message = "El número de asistentes es obligatorio")
    @Positive(message = "El número de asistentes debe ser positivo")
    @Column(name = "numero_asistentes", nullable = false)
    private Integer numeroAsistentes;

    @Column(name = "menu", columnDefinition = "varchar(max)")
    private String menu;

    @NotNull(message = "La fecha es obligatoria")
    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @NotNull(message = "La hora es obligatoria")
    @Column(name = "hora", nullable = false)
    private LocalTime hora;

    @Size(max = 150, message = "El lugar no puede superar 150 caracteres")
    @Column(name = "lugar", length = 150)
    private String lugar;

    @NotNull(message = "El estado es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private EstadoSolicitud estado;

    @NotNull(message = "El precio total es obligatorio")
    @DecimalMin(value = "0.0", message = "El precio no puede ser negativo")
    @Column(name = "precio_total", nullable = false,
            precision = 12, scale = 2)
    private BigDecimal precioTotal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_reserva_espacio")
    private ReservaEspacio reservaEspacio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cotizacion")
    private Cotizacion cotizacion;

    @Builder.Default
    @Column(name = "fecha_registro", nullable = false)
    private LocalDateTime fechaRegistro = LocalDateTime.now();

    public enum EstadoSolicitud {
        PENDIENTE,
        CONFIRMADA,
        CANCELADA,
        FINALIZADA
    }
}