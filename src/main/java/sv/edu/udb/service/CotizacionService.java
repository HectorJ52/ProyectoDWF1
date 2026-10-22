package sv.edu.udb.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sv.edu.udb.controller.response.CotizacionResponse;
import sv.edu.udb.model.Cotizacion;

public interface CotizacionService {
    Page<CotizacionResponse> findAll(Cotizacion.EstadoCotizacion estado, Long idCliente, Pageable pageable);
    CotizacionResponse findById(Long id);
    CotizacionResponse save(Cotizacion cotizacion);
    CotizacionResponse update(Long id, Cotizacion cotizacion);
    void delete(Long id);
}