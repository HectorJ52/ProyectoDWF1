package sv.edu.udb.controller.response;

import lombok.*;
import sv.edu.udb.model.Espacio.TipoEspacio;

import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EspacioResponse {

    private Long id;
    private String nombre;
    private TipoEspacio tipoEspacio;
    private Integer capacidad;
    private String descripcion;
    private Boolean activo;
    private Set<Long> idsEquipamientos;
}