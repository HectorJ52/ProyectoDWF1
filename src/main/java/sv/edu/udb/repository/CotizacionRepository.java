package sv.edu.udb.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sv.edu.udb.model.Cotizacion;
import sv.edu.udb.model.Cliente;

@Repository
public interface CotizacionRepository extends JpaRepository<Cotizacion, Long> {

    Page<Cotizacion> findByEstado(Cotizacion.EstadoCotizacion estado, Pageable pageable);

    Page<Cotizacion> findByCliente(Cliente cliente, Pageable pageable);
}