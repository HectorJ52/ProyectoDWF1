package sv.edu.udb.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "modalidades")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Modalidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_modalidad")
    private Long id;

    @NotBlank
    @Size(max = 100)
    @Column(name = "nombre", nullable = false, unique = true, length = 100)
    private String nombre;

    @Size(max = 255)
    @Column(name = "descripcion", length = 255)
    private String descripcion;
}