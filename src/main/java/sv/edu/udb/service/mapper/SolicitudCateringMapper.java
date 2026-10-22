package sv.edu.udb.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import sv.edu.udb.controller.response.SolicitudCateringResponse;
import sv.edu.udb.model.SolicitudCatering;

@Mapper(componentModel = "spring")
public interface SolicitudCateringMapper {

    @Mapping(target = "idCliente", source = "cliente.id")
    @Mapping(target = "idServicio", source = "servicio.id")
    @Mapping(target = "nombreServicio", source = "servicio.nombre")
    @Mapping(target = "idReservaEspacio", source = "reservaEspacio.id")
    @Mapping(target = "idCotizacion", source = "cotizacion.id")
    SolicitudCateringResponse toResponse(SolicitudCatering data);
}