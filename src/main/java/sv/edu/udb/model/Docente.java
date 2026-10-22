package sv.edu.udb.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "docentes")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Docente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_docente")
    private Long id;

    @NotBlank
    @Size(max = 100)
    @Column(name = "nombres", nullable = false, length = 100)
    private String nombres;

    @NotBlank
    @Size(max = 100)
    @Column(name = "apellidos", nullable = false, length = 100)
    private String apellidos;

    @NotBlank
    @Size(max = 20)
    @Column(name = "documento", nullable = false, unique = true, length = 20)
    private String documento;

    @NotBlank
    @Email
    @Size(max = 150)
    @Column(name = "correo", nullable = false, unique = true, length = 150)
    private String correo;
}