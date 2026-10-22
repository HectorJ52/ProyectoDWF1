package sv.edu.udb.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sv.edu.udb.controller.response.CursoResponse;
import sv.edu.udb.model.Curso;

public interface CursoService {
    Page<CursoResponse> findAllPaginated(Long idCategoria, Long idModalidad, Boolean activo, Pageable pageable);
    CursoResponse findById(Long id);
    CursoResponse save(Curso curso);
    CursoResponse update(Long id, Curso curso);
    void delete(Long id);
}