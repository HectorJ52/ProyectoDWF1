package sv.edu.udb.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sv.edu.udb.model.Inscripcion;

import java.util.List;

@Repository
public interface InscripcionRepository extends JpaRepository<Inscripcion, Long> {
    Page<Inscripcion> findByActivo(Boolean activo, Pageable pageable);
    Page<Inscripcion> findByClienteId(Long idCliente, Pageable pageable);
    long countByCursoIdAndEstadoNot(Long idCurso, Inscripcion.EstadoInscripcion estado);
    long countByDiplomadoIdAndEstadoNot(Long idDiplomado, Inscripcion.EstadoInscripcion estado);

    @Query("SELECT i FROM Inscripcion i WHERE i.cliente.id = :idCliente AND i.curso.horario = :horario AND i.estado != 'CANCELADA'")
    List<Inscripcion> buscarChoqueHorario(@Param("idCliente") Long idCliente, @Param("horario") String horario);
}