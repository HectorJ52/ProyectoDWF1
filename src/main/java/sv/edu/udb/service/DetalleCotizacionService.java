package sv.edu.udb.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sv.edu.udb.controller.response.DetalleCotizacionResponse;
import sv.edu.udb.model.DetalleCotizacion;

public interface DetalleCotizacionService {
    Page<DetalleCotizacionResponse> findAll(Long idCotizacion, Pageable pageable);
    DetalleCotizacionResponse findById(Long id);
    DetalleCotizacionResponse save(DetalleCotizacion detalle);
    DetalleCotizacionResponse update(Long id, DetalleCotizacion detalle);
    void delete(Long id);
}