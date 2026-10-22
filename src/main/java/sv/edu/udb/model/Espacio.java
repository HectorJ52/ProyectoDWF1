package sv.edu.udb.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "espacios")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Espacio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_espacio")
    private Long id;

    @NotBlank
    @Size(max = 100)
    @Column(name = "nombre", nullable = false, unique = true, length = 100)
    private String nombre;

    @Column(name = "descripcion", columnDefinition = "varchar(max)")
    private String descripcion;

    @NotNull
    @Positive
    @Column(name = "capacidad", nullable = false)
    private Integer capacidad;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_espacio", nullable = false, length = 50)
    private TipoEspacio tipoEspacio;

    @Builder.Default
    @Column(name = "activo", nullable = false)
    private Boolean activo = true;

    @ManyToMany
    @JoinTable(
        name = "espacio_equipamiento",
        joinColumns = @JoinColumn(name = "id_espacio"),
        inverseJoinColumns = @JoinColumn(name = "id_equipamiento")
    )
    @Builder.Default
    private Set<Equipamiento> equipamientos = new HashSet<>();

    public enum TipoEspacio {
        AUDITORIO,
        SALA_REUNIONES,
        LABORATORIO,
        AULA,
        SALA_MULTIMEDIA
    }
}