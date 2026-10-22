package sv.edu.udb.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import sv.edu.udb.controller.response.EspacioResponse;
import sv.edu.udb.model.Espacio;
import sv.edu.udb.model.Equipamiento;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface EspacioMapper {

    @Mapping(target = "idsEquipamientos", expression = "java(mapEquipamientos(espacio.getEquipamientos()))")
    EspacioResponse toResponse(Espacio espacio);

    default Set<Long> mapEquipamientos(Set<Equipamiento> equipamientos) {
        if (equipamientos == null) return Set.of();
        return equipamientos.stream()
                .map(Equipamiento::getId)
                .collect(Collectors.toSet());
    }
}