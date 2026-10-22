package sv.edu.udb.controller.response;

import lombok.*;

@Getter @Setter @Builder
@AllArgsConstructor @NoArgsConstructor
public class ClienteResponse {
    private Long id;
    private String nombres;
    private String apellidos;
    private String documento;
    private String correo;
    private String telefono;
    private String direccion;
    private Boolean activo;
}