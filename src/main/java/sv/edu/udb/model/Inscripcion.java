package sv.edu.udb.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "inscripciones")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Inscripcion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_inscripcion")
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_curso")
    private Curso curso;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_diplomado")
    private Diplomado diplomado;

    @NotNull
    @Column(name = "fecha_inscripcion", nullable = false)
    private LocalDateTime fechaInscripcion;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 30)
    private EstadoInscripcion estado;

    /*
     * No existe una columna monto en la tabla SQL.
     * Este campo solo se usa en memoria; el costo se deriva del curso
     * o diplomado relacionado.
     */
    @Transient
    private BigDecimal monto;

    @Builder.Default
    @Column(name = "activo", nullable = false)
    private Boolean activo = true;

    public BigDecimal getMonto() {
        if (curso != null) {
            return curso.getCosto();
        }

        if (diplomado != null) {
            return diplomado.getCosto();
        }

        return monto;
    }

    public enum EstadoInscripcion {
        PENDIENTE,
        CONFIRMADA,
        CANCELADA,
        FINALIZADA
    }
}