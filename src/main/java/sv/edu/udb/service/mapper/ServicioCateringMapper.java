package sv.edu.udb.service.mapper;

import org.mapstruct.Mapper;
import sv.edu.udb.controller.response.ServicioCateringResponse;
import sv.edu.udb.model.ServicioCatering;

@Mapper(componentModel = "spring")
public interface ServicioCateringMapper {
    ServicioCateringResponse toResponse(ServicioCatering data);
}