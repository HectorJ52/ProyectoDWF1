package sv.edu.udb.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sv.edu.udb.controller.response.EquipamientoResponse;
import sv.edu.udb.model.Equipamiento;

public interface EquipamientoService {

    Page<EquipamientoResponse> findAll(Pageable pageable);

    EquipamientoResponse findById(Long id);

    EquipamientoResponse save(Equipamiento equipamiento);

    EquipamientoResponse update(Long id, Equipamiento equipamiento);

    void delete(Long id);
}