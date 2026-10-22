package sv.edu.udb.implementation;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sv.edu.udb.controller.response.PagoResponse;
import sv.edu.udb.exception.SaldoInsuficienteException;
import sv.edu.udb.model.Cotizacion;
import sv.edu.udb.model.Inscripcion;
import sv.edu.udb.model.Pago;
import sv.edu.udb.repository.CotizacionRepository;
import sv.edu.udb.repository.InscripcionRepository;
import sv.edu.udb.repository.PagoRepository;
import sv.edu.udb.service.PagoService;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PagoServiceImpl implements PagoService {

    private final PagoRepository pagoRepository;
    private final InscripcionRepository inscripcionRepository;
    private final CotizacionRepository cotizacionRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<PagoResponse> findAll(Pageable pageable) {
        return pagoRepository.findAll(pageable).map(this::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public PagoResponse findById(Long id) {

        Pago pago = pagoRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Pago no encontrado con ID: " + id
                        )
                );

        return toResponse(pago);
    }

    @Override
    @Transactional
    public PagoResponse save(Pago pago) {

        validarCampos(pago);
        validarReferencia(pago);

        Inscripcion inscripcion = null;
        Cotizacion cotizacion = null;

        if (pago.getInscripcion() != null
                && pago.getInscripcion().getId() != null) {

            Long idInscripcion = pago.getInscripcion().getId();

            inscripcion = inscripcionRepository.findById(idInscripcion)
                    .orElseThrow(() ->
                            new EntityNotFoundException(
                                    "Inscripción no encontrada con ID: "
                                            + idInscripcion
                            )
                    );

            validarSaldoInscripcion(inscripcion, pago.getMonto(), null);
        }

        if (pago.getCotizacion() != null
                && pago.getCotizacion().getId() != null) {

            Long idCotizacion = pago.getCotizacion().getId();

            cotizacion = cotizacionRepository.findById(idCotizacion)
                    .orElseThrow(() ->
                            new EntityNotFoundException(
                                    "Cotización no encontrada con ID: "
                                            + idCotizacion
                            )
                    );

            validarSaldoCotizacion(cotizacion, pago.getMonto(), null);
        }

        Pago nuevoPago = Pago.builder()
                .inscripcion(inscripcion)
                .cotizacion(cotizacion)
                .monto(pago.getMonto())
                .fechaPago(pago.getFechaPago())
                .metodoPago(pago.getMetodoPago())
                .estado(pago.getEstado())
                .build();

        return toResponse(pagoRepository.save(nuevoPago));
    }

    @Override
    @Transactional
    public PagoResponse update(Long id, Pago entrada) {

        Pago existente = pagoRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Pago no encontrado con ID: " + id
                        )
                );

        validarCampos(entrada);
        validarReferencia(entrada);

        Inscripcion inscripcion = null;
        Cotizacion cotizacion = null;

        if (entrada.getInscripcion() != null
                && entrada.getInscripcion().getId() != null) {

            Long idInscripcion = entrada.getInscripcion().getId();

            inscripcion = inscripcionRepository.findById(idInscripcion)
                    .orElseThrow(() ->
                            new EntityNotFoundException(
                                    "Inscripción no encontrada con ID: "
                                            + idInscripcion
                            )
                    );

            validarSaldoInscripcion(
                    inscripcion, entrada.getMonto(), id
            );
        }

        if (entrada.getCotizacion() != null
                && entrada.getCotizacion().getId() != null) {

            Long idCotizacion = entrada.getCotizacion().getId();

            cotizacion = cotizacionRepository.findById(idCotizacion)
                    .orElseThrow(() ->
                            new EntityNotFoundException(
                                    "Cotización no encontrada con ID: "
                                            + idCotizacion
                            )
                    );

            validarSaldoCotizacion(
                    cotizacion, entrada.getMonto(), id
            );
        }

        existente.setInscripcion(inscripcion);
        existente.setCotizacion(cotizacion);
        existente.setMonto(entrada.getMonto());
        existente.setFechaPago(entrada.getFechaPago());
        existente.setMetodoPago(entrada.getMetodoPago());
        existente.setEstado(entrada.getEstado());

        return toResponse(pagoRepository.save(existente));
    }

    @Override
    @Transactional
    public void delete(Long id) {

        if (!pagoRepository.existsById(id)) {
            throw new EntityNotFoundException(
                    "Pago no encontrado con ID: " + id
            );
        }

        pagoRepository.deleteById(id);
    }

    private void validarCampos(Pago pago) {

        if (pago.getMonto() == null
                || pago.getMonto().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(
                    "El monto debe ser mayor o igual a cero"
            );
        }

        if (pago.getFechaPago() == null) {
            throw new IllegalArgumentException(
                    "La fecha de pago es obligatoria"
            );
        }

        if (pago.getMetodoPago() == null
                || pago.getMetodoPago().isBlank()) {
            throw new IllegalArgumentException(
                    "El método de pago es obligatorio"
            );
        }

        if (pago.getEstado() == null || pago.getEstado().isBlank()) {
            throw new IllegalArgumentException(
                    "El estado del pago es obligatorio"
            );
        }
    }

    private void validarReferencia(Pago pago) {

        boolean tieneInscripcion =
                pago.getInscripcion() != null
                && pago.getInscripcion().getId() != null;

        boolean tieneCotizacion =
                pago.getCotizacion() != null
                && pago.getCotizacion().getId() != null;

        if (tieneInscripcion == tieneCotizacion) {
            throw new IllegalArgumentException(
                    "El pago debe pertenecer a una inscripción o a una "
                    + "cotización, pero no a ambas"
            );
        }
    }

    private void validarSaldoInscripcion(
            Inscripcion inscripcion,
            BigDecimal nuevoPago,
            Long idPagoActual) {

        BigDecimal total = inscripcion.getMonto();

        if (total == null) {
            throw new IllegalArgumentException(
                    "No se pudo determinar el costo de la inscripción"
            );
        }

        List<Pago> pagos = pagoRepository.findByInscripcionId(
                inscripcion.getId()
        );

        BigDecimal totalPagado = pagos.stream()
                .filter(p -> idPagoActual == null
                        || !p.getId().equals(idPagoActual))
                .map(Pago::getMonto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal saldoPendiente = total.subtract(totalPagado);

        if (nuevoPago.compareTo(saldoPendiente) > 0) {
            throw new SaldoInsuficienteException(
                    "El pago supera el saldo pendiente de la inscripción"
            );
        }
    }

    private void validarSaldoCotizacion(
            Cotizacion cotizacion,
            BigDecimal nuevoPago,
            Long idPagoActual) {

        BigDecimal total = cotizacion.getTotal();

        if (total == null) {
            throw new IllegalArgumentException(
                    "La cotización no tiene un total definido"
            );
        }

        List<Pago> pagos = pagoRepository.findByCotizacionId(
                cotizacion.getId()
        );

        BigDecimal totalPagado = pagos.stream()
                .filter(p -> idPagoActual == null
                        || !p.getId().equals(idPagoActual))
                .map(Pago::getMonto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal saldoPendiente = total.subtract(totalPagado);

        if (nuevoPago.compareTo(saldoPendiente) > 0) {
            throw new SaldoInsuficienteException(
                    "El pago supera el saldo pendiente de la cotización"
            );
        }
    }

    private PagoResponse toResponse(Pago pago) {

        return PagoResponse.builder()
                .id(pago.getId())
                .idInscripcion(
                        pago.getInscripcion() != null
                                ? pago.getInscripcion().getId()
                                : null
                )
                .idCotizacion(
                        pago.getCotizacion() != null
                                ? pago.getCotizacion().getId()
                                : null
                )
                .monto(pago.getMonto())
                .fechaPago(pago.getFechaPago())
                .metodoPago(pago.getMetodoPago())
                .estado(pago.getEstado())
                .build();
    }
}