package sv.edu.udb.implementation;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sv.edu.udb.controller.response.DetalleCotizacionResponse;
import sv.edu.udb.model.Cotizacion;
import sv.edu.udb.model.DetalleCotizacion;
import sv.edu.udb.repository.CotizacionRepository;
import sv.edu.udb.repository.DetalleCotizacionRepository;
import sv.edu.udb.service.DetalleCotizacionService;
import sv.edu.udb.service.mapper.DetalleCotizacionMapper;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class DetalleCotizacionServiceImpl implements DetalleCotizacionService {

    private final DetalleCotizacionRepository detalleRepository;
    private final CotizacionRepository cotizacionRepository;
    private final DetalleCotizacionMapper detalleMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<DetalleCotizacionResponse> findAll(Long idCotizacion, Pageable pageable) {
        if (idCotizacion != null) {
            Cotizacion cotizacion = cotizacionRepository.findById(idCotizacion)
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Cotización no encontrada con ID: " + idCotizacion));
            return detalleRepository.findByCotizacion(cotizacion, pageable)
                    .map(detalleMapper::toResponse);
        }

        return detalleRepository.findAll(pageable)
                .map(detalleMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public DetalleCotizacionResponse findById(Long id) {
        DetalleCotizacion detalle = detalleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Detalle de cotización no encontrado con ID: " + id));
        return detalleMapper.toResponse(detalle);
    }

    @Override
    @Transactional
    public DetalleCotizacionResponse save(DetalleCotizacion detalle) {
        if (detalle.getCotizacion() == null || detalle.getCotizacion().getId() == null) {
            throw new IllegalArgumentException("Debe proporcionar una cotización de referencia válida");
        }

        Long idCotizacion = detalle.getCotizacion().getId();
        Cotizacion cotizacion = obtenerCotizacion(idCotizacion);

        BigDecimal subtotal = detalle.getPrecioUnitario()
                .multiply(BigDecimal.valueOf(detalle.getCantidad()));

        DetalleCotizacion nuevoDetalle = DetalleCotizacion.builder()
                .cotizacion(cotizacion)
                .tipoItem(detalle.getTipoItem())
                .idReferencia(detalle.getIdReferencia())
                .cantidad(detalle.getCantidad())
                .precioUnitario(detalle.getPrecioUnitario())
                .subtotal(subtotal)
                .build();

        DetalleCotizacionResponse response =
                detalleMapper.toResponse(detalleRepository.save(nuevoDetalle));

        actualizarTotal(cotizacion);

        return response;
    }

    @Override
    @Transactional
    public DetalleCotizacionResponse update(Long id, DetalleCotizacion detalle) {
        DetalleCotizacion detalleExistente = detalleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Detalle de cotización no encontrado con ID: " + id));

        if (detalle.getCotizacion() == null || detalle.getCotizacion().getId() == null) {
            throw new IllegalArgumentException("Debe proporcionar una cotización de referencia válida");
        }

        Long idCotizacion = detalle.getCotizacion().getId();
        Cotizacion cotizacion = obtenerCotizacion(idCotizacion);

        detalleExistente.setCotizacion(cotizacion);
        detalleExistente.setTipoItem(detalle.getTipoItem());
        detalleExistente.setIdReferencia(detalle.getIdReferencia());
        detalleExistente.setCantidad(detalle.getCantidad());
        detalleExistente.setPrecioUnitario(detalle.getPrecioUnitario());
        detalleExistente.setSubtotal(detalle.getPrecioUnitario()
                .multiply(BigDecimal.valueOf(detalle.getCantidad())));

        DetalleCotizacionResponse response =
                detalleMapper.toResponse(detalleRepository.save(detalleExistente));

        actualizarTotal(cotizacion);

        return response;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        DetalleCotizacion detalle = detalleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Detalle de cotización no encontrado con ID: " + id));

        Cotizacion cotizacion = detalle.getCotizacion();
        detalleRepository.delete(detalle);
        actualizarTotal(cotizacion);
    }

    private Cotizacion obtenerCotizacion(Long id) {
        return cotizacionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Cotización no encontrada con ID: " + id));
    }

    private void actualizarTotal(Cotizacion cotizacion) {
        BigDecimal total = detalleRepository.findByCotizacion(
                        cotizacion,
                        Pageable.unpaged())
                .getContent()
                .stream()
                .map(DetalleCotizacion::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        cotizacion.setTotal(total);
        cotizacionRepository.save(cotizacion);
    }
}