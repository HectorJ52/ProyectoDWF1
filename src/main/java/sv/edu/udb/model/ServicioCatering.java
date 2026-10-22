package sv.edu.udb.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "servicios_catering")
@Getter @Setter @Builder
@AllArgsConstructor @NoArgsConstructor
public class ServicioCatering {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_servicio")
    private Long id;

    @NotBlank(message = "El nombre del servicio es obligatorio")
    @Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
    @Column(name = "nombre", nullable = false, unique = true, length = 100)
    private String nombre;

    @Column(name = "descripcion", columnDefinition = "varchar(max)")
    private String descripcion;

    @NotNull(message = "El precio por persona es obligatorio")
    @DecimalMin(value = "0.0", inclusive = true, message = "El precio no puede ser negativo")
    @Column(name = "precio_persona", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioPersona;

    @Column(name = "activo", nullable = false)
    private Boolean activo = true;
}