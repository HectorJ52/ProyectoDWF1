package sv.edu.udb.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sv.edu.udb.controller.response.InscripcionResponse;
import sv.edu.udb.model.Inscripcion;

public interface InscripcionService {
    Page<InscripcionResponse> findAllPaginated(Boolean activo, Long idCliente, Pageable pageable);
    InscripcionResponse findById(Long id);
    InscripcionResponse save(Inscripcion inscripcion);
    InscripcionResponse update(Long id, Inscripcion inscripcion);
    void delete(Long id);
}