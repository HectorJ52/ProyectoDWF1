package sv.edu.udb.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sv.edu.udb.model.Cotizacion;
import sv.edu.udb.model.DetalleCotizacion;

@Repository
public interface DetalleCotizacionRepository extends JpaRepository<DetalleCotizacion, Long> {

    Page<DetalleCotizacion> findByCotizacion(Cotizacion cotizacion, Pageable pageable);
}