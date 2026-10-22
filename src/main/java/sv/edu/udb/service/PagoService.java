package sv.edu.udb.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sv.edu.udb.controller.response.PagoResponse;
import sv.edu.udb.model.Pago;

public interface PagoService {
    Page<PagoResponse> findAll(Pageable pageable);
    PagoResponse findById(Long id);
    PagoResponse save(Pago pago);
    PagoResponse update(Long id, Pago pago);
    void delete(Long id);
}