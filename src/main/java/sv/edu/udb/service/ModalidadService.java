package sv.edu.udb.service;

import java.util.List;
import sv.edu.udb.model.Modalidad;

public interface ModalidadService {
    List<Modalidad> findAll();
    Modalidad findById(Long id);
    Modalidad save(Modalidad modalidad);
    Modalidad update(Long id, Modalidad modalidad);
    void delete(Long id);
}