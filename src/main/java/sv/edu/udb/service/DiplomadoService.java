package sv.edu.udb.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sv.edu.udb.controller.response.DiplomadoResponse;
import sv.edu.udb.model.Diplomado;

public interface DiplomadoService {
    Page<DiplomadoResponse> findAllPaginated(Long idCategoria, Long idModalidad, Boolean activo, Pageable pageable);
    DiplomadoResponse findById(Long id);
    DiplomadoResponse save(Diplomado diplomado);
    DiplomadoResponse update(Long id, Diplomado diplomado);
    void delete(Long id);
}