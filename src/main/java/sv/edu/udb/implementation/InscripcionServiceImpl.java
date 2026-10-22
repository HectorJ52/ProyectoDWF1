package sv.edu.udb.implementation;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sv.edu.udb.controller.response.InscripcionResponse;
import sv.edu.udb.model.*;
import sv.edu.udb.repository.*;
import sv.edu.udb.service.InscripcionService;
import sv.edu.udb.service.mapper.InscripcionMapper;

@Service
@RequiredArgsConstructor
public class InscripcionServiceImpl implements InscripcionService {

    private final InscripcionRepository inscripcionRepository;
    private final ClienteRepository clienteRepository;
    private final CursoRepository cursoRepository;
    private final DiplomadoRepository diplomadoRepository;
    private final InscripcionMapper inscripcionMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<InscripcionResponse> findAllPaginated(Boolean activo, Long idCliente, Pageable pageable) {
        if (idCliente != null) {
            return inscripcionRepository.findByClienteId(idCliente, pageable)
                    .map(inscripcionMapper::toInscripcionResponse);
        }
        return inscripcionRepository.findByActivo(activo, pageable)
                .map(inscripcionMapper::toInscripcionResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public InscripcionResponse findById(Long id) {
        Inscripcion inscripcion = inscripcionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Inscripción no encontrada con ID: " + id));
        return inscripcionMapper.toInscripcionResponse(inscripcion);
    }

    @Override
    @Transactional
    public InscripcionResponse save(Inscripcion inscripcion) { // 🟢 Modificado: Parámetro mapeado a Entidad
        boolean tieneCurso = inscripcion.getCurso() != null && inscripcion.getCurso().getId() != null;
        boolean tieneDiplomado = inscripcion.getDiplomado() != null && inscripcion.getDiplomado().getId() != null;
        
        // Regla 1: exactamente un Curso o un Diplomado
        if (tieneCurso == tieneDiplomado) {
            throw new IllegalArgumentException("La inscripción debe tener exactamente un Curso o un Diplomado, no ambos ni ninguno");
        }

        if (inscripcion.getCliente() == null || inscripcion.getCliente().getId() == null) {
            throw new IllegalArgumentException("Debe proporcionar un cliente válido");
        }

        // El cliente debe existir y estar activo
        Long idCliente = inscripcion.getCliente().getId();
        Cliente cliente = clienteRepository.findById(idCliente)
                .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado con ID: " + idCliente));
        if (Boolean.FALSE.equals(cliente.getActivo())) {
            throw new IllegalStateException("El cliente está inactivo y no puede inscribirse");
        }

        inscripcion.setCliente(cliente);
        inscripcion.setActivo(true);

        // Validar cupo si es Curso
        if (tieneCurso) {
            Long idCurso = inscripcion.getCurso().getId();
            Curso curso = cursoRepository.findById(idCurso)
                    .orElseThrow(() -> new EntityNotFoundException("Curso no encontrado con ID: " + idCurso));
            if (Boolean.FALSE.equals(curso.getActivo())) {
                throw new IllegalStateException("El curso está inactivo");
            }
            long inscritos = inscripcionRepository.countByCursoIdAndEstadoNot(
                    curso.getId(), Inscripcion.EstadoInscripcion.CANCELADA);
            if (inscritos >= curso.getCupoMaximo()) {
                throw new IllegalStateException("El curso ha alcanzado su cupo máximo de " + curso.getCupoMaximo());
            }
            inscripcion.setCurso(curso);
            inscripcion.setMonto(curso.getCosto());
        }

        // Validar cupo si es Diplomado
        if (tieneDiplomado) {
            Long idDiplomado = inscripcion.getDiplomado().getId();
            Diplomado diplomado = diplomadoRepository.findById(idDiplomado)
                    .orElseThrow(() -> new EntityNotFoundException("Diplomado no encontrado con ID: " + idDiplomado));
            if (Boolean.FALSE.equals(diplomado.getActivo())) {
                throw new IllegalStateException("El diplomado está inactivo");
            }
            long inscritos = inscripcionRepository.countByDiplomadoIdAndEstadoNot(
                    diplomado.getId(), Inscripcion.EstadoInscripcion.CANCELADA);
            if (inscritos >= diplomado.getCupoMaximo()) {
                throw new IllegalStateException("El diplomado ha alcanzado su cupo máximo de " + diplomado.getCupoMaximo());
            }
            inscripcion.setDiplomado(diplomado);
            inscripcion.setMonto(diplomado.getCosto());
        }

        return inscripcionMapper.toInscripcionResponse(inscripcionRepository.save(inscripcion));
    }

    @Override
    @Transactional
    public InscripcionResponse update(Long id, Inscripcion deEntrada) {
        Inscripcion inscripcionExistente = inscripcionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Inscripción no encontrada con ID: " + id));

        boolean tieneCurso = deEntrada.getCurso() != null && deEntrada.getCurso().getId() != null;
        boolean tieneDiplomado = deEntrada.getDiplomado() != null && deEntrada.getDiplomado().getId() != null;
        
        // Exactamente un Curso o un Diplomado
        if (tieneCurso == tieneDiplomado) {
            throw new IllegalArgumentException("La inscripción debe tener exactamente un Curso o un Diplomado, no ambos ni ninguno");
        }

        if (deEntrada.getCliente() == null || deEntrada.getCliente().getId() == null) {
            throw new IllegalArgumentException("Debe proporcionar un cliente válido");
        }

        // Cliente
        Long idCliente = deEntrada.getCliente().getId();
        Cliente cliente = clienteRepository.findById(idCliente)
                .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado con ID: " + idCliente));
        if (Boolean.FALSE.equals(cliente.getActivo())) {
            throw new IllegalStateException("El cliente está inactivo");
        }
        inscripcionExistente.setCliente(cliente);

        // Curso o Diplomado
        if (tieneCurso) {
            Long idCurso = deEntrada.getCurso().getId();
            Curso curso = cursoRepository.findById(idCurso)
                    .orElseThrow(() -> new EntityNotFoundException("Curso no encontrado con ID: " + idCurso));
            if (Boolean.FALSE.equals(curso.getActivo())) {
                throw new IllegalStateException("El curso está inactivo");
            }
            if (inscripcionExistente.getCurso() == null
                    || !inscripcionExistente.getCurso().getId().equals(curso.getId())) {
                long inscritos = inscripcionRepository.countByCursoIdAndEstadoNot(
                        curso.getId(), Inscripcion.EstadoInscripcion.CANCELADA);
                if (inscritos >= curso.getCupoMaximo()) {
                    throw new IllegalStateException("El curso ha alcanzado su cupo máximo");
                }
            }
            inscripcionExistente.setCurso(curso);
            inscripcionExistente.setDiplomado(null);
            inscripcionExistente.setMonto(curso.getCosto());
        }

        if (tieneDiplomado) {
            Long idDiplomado = deEntrada.getDiplomado().getId();
            Diplomado diplomado = diplomadoRepository.findById(idDiplomado)
                    .orElseThrow(() -> new EntityNotFoundException("Diplomado no encontrado con ID: " + idDiplomado));
            if (Boolean.FALSE.equals(diplomado.getActivo())) {
                throw new IllegalStateException("El diplomado está inactivo");
            }
            if (inscripcionExistente.getDiplomado() == null
                    || !inscripcionExistente.getDiplomado().getId().equals(diplomado.getId())) {
                long inscritos = inscripcionRepository.countByDiplomadoIdAndEstadoNot(
                        diplomado.getId(), Inscripcion.EstadoInscripcion.CANCELADA);
                if (inscritos >= diplomado.getCupoMaximo()) {
                    throw new IllegalStateException("El diplomado ha alcanzado su cupo máximo");
                }
            }
            inscripcionExistente.setDiplomado(diplomado);
            inscripcionExistente.setCurso(null);
            inscripcionExistente.setMonto(diplomado.getCosto());
        }

        inscripcionExistente.setFechaInscripcion(deEntrada.getFechaInscripcion());
        inscripcionExistente.setEstado(deEntrada.getEstado());

        return inscripcionMapper.toInscripcionResponse(inscripcionRepository.save(inscripcionExistente));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Inscripcion inscripcion = inscripcionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Inscripción no encontrada con ID: " + id));
        inscripcion.setActivo(false);
        inscripcionRepository.save(inscripcion);
    }
}