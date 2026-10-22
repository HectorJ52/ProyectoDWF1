package sv.edu.udb.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import sv.edu.udb.controller.response.DiplomadoResponse;
import sv.edu.udb.model.Diplomado;

@Mapper(componentModel = "spring")
public interface DiplomadoMapper {
    
    @Mapping(target = "idCategoria", source = "categoria.id")
    @Mapping(target = "nombreCategoria", source = "categoria.nombre")
    @Mapping(target = "idModalidad", source = "modalidad.id")
    @Mapping(target = "nombreModalidad", source = "modalidad.nombre")
    @Mapping(target = "idDocente", source = "docente.id")
    @Mapping(target = "nombreCompletoDocente", expression = "java(data.getDocente() != null ? data.getDocente().getNombres() + \" \" + data.getDocente().getApellidos() : null)")
    DiplomadoResponse toDiplomadoResponse(Diplomado data);
}