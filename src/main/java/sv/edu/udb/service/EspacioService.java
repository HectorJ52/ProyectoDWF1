package sv.edu.udb.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sv.edu.udb.controller.response.EspacioResponse;
import sv.edu.udb.model.Espacio;
import sv.edu.udb.model.Espacio.TipoEspacio;

public interface EspacioService {

    Page<EspacioResponse> findAll(TipoEspacio tipoEspacio, Boolean soloActivos, Integer capacidadMinima, Pageable pageable);

    EspacioResponse findById(Long id);

    EspacioResponse save(Espacio espacio);

    EspacioResponse update(Long id, Espacio espacio);

    void delete(Long id);
}