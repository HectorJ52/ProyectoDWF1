package sv.edu.udb.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import sv.edu.udb.controller.response.DetalleCotizacionResponse;
import sv.edu.udb.model.DetalleCotizacion;

@Mapper(componentModel = "spring")
public interface DetalleCotizacionMapper {

    @Mapping(target = "idCotizacion", source = "cotizacion.id")
    DetalleCotizacionResponse toResponse(DetalleCotizacion data);
}