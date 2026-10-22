package sv.edu.udb.implementation;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sv.edu.udb.controller.response.ReservaEspacioResponse;
import sv.edu.udb.exception.EspacioOcupadoException;
import sv.edu.udb.model.Cliente;
import sv.edu.udb.model.Espacio;
import sv.edu.udb.model.ReservaEspacio;
import sv.edu.udb.repository.ClienteRepository;
import sv.edu.udb.repository.EspacioRepository;
import sv.edu.udb.repository.ReservaEspacioRepository;
import sv.edu.udb.service.ReservaEspacioService;
import sv.edu.udb.service.mapper.ReservaEspacioMapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservaEspacioServiceImpl implements ReservaEspacioService {

    private final ReservaEspacioRepository reservaRepository;
    private final EspacioRepository espacioRepository;
    private final ClienteRepository clienteRepository;
    private final ReservaEspacioMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public Page<ReservaEspacioResponse> findAll(
            ReservaEspacio.EstadoReserva estado,
            Long idEspacio,
            Long idCliente,
            Pageable pageable) {

        Page<ReservaEspacio> page;

        if (estado != null) {
            page = reservaRepository.findByEstado(estado, pageable);
        } else if (idEspacio != null) {
            page = reservaRepository.findByEspacioId(idEspacio, pageable);
        } else if (idCliente != null) {
            page = reservaRepository.findByClienteId(idCliente, pageable);
        } else {
            page = reservaRepository.findAll(pageable);
        }

        return page.map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public ReservaEspacioResponse findById(Long id) {
        return mapper.toResponse(findEntityById(id));
    }

    @Override
    @Transactional
    public ReservaEspacioResponse save(ReservaEspacio entrada) {

        validarReferencias(entrada);

        Long idEspacio = entrada.getEspacio().getId();
        Long idCliente = entrada.getCliente().getId();

        validarDisponibilidad(
                idEspacio,
                entrada.getFechaInicio(),
                entrada.getFechaFin(),
                entrada.getHoraInicio(),
                entrada.getHoraFin(),
                null
        );

        Cliente cliente = clienteRepository.findById(idCliente)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Cliente no encontrado con ID: " + idCliente
                        )
                );

        Espacio espacio = espacioRepository.findById(idEspacio)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Espacio no encontrado con ID: " + idEspacio
                        )
                );

        ReservaEspacio reserva = ReservaEspacio.builder()
                .cliente(cliente)
                .espacio(espacio)
                .fechaInicio(entrada.getFechaInicio())
                .fechaFin(entrada.getFechaFin())
                .horaInicio(entrada.getHoraInicio())
                .horaFin(entrada.getHoraFin())
                .motivo(entrada.getMotivo())
                .montoTotal(entrada.getMontoTotal())
                .estado(entrada.getEstado())
                .fechaRegistro(
                        entrada.getFechaRegistro() != null
                                ? entrada.getFechaRegistro()
                                : LocalDateTime.now()
                )
                .build();

        return mapper.toResponse(reservaRepository.save(reserva));
    }

    @Override
    @Transactional
    public ReservaEspacioResponse update(Long id, ReservaEspacio entrada) {

        ReservaEspacio existente = findEntityById(id);

        validarReferencias(entrada);

        Long idEspacio = entrada.getEspacio().getId();
        Long idCliente = entrada.getCliente().getId();

        validarDisponibilidad(
                idEspacio,
                entrada.getFechaInicio(),
                entrada.getFechaFin(),
                entrada.getHoraInicio(),
                entrada.getHoraFin(),
                id
        );

        Cliente cliente = clienteRepository.findById(idCliente)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Cliente no encontrado con ID: " + idCliente
                        )
                );

        Espacio espacio = espacioRepository.findById(idEspacio)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Espacio no encontrado con ID: " + idEspacio
                        )
                );

        existente.setCliente(cliente);
        existente.setEspacio(espacio);
        existente.setFechaInicio(entrada.getFechaInicio());
        existente.setFechaFin(entrada.getFechaFin());
        existente.setHoraInicio(entrada.getHoraInicio());
        existente.setHoraFin(entrada.getHoraFin());
        existente.setMotivo(entrada.getMotivo());
        existente.setMontoTotal(entrada.getMontoTotal());
        existente.setEstado(entrada.getEstado());

        return mapper.toResponse(
                reservaRepository.save(existente)
        );
    }

    @Override
    @Transactional
    public void delete(Long id) {

        if (!reservaRepository.existsById(id)) {
            throw new EntityNotFoundException(
                    "Reserva no encontrada con ID: " + id
            );
        }

        reservaRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean verificarDisponibilidad(
            Long idEspacio,
            LocalDate fechaInicio,
            LocalDate fechaFin,
            LocalTime horaInicio,
            LocalTime horaFin) {

        List<ReservaEspacio> conflictos =
                reservaRepository.findSolapamientos(
                        idEspacio,
                        fechaInicio,
                        fechaFin,
                        horaInicio,
                        horaFin,
                        null
                );

        return conflictos.isEmpty();
    }

    private void validarReferencias(ReservaEspacio entrada) {

        if (entrada.getEspacio() == null
                || entrada.getEspacio().getId() == null) {
            throw new IllegalArgumentException(
                    "Debe proporcionar un espacio válido"
            );
        }

        if (entrada.getCliente() == null
                || entrada.getCliente().getId() == null) {
            throw new IllegalArgumentException(
                    "Debe proporcionar un cliente válido"
            );
        }
    }

    private void validarDisponibilidad(
            Long idEspacio,
            LocalDate fechaInicio,
            LocalDate fechaFin,
            LocalTime horaInicio,
            LocalTime horaFin,
            Long idExcluir) {

        List<ReservaEspacio> conflictos =
                reservaRepository.findSolapamientos(
                        idEspacio,
                        fechaInicio,
                        fechaFin,
                        horaInicio,
                        horaFin,
                        idExcluir
                );

        if (!conflictos.isEmpty()) {
            throw new EspacioOcupadoException(
                    "El espacio ya está reservado en el horario solicitado. "
                    + "Conflicto con la reserva ID: "
                    + conflictos.get(0).getId()
            );
        }
    }

    private ReservaEspacio findEntityById(Long id) {
        return reservaRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Reserva no encontrada con ID: " + id
                        )
                );
    }
}