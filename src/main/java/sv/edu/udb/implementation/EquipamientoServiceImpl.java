package sv.edu.udb.implementation;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sv.edu.udb.controller.response.EquipamientoResponse;
import sv.edu.udb.model.Equipamiento;
import sv.edu.udb.repository.EquipamientoRepository;
import sv.edu.udb.service.EquipamientoService;
import sv.edu.udb.service.mapper.EquipamientoMapper;

@Service
@RequiredArgsConstructor
public class EquipamientoServiceImpl implements EquipamientoService {

    private final EquipamientoRepository repository;
    private final EquipamientoMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public Page<EquipamientoResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public EquipamientoResponse findById(Long id) {
        return mapper.toResponse(findEntityById(id));
    }

    @Override
    @Transactional
    public EquipamientoResponse save(Equipamiento equipamiento) {
        if (repository.existsByNombreIgnoreCase(equipamiento.getNombre())) {
            throw new IllegalArgumentException("Ya existe un equipamiento con el nombre: " + equipamiento.getNombre());
        }

        Equipamiento nuevoEquipamiento = Equipamiento.builder()
                .nombre(equipamiento.getNombre())
                .descripcion(equipamiento.getDescripcion())
                .build();

        return mapper.toResponse(repository.save(nuevoEquipamiento));
    }

    @Override
    @Transactional
    public EquipamientoResponse update(Long id, Equipamiento equipamiento) {
        Equipamiento equipamientoExistente = findEntityById(id);

        // Validar nombre único (excluyendo el actual)
        repository.findByNombreIgnoreCase(equipamiento.getNombre())
                .ifPresent(e -> {
                    if (!e.getId().equals(id)) {
                        throw new IllegalArgumentException("Ya existe un equipamiento con el nombre: " + equipamiento.getNombre());
                    }
                });

        equipamientoExistente.setNombre(equipamiento.getNombre());
        equipamientoExistente.setDescripcion(equipamiento.getDescripcion());

        return mapper.toResponse(repository.save(equipamientoExistente));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Equipamiento no encontrado con ID: " + id);
        }
        repository.deleteById(id);
    }

    private Equipamiento findEntityById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Equipamiento no encontrado con ID: " + id));
    }
}