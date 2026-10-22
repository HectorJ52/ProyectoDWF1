package sv.edu.udb.implementation;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sv.edu.udb.controller.response.CotizacionResponse;
import sv.edu.udb.model.Cliente;
import sv.edu.udb.model.Cotizacion;
import sv.edu.udb.repository.ClienteRepository;
import sv.edu.udb.repository.CotizacionRepository;
import sv.edu.udb.service.CotizacionService;
import sv.edu.udb.service.mapper.CotizacionMapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CotizacionServiceImpl implements CotizacionService {

    private final CotizacionRepository cotizacionRepository;
    private final ClienteRepository clienteRepository;
    private final CotizacionMapper cotizacionMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<CotizacionResponse> findAll(
            Cotizacion.EstadoCotizacion estado,
            Long idCliente,
            Pageable pageable) {

        Page<Cotizacion> page;

        if (estado != null) {
            page = cotizacionRepository.findByEstado(estado, pageable);
        } else if (idCliente != null) {
            Cliente cliente = clienteRepository.findById(idCliente)
                    .orElseThrow(() ->
                            new EntityNotFoundException(
                                    "Cliente no encontrado con ID: " + idCliente
                            )
                    );

            page = cotizacionRepository.findByCliente(cliente, pageable);
        } else {
            page = cotizacionRepository.findAll(pageable);
        }

        return page.map(cotizacionMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public CotizacionResponse findById(Long id) {

        Cotizacion cotizacion = cotizacionRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Cotización no encontrada con ID: " + id
                        )
                );

        return cotizacionMapper.toResponse(cotizacion);
    }

    @Override
    @Transactional
    public CotizacionResponse save(Cotizacion cotizacion) {

        if (cotizacion.getCliente() == null
                || cotizacion.getCliente().getId() == null) {
            throw new IllegalArgumentException(
                    "Debe proporcionar un cliente válido"
            );
        }

        Long idCliente = cotizacion.getCliente().getId();

        Cliente cliente = clienteRepository.findById(idCliente)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Cliente no encontrado con ID: " + idCliente
                        )
                );

        Cotizacion nueva = Cotizacion.builder()
                .cliente(cliente)
                .fechaCotizacion(
                        cotizacion.getFechaCotizacion() != null
                                ? cotizacion.getFechaCotizacion()
                                : LocalDateTime.now()
                )
                .estado(
                        cotizacion.getEstado() != null
                                ? cotizacion.getEstado()
                                : Cotizacion.EstadoCotizacion.PENDIENTE
                )
                .total(BigDecimal.ZERO)
                .build();

        return cotizacionMapper.toResponse(
                cotizacionRepository.save(nueva)
        );
    }

    @Override
    @Transactional
    public CotizacionResponse update(Long id, Cotizacion entrada) {

        Cotizacion existente = cotizacionRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Cotización no encontrada con ID: " + id
                        )
                );

        if (entrada.getCliente() == null
                || entrada.getCliente().getId() == null) {
            throw new IllegalArgumentException(
                    "Debe proporcionar un cliente válido"
            );
        }

        Long idCliente = entrada.getCliente().getId();

        Cliente cliente = clienteRepository.findById(idCliente)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Cliente no encontrado con ID: " + idCliente
                        )
                );

        existente.setCliente(cliente);

        if (entrada.getEstado() != null) {
            existente.setEstado(entrada.getEstado());
        }

        if (entrada.getFechaCotizacion() != null) {
            existente.setFechaCotizacion(entrada.getFechaCotizacion());
        }

        return cotizacionMapper.toResponse(
                cotizacionRepository.save(existente)
        );
    }

    @Override
    @Transactional
    public void delete(Long id) {

        if (!cotizacionRepository.existsById(id)) {
            throw new EntityNotFoundException(
                    "Cotización no encontrada con ID: " + id
            );
        }

        cotizacionRepository.deleteById(id);
    }
}