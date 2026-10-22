package sv.edu.udb.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import sv.edu.udb.controller.response.InscripcionResponse;
import sv.edu.udb.model.Inscripcion;

@Mapper(componentModel = "spring")
public interface InscripcionMapper {

    @Mapping(target = "idCliente", source = "cliente.id")
    @Mapping(
        target = "nombreCliente",
        expression = "java(data.getCliente() != null ? data.getCliente().getNombres() + \" \" + data.getCliente().getApellidos() : null)"
    )
    @Mapping(target = "idCurso", source = "curso.id")
    @Mapping(target = "nombreCurso", source = "curso.nombre")
    @Mapping(target = "idDiplomado", source = "diplomado.id")
    @Mapping(target = "nombreDiplomado", source = "diplomado.nombre")
    @Mapping(
        target = "estado",
        expression = "java(data.getEstado() != null ? data.getEstado().name() : null)"
    )
    @Mapping(target = "monto", expression = "java(data.getMonto())")
    InscripcionResponse toInscripcionResponse(Inscripcion data);
}