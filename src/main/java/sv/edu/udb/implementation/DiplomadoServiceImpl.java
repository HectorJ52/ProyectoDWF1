package sv.edu.udb.implementation;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sv.edu.udb.controller.response.DiplomadoResponse;
import sv.edu.udb.model.*;
import sv.edu.udb.repository.*;
import sv.edu.udb.service.DiplomadoService;
import sv.edu.udb.service.mapper.DiplomadoMapper;

@Service
@RequiredArgsConstructor
public class DiplomadoServiceImpl implements DiplomadoService {

    private final DiplomadoRepository diplomadoRepository;
    private final CategoriaRepository categoriaRepository;
    private final ModalidadRepository modalidadRepository;
    private final DocenteRepository docenteRepository;
    private final DiplomadoMapper diplomadoMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<DiplomadoResponse> findAllPaginated(Long idCategoria, Long idModalidad, Boolean activo, Pageable pageable) {
        if (idCategoria != null && idModalidad != null) {
            return diplomadoRepository.findByActivoAndCategoriaIdAndModalidadId(activo, idCategoria, idModalidad, pageable)
                    .map(diplomadoMapper::toDiplomadoResponse);
        }
        return diplomadoRepository.findByActivo(activo, pageable).map(diplomadoMapper::toDiplomadoResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public DiplomadoResponse findById(Long id) {
        Diplomado diplomado = diplomadoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Diplomado no encontrado"));
        return diplomadoMapper.toDiplomadoResponse(diplomado);
    }

    @Override
    @Transactional
    public DiplomadoResponse save(Diplomado diplomado) { // 🟢 Modificado: Parámetro mapeado a Entidad
        if (diplomado.getCategoria() == null || diplomado.getCategoria().getId() == null) {
            throw new IllegalArgumentException("Debe proporcionar una categoría válida");
        }
        if (diplomado.getModalidad() == null || diplomado.getModalidad().getId() == null) {
            throw new IllegalArgumentException("Debe proporcionar una modalidad válida");
        }

        Categoria cat = categoriaRepository.findById(diplomado.getCategoria().getId())
                .orElseThrow(() -> new EntityNotFoundException("Categoría no encontrada"));
        Modalidad mod = modalidadRepository.findById(diplomado.getModalidad().getId())
                .orElseThrow(() -> new EntityNotFoundException("Modalidad no encontrada"));

        Docente doc = null;
        if (diplomado.getDocente() != null && diplomado.getDocente().getId() != null) {
            doc = docenteRepository.findById(diplomado.getDocente().getId())
                    .orElseThrow(() -> new EntityNotFoundException("Docente no encontrado"));
        }

        diplomado.setCategoria(cat);
        diplomado.setModalidad(mod);
        diplomado.setDocente(doc);
        diplomado.setActivo(true);

        return diplomadoMapper.toDiplomadoResponse(diplomadoRepository.save(diplomado));
    }

    @Override
    @Transactional
    public DiplomadoResponse update(Long id, Diplomado diplomado) {
        Diplomado diplomadoExistente = diplomadoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Diplomado no encontrado"));

        if (diplomado.getCategoria() == null || diplomado.getCategoria().getId() == null) {
            throw new IllegalArgumentException("Debe proporcionar una categoría válida");
        }
        if (diplomado.getModalidad() == null || diplomado.getModalidad().getId() == null) {
            throw new IllegalArgumentException("Debe proporcionar una modalidad válida");
        }

        Categoria cat = categoriaRepository.findById(diplomado.getCategoria().getId())
                .orElseThrow(() -> new EntityNotFoundException("Categoría no encontrada"));
        Modalidad mod = modalidadRepository.findById(diplomado.getModalidad().getId())
                .orElseThrow(() -> new EntityNotFoundException("Modalidad no encontrada"));

        if (diplomado.getDocente() != null && diplomado.getDocente().getId() != null) {
            Docente doc = docenteRepository.findById(diplomado.getDocente().getId())
                    .orElseThrow(() -> new EntityNotFoundException("Docente no encontrado"));
            diplomadoExistente.setDocente(doc);
        } else {
            diplomadoExistente.setDocente(null);
        }

        diplomadoExistente.setCodigo(diplomado.getCodigo());
        diplomadoExistente.setNombre(diplomado.getNombre());
        diplomadoExistente.setDescripcion(diplomado.getDescripcion());
        diplomadoExistente.setCategoria(cat);
        diplomadoExistente.setModalidad(mod);
        diplomadoExistente.setCupoMaximo(diplomado.getCupoMaximo());
        diplomadoExistente.setCosto(diplomado.getCosto());
        diplomadoExistente.setFechaInicio(diplomado.getFechaInicio());
        diplomadoExistente.setFechaFin(diplomado.getFechaFin());
        diplomadoExistente.setHorario(diplomado.getHorario());
        
        return diplomadoMapper.toDiplomadoResponse(diplomadoRepository.save(diplomadoExistente));
    }
    
    @Override
    @Transactional
    public void delete(Long id) {
        Diplomado diplomado = diplomadoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Diplomado no encontrado"));
        diplomado.setActivo(false); 
        diplomadoRepository.save(diplomado);
    }
}