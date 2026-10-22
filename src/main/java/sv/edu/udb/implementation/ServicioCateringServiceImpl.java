package sv.edu.udb.implementation;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sv.edu.udb.controller.response.ServicioCateringResponse;
import sv.edu.udb.model.ServicioCatering;
import sv.edu.udb.repository.ServicioCateringRepository;
import sv.edu.udb.service.ServicioCateringService;
import sv.edu.udb.service.mapper.ServicioCateringMapper;

@Service
@RequiredArgsConstructor
public class ServicioCateringServiceImpl implements ServicioCateringService {

    private final ServicioCateringRepository repository;
    private final ServicioCateringMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public Page<ServicioCateringResponse> findAll(Boolean soloActivos, Pageable pageable) {
        Page<ServicioCatering> page;
        if (Boolean.TRUE.equals(soloActivos)) {
            page = repository.findByActivoTrue(pageable);
        } else {
            page = repository.findAll(pageable);
        }
        return page.map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public ServicioCateringResponse findById(Long id) {
        return mapper.toResponse(findEntityById(id));
    }

    @Override
    @Transactional
    public ServicioCateringResponse save(ServicioCatering servicio) {
        if (repository.existsByNombreIgnoreCase(servicio.getNombre())) {
            throw new IllegalArgumentException("Ya existe un servicio de catering con el nombre: " + servicio.getNombre());
        }
        ServicioCatering nuevoServicio = ServicioCatering.builder()
                .nombre(servicio.getNombre())
                .descripcion(servicio.getDescripcion())
                .precioPersona(servicio.getPrecioPersona())
                .activo(servicio.getActivo() != null ? servicio.getActivo() : true)
                .build();
        return mapper.toResponse(repository.save(nuevoServicio));
    }

    @Override
    @Transactional
    public ServicioCateringResponse update(Long id, ServicioCatering servicio) {
        ServicioCatering servicioExistente = findEntityById(id);
        servicioExistente.setNombre(servicio.getNombre());
        servicioExistente.setDescripcion(servicio.getDescripcion());
        servicioExistente.setPrecioPersona(servicio.getPrecioPersona());
        if (servicio.getActivo() != null) {
            servicioExistente.setActivo(servicio.getActivo());
        }
        return mapper.toResponse(repository.save(servicioExistente));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Servicio de catering no encontrado con ID: " + id);
        }
        repository.deleteById(id);
    }

    private ServicioCatering findEntityById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Servicio de catering no encontrado con ID: " + id));
    }
}