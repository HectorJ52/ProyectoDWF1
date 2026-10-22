package sv.edu.udb.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import sv.edu.udb.controller.response.ReservaEspacioResponse;
import sv.edu.udb.model.ReservaEspacio;

@Mapper(componentModel = "spring")
public interface ReservaEspacioMapper {

    @Mapping(target = "idCliente", source = "cliente.id")
    @Mapping(target = "idEspacio", source = "espacio.id")
    @Mapping(target = "nombreEspacio", source = "espacio.nombre")
    ReservaEspacioResponse toResponse(ReservaEspacio data);
}