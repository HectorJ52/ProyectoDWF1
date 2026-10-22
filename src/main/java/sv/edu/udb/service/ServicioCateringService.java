package sv.edu.udb.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sv.edu.udb.controller.response.ServicioCateringResponse;
import sv.edu.udb.model.ServicioCatering;

public interface ServicioCateringService {
    Page<ServicioCateringResponse> findAll(Boolean soloActivos, Pageable pageable);
    ServicioCateringResponse findById(Long id);
    ServicioCateringResponse save(ServicioCatering servicioCatering);
    ServicioCateringResponse update(Long id, ServicioCatering servicioCatering);
    void delete(Long id);
}