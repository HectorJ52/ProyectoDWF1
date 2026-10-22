package sv.edu.udb.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import sv.edu.udb.controller.response.SolicitudCateringResponse;
import sv.edu.udb.model.SolicitudCatering;
import sv.edu.udb.model.SolicitudCatering.EstadoSolicitud;

public interface SolicitudCateringService {

    Page<SolicitudCateringResponse> findAll(
            EstadoSolicitud estado,
            Long idCliente,
            Long idServicio,
            Pageable pageable
    );

    SolicitudCateringResponse findById(Long id);

    SolicitudCateringResponse save(SolicitudCatering solicitud);

    SolicitudCateringResponse update(
            Long id,
            SolicitudCatering solicitud
    );

    void delete(Long id);
}