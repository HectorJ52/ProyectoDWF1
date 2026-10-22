package sv.edu.udb.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "cursos")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_curso")
    private Long id;

    @NotBlank
    @Size(max = 20)
    @Column(name = "codigo", nullable = false, unique = true, length = 20)
    private String codigo;

    @NotBlank
    @Size(max = 150)
    @Column(name = "nombre", nullable = false, length = 150)
    private String nombre;

    @Column(name = "descripcion", columnDefinition = "varchar(max)")
    private String descripcion;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_categoria", nullable = false)
    private Categoria categoria;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_modalidad", nullable = false)
    private Modalidad modalidad;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_docente")
    private Docente docente;

    @NotNull
    @Positive
    @Column(name = "cupo_maximo", nullable = false)
    private Integer cupoMaximo;

    @NotNull
    @DecimalMin(value = "0.0")
    @Column(name = "costo", nullable = false, precision = 10, scale = 2)
    private BigDecimal costo;

    @NotNull
    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @NotNull
    @Column(name = "fecha_fin", nullable = false)
    private LocalDate fechaFin;

    @Size(max = 100)
    @Column(name = "horario", length = 100)
    private String horario;

    @Builder.Default
    @Column(name = "activo", nullable = false)
    private Boolean activo = true;
}