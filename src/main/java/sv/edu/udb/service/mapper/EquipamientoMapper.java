package sv.edu.udb.service.mapper;

import org.mapstruct.Mapper;
import sv.edu.udb.controller.response.EquipamientoResponse;
import sv.edu.udb.model.Equipamiento;

@Mapper(componentModel = "spring")
public interface EquipamientoMapper {
    
    EquipamientoResponse toResponse(Equipamiento data);
}