package sv.edu.udb.implementation;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sv.edu.udb.controller.response.CursoResponse;
import sv.edu.udb.model.*;
import sv.edu.udb.repository.*;
import sv.edu.udb.service.CursoService;
import sv.edu.udb.service.mapper.CursoMapper;

@Service
@RequiredArgsConstructor
public class CursoServiceImpl implements CursoService {

    private final CursoRepository cursoRepository;
    private final CategoriaRepository categoriaRepository;
    private final ModalidadRepository modalidadRepository;
    private final DocenteRepository docenteRepository;
    private final CursoMapper cursoMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<CursoResponse> findAllPaginated(Long idCategoria, Long idModalidad, Boolean activo, Pageable pageable) {
        if (idCategoria != null && idModalidad != null) {
            return cursoRepository.findByActivoAndCategoriaIdAndModalidadId(activo, idCategoria, idModalidad, pageable)
                    .map(cursoMapper::toCursoResponse);
        }
        return cursoRepository.findByActivo(activo, pageable).map(cursoMapper::toCursoResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public CursoResponse findById(Long id) {
        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Curso no encontrado con ID: " + id));
        return cursoMapper.toCursoResponse(curso);
    }

    @Override
    @Transactional
    public CursoResponse save(Curso curso) {
        if (curso.getCategoria() == null || curso.getCategoria().getId() == null) {
            throw new IllegalArgumentException("Debe proporcionar una categoría válida");
        }
        if (curso.getModalidad() == null || curso.getModalidad().getId() == null) {
            throw new IllegalArgumentException("Debe proporcionar una modalidad válida");
        }

        Categoria cat = categoriaRepository.findById(curso.getCategoria().getId())
                .orElseThrow(() -> new EntityNotFoundException("Categoría no encontrada"));
        Modalidad mod = modalidadRepository.findById(curso.getModalidad().getId())
                .orElseThrow(() -> new EntityNotFoundException("Modalidad no encontrada"));

        Docente doc = null;
        if (curso.getDocente() != null && curso.getDocente().getId() != null) {
            doc = docenteRepository.findById(curso.getDocente().getId())
                    .orElseThrow(() -> new EntityNotFoundException("Docente no encontrado"));
        }

        curso.setCategoria(cat);
        curso.setModalidad(mod);
        curso.setDocente(doc);
        curso.setActivo(true);

        return cursoMapper.toCursoResponse(cursoRepository.save(curso));
    }

    @Override
    @Transactional
    public CursoResponse update(Long id, Curso curso) {
        Curso cursoExistente = cursoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Curso no encontrado"));

        if (curso.getCategoria() == null || curso.getCategoria().getId() == null) {
            throw new IllegalArgumentException("Debe proporcionar una categoría válida");
        }
        if (curso.getModalidad() == null || curso.getModalidad().getId() == null) {
            throw new IllegalArgumentException("Debe proporcionar una modalidad válida");
        }

        Categoria cat = categoriaRepository.findById(curso.getCategoria().getId())
                .orElseThrow(() -> new EntityNotFoundException("Categoría no encontrada"));
        Modalidad mod = modalidadRepository.findById(curso.getModalidad().getId())
                .orElseThrow(() -> new EntityNotFoundException("Modalidad no encontrada"));

        if (curso.getDocente() != null && curso.getDocente().getId() != null) {
            Docente doc = docenteRepository.findById(curso.getDocente().getId())
                    .orElseThrow(() -> new EntityNotFoundException("Docente no encontrado"));
            cursoExistente.setDocente(doc);
        } else {
            cursoExistente.setDocente(null);
        }

        cursoExistente.setCodigo(curso.getCodigo());
        cursoExistente.setNombre(curso.getNombre());
        cursoExistente.setDescripcion(curso.getDescripcion());
        cursoExistente.setCategoria(cat);
        cursoExistente.setModalidad(mod);
        cursoExistente.setCupoMaximo(curso.getCupoMaximo());
        cursoExistente.setCosto(curso.getCosto());
        cursoExistente.setFechaInicio(curso.getFechaInicio());
        cursoExistente.setFechaFin(curso.getFechaFin());
        cursoExistente.setHorario(curso.getHorario());

        return cursoMapper.toCursoResponse(cursoRepository.save(cursoExistente));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Curso no encontrado"));
        curso.setActivo(false);
        cursoRepository.save(curso);
    }
}