package sv.edu.udb.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import sv.edu.udb.model.ServicioCatering;

public interface ServicioCateringRepository extends
        JpaRepository<ServicioCatering, Long> {
    Page<ServicioCatering> findByActivoTrue(Pageable pageable);
    boolean existsByNombreIgnoreCase(String nombre);
}