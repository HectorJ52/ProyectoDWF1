package sv.edu.udb.implementation;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sv.edu.udb.controller.response.SolicitudCateringResponse;
import sv.edu.udb.model.*;
import sv.edu.udb.model.SolicitudCatering.EstadoSolicitud;
import sv.edu.udb.repository.*;
import sv.edu.udb.service.SolicitudCateringService;
import sv.edu.udb.service.mapper.SolicitudCateringMapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SolicitudCateringServiceImpl
        implements SolicitudCateringService {

    private final SolicitudCateringRepository solicitudRepository;
    private final ClienteRepository clienteRepository;
    private final ServicioCateringRepository servicioRepository;
    private final ReservaEspacioRepository reservaRepository;
    private final CotizacionRepository cotizacionRepository;
    private final SolicitudCateringMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public Page<SolicitudCateringResponse> findAll(
            EstadoSolicitud estado,
            Long idCliente,
            Long idServicio,
            Pageable pageable) {

        Page<SolicitudCatering> page;

        if (estado != null) {
            page = solicitudRepository.findByEstado(estado, pageable);
        } else if (idCliente != null) {
            page = solicitudRepository.findByClienteId(
                    idCliente, pageable
            );
        } else if (idServicio != null) {
            page = solicitudRepository.findByServicioId(
                    idServicio, pageable
            );
        } else {
            page = solicitudRepository.findAll(pageable);
        }

        return page.map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public SolicitudCateringResponse findById(Long id) {
        return mapper.toResponse(findEntityById(id));
    }

    @Override
    @Transactional
    public SolicitudCateringResponse save(
            SolicitudCatering entrada) {

        validarDatos(entrada);

        Cliente cliente = buscarCliente(
                entrada.getCliente().getId()
        );

        ServicioCatering servicio = buscarServicio(
                entrada.getServicio().getId()
        );

        ReservaEspacio reserva = buscarReservaOpcional(
                entrada.getReservaEspacio()
        );

        Cotizacion cotizacion = buscarCotizacionOpcional(
                entrada.getCotizacion()
        );

        BigDecimal total = calcularTotal(
                servicio,
                entrada.getNumeroAsistentes()
        );

        SolicitudCatering nueva = SolicitudCatering.builder()
                .cliente(cliente)
                .servicio(servicio)
                .numeroAsistentes(entrada.getNumeroAsistentes())
                .menu(entrada.getMenu())
                .fecha(entrada.getFecha())
                .hora(entrada.getHora())
                .lugar(entrada.getLugar())
                .estado(
                        entrada.getEstado() != null
                                ? entrada.getEstado()
                                : EstadoSolicitud.PENDIENTE
                )
                .precioTotal(total)
                .reservaEspacio(reserva)
                .cotizacion(cotizacion)
                .fechaRegistro(LocalDateTime.now())
                .build();

        return mapper.toResponse(
                solicitudRepository.save(nueva)
        );
    }

    @Override
    @Transactional
    public SolicitudCateringResponse update(
            Long id,
            SolicitudCatering entrada) {

        SolicitudCatering existente = findEntityById(id);

        validarDatos(entrada);

        Cliente cliente = buscarCliente(
                entrada.getCliente().getId()
        );

        ServicioCatering servicio = buscarServicio(
                entrada.getServicio().getId()
        );

        ReservaEspacio reserva = buscarReservaOpcional(
                entrada.getReservaEspacio()
        );

        Cotizacion cotizacion = buscarCotizacionOpcional(
                entrada.getCotizacion()
        );

        existente.setCliente(cliente);
        existente.setServicio(servicio);
        existente.setNumeroAsistentes(
                entrada.getNumeroAsistentes()
        );
        existente.setMenu(entrada.getMenu());
        existente.setFecha(entrada.getFecha());
        existente.setHora(entrada.getHora());
        existente.setLugar(entrada.getLugar());

        if (entrada.getEstado() != null) {
            existente.setEstado(entrada.getEstado());
        }

        existente.setPrecioTotal(
                calcularTotal(
                        servicio,
                        entrada.getNumeroAsistentes()
                )
        );

        existente.setReservaEspacio(reserva);
        existente.setCotizacion(cotizacion);

        return mapper.toResponse(
                solicitudRepository.save(existente)
        );
    }

    @Override
    @Transactional
    public void delete(Long id) {

        SolicitudCatering solicitud = findEntityById(id);

        solicitudRepository.delete(solicitud);
    }

    private void validarDatos(SolicitudCatering solicitud) {

        if (solicitud.getCliente() == null
                || solicitud.getCliente().getId() == null) {
            throw new IllegalArgumentException(
                    "Debe proporcionar un cliente válido"
            );
        }

        if (solicitud.getServicio() == null
                || solicitud.getServicio().getId() == null) {
            throw new IllegalArgumentException(
                    "Debe proporcionar un servicio de catering válido"
            );
        }

        if (solicitud.getNumeroAsistentes() == null
                || solicitud.getNumeroAsistentes() <= 0) {
            throw new IllegalArgumentException(
                    "El número de asistentes debe ser mayor que cero"
            );
        }

        if (solicitud.getFecha() == null) {
            throw new IllegalArgumentException(
                    "La fecha es obligatoria"
            );
        }

        if (solicitud.getHora() == null) {
            throw new IllegalArgumentException(
                    "La hora es obligatoria"
            );
        }

        if (solicitud.getEstado() == null) {
            solicitud.setEstado(EstadoSolicitud.PENDIENTE);
        }
    }

    private Cliente buscarCliente(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Cliente no encontrado con ID: " + id
                        )
                );
    }

    private ServicioCatering buscarServicio(Long id) {
        return servicioRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Servicio de catering no encontrado "
                                        + "con ID: " + id
                        )
                );
    }

    private ReservaEspacio buscarReservaOpcional(
            ReservaEspacio entrada) {

        if (entrada == null || entrada.getId() == null) {
            return null;
        }

        Long id = entrada.getId();

        return reservaRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Reserva de espacio no encontrada "
                                        + "con ID: " + id
                        )
                );
    }

    private Cotizacion buscarCotizacionOpcional(
            Cotizacion entrada) {

        if (entrada == null || entrada.getId() == null) {
            return null;
        }

        Long id = entrada.getId();

        return cotizacionRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Cotización no encontrada con ID: " + id
                        )
                );
    }

    private BigDecimal calcularTotal(
            ServicioCatering servicio,
            Integer asistentes) {

        if (servicio.getPrecioPersona() == null) {
            throw new IllegalArgumentException(
                    "El servicio no tiene un precio por persona definido"
            );
        }

        return servicio.getPrecioPersona()
                .multiply(BigDecimal.valueOf(asistentes));
    }

    private SolicitudCatering findEntityById(Long id) {
        return solicitudRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Solicitud de catering no encontrada "
                                        + "con ID: " + id
                        )
                );
    }
}