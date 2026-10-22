package sv.edu.udb.controller.response;

import lombok.*;

@Getter @Setter @Builder
@AllArgsConstructor @NoArgsConstructor
public class EquipamientoResponse {
    private Long id;
    private String nombre;
    private String descripcion;
}