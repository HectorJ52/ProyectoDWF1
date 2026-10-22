package sv.edu.udb.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sv.edu.udb.model.Inscripcion;

@Repository
public interface InscripcionRepository extends JpaRepository<Inscripcion, Long> {
    Page<Inscripcion> findByActivo(Boolean activo, Pageable pageable);
    Page<Inscripcion> findByClienteId(Long idCliente, Pageable pageable);
    long countByCursoIdAndEstadoNot(Long idCurso, Inscripcion.EstadoInscripcion estado);
    long countByDiplomadoIdAndEstadoNot(Long idDiplomado, Inscripcion.EstadoInscripcion estado);
}