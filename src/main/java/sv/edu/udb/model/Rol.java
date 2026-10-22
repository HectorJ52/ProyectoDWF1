package sv.edu.udb.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "roles")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Rol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rol")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "nombre", nullable = false, unique = true, length = 50)
    private NombreRol nombre;

    public enum NombreRol {
        ADMIN,
        USER,
        RECEPCIONISTA,
        CLIENTE,
        CONTABILIDAD
    }
}