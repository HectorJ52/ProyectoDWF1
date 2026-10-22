package sv.edu.udb.service.mapper;

import org.mapstruct.Mapper;
import sv.edu.udb.controller.response.ClienteResponse;
import sv.edu.udb.model.Cliente;

@Mapper(componentModel = "spring")
public interface ClienteMapper {

    ClienteResponse toClienteResponse(Cliente data);
}