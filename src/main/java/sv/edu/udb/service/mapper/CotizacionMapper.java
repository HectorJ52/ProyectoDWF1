package sv.edu.udb.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import sv.edu.udb.controller.response.CotizacionResponse;
import sv.edu.udb.model.Cotizacion;

@Mapper(componentModel = "spring")
public interface CotizacionMapper {

    @Mapping(target = "idCliente", source = "cliente.id")
    CotizacionResponse toResponse(Cotizacion data);
}