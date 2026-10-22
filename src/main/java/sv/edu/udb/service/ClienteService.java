package sv.edu.udb.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sv.edu.udb.controller.response.ClienteResponse;
import sv.edu.udb.model.Cliente;

public interface ClienteService {
    Page<ClienteResponse> findAllPaginated(Boolean activo, Pageable pageable);
    ClienteResponse findById(Long id);
    ClienteResponse save(Cliente cliente);
    ClienteResponse update(Long id, Cliente cliente);
    void delete(Long id);
}