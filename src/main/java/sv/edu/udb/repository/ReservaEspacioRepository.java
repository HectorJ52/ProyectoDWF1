package sv.edu.udb.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sv.edu.udb.model.ReservaEspacio;
import sv.edu.udb.model.ReservaEspacio.EstadoReserva;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface ReservaEspacioRepository extends JpaRepository<ReservaEspacio, Long> {

    Page<ReservaEspacio> findByEspacioId(Long idEspacio, Pageable pageable);

    Page<ReservaEspacio> findByClienteId(Long idCliente, Pageable pageable);

    Page<ReservaEspacio> findByEstado(EstadoReserva estado, Pageable pageable);

    /**
     * Detecta solapamientos de horario en el mismo espacio.
     * Condición de solapamiento:
     * (fechaInicioNueva <= fechaFinExistente) AND (fechaFinNueva >= fechaInicioExistente)
     * y además las horas se cruzan.
     */
    @Query("""
        SELECT r FROM ReservaEspacio r
        WHERE r.espacio.id = :idEspacio
          AND r.estado IN ('PENDIENTE', 'CONFIRMADA')
          AND r.fechaInicio <= :fechaFin
          AND r.fechaFin >= :fechaInicio
          AND r.horaInicio < :horaFin
          AND r.horaFin > :horaInicio
          AND (:idReservaExcluir IS NULL OR r.id <> :idReservaExcluir)
        """)
    List<ReservaEspacio> findSolapamientos(
            @Param("idEspacio") Long idEspacio,
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin,
            @Param("horaInicio") LocalTime horaInicio,
            @Param("horaFin") LocalTime horaFin,
            @Param("idReservaExcluir") Long idReservaExcluir
    );
}