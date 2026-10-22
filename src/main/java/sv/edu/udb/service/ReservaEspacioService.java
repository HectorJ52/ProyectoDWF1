package sv.edu.udb.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sv.edu.udb.controller.response.ReservaEspacioResponse;
import sv.edu.udb.model.ReservaEspacio;
import sv.edu.udb.model.ReservaEspacio.EstadoReserva;
import java.time.LocalDate;
import java.time.LocalTime;

public interface ReservaEspacioService {
    Page<ReservaEspacioResponse> findAll(EstadoReserva estado, Long idEspacio, Long idCliente, Pageable pageable);
    ReservaEspacioResponse findById(Long id);
    ReservaEspacioResponse save(ReservaEspacio reserva);
    ReservaEspacioResponse update(Long id, ReservaEspacio reserva);
    void delete(Long id);
    boolean verificarDisponibilidad(Long idEspacio, LocalDate fechaInicio, LocalDate fechaFin,
                                    LocalTime horaInicio, LocalTime horaFin);
}