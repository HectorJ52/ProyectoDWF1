package sv.edu.udb.implementation;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sv.edu.udb.controller.response.EspacioResponse;
import sv.edu.udb.model.Equipamiento;
import sv.edu.udb.model.Espacio;
import sv.edu.udb.model.Espacio.TipoEspacio;
import sv.edu.udb.repository.EquipamientoRepository;
import sv.edu.udb.repository.EspacioRepository;
import sv.edu.udb.service.EspacioService;
import sv.edu.udb.service.mapper.EspacioMapper;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class EspacioServiceImpl implements EspacioService {

    private final EspacioRepository espacioRepository;
    private final EquipamientoRepository equipamientoRepository;
    private final EspacioMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public Page<EspacioResponse> findAll(
            TipoEspacio tipoEspacio,
            Boolean soloActivos,
            Integer capacidadMinima,
            Pageable pageable) {

        Page<Espacio> page;

        if (tipoEspacio != null) {
            page = espacioRepository.findByTipoEspacio(
                    tipoEspacio, pageable
            );
        } else if (capacidadMinima != null) {
            page = espacioRepository.findDisponiblesPorCapacidad(
                    capacidadMinima, pageable
            );
        } else if (Boolean.TRUE.equals(soloActivos)) {
            page = espacioRepository.findByActivoTrue(pageable);
        } else {
            page = espacioRepository.findAll(pageable);
        }

        return page.map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public EspacioResponse findById(Long id) {
        return mapper.toResponse(findEntityById(id));
    }

    @Override
    @Transactional
    public EspacioResponse save(Espacio espacio) {

        Espacio nuevo = Espacio.builder()
                .nombre(espacio.getNombre())
                .tipoEspacio(espacio.getTipoEspacio())
                .capacidad(espacio.getCapacidad())
                .descripcion(espacio.getDescripcion())
                .activo(
                        espacio.getActivo() != null
                                ? espacio.getActivo()
                                : true
                )
                .equipamientos(new HashSet<>())
                .build();

        nuevo.setEquipamientos(
                cargarEquipamientos(espacio.getEquipamientos())
        );

        return mapper.toResponse(espacioRepository.save(nuevo));
    }

    @Override
    @Transactional
    public EspacioResponse update(Long id, Espacio entrada) {

        Espacio existente = findEntityById(id);

        existente.setNombre(entrada.getNombre());
        existente.setTipoEspacio(entrada.getTipoEspacio());
        existente.setCapacidad(entrada.getCapacidad());
        existente.setDescripcion(entrada.getDescripcion());

        if (entrada.getActivo() != null) {
            existente.setActivo(entrada.getActivo());
        }

        if (entrada.getEquipamientos() != null) {
            existente.setEquipamientos(
                    cargarEquipamientos(entrada.getEquipamientos())
            );
        }

        return mapper.toResponse(
                espacioRepository.save(existente)
        );
    }

    @Override
    @Transactional
    public void delete(Long id) {

        if (!espacioRepository.existsById(id)) {
            throw new EntityNotFoundException(
                    "Espacio no encontrado con ID: " + id
            );
        }

        espacioRepository.deleteById(id);
    }

    private Set<Equipamiento> cargarEquipamientos(
            Set<Equipamiento> entrada) {

        Set<Equipamiento> validados = new HashSet<>();

        if (entrada == null) {
            return validados;
        }

        for (Equipamiento equipamiento : entrada) {
            if (equipamiento.getId() != null) {
                Equipamiento existente = equipamientoRepository
                        .findById(equipamiento.getId())
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "Equipamiento no encontrado con ID: "
                                                + equipamiento.getId()
                                )
                        );

                validados.add(existente);
            }
        }

        return validados;
    }

    private Espacio findEntityById(Long id) {
        return espacioRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Espacio no encontrado con ID: " + id
                        )
                );
    }
}