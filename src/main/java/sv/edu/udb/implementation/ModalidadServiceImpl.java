package sv.edu.udb.implementation;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sv.edu.udb.model.Modalidad;
import sv.edu.udb.repository.ModalidadRepository;
import sv.edu.udb.service.ModalidadService;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ModalidadServiceImpl implements ModalidadService {
    
    private final ModalidadRepository modalidadRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Modalidad> findAll() {
        return modalidadRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Modalidad findById(Long id) {
        return modalidadRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Modalidad no encontrada con ID: " + id));
    }

    @Override
    @Transactional
    public Modalidad save(Modalidad modalidad) {
        return modalidadRepository.save(modalidad);
    }

    @Override
    @Transactional
    public Modalidad update(Long id, Modalidad modalidadActualizada) {
        Modalidad modalidad = modalidadRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Modalidad no encontrada con ID: " + id));
        modalidad.setNombre(modalidadActualizada.getNombre());
        modalidad.setDescripcion(modalidadActualizada.getDescripcion());
        return modalidadRepository.save(modalidad);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!modalidadRepository.existsById(id)) {
            throw new EntityNotFoundException("Modalidad no encontrada con ID: " + id);
        }
        modalidadRepository.deleteById(id);
    }
}