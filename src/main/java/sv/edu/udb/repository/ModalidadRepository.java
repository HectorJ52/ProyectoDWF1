package sv.edu.udb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sv.edu.udb.model.Modalidad;

@Repository
public interface ModalidadRepository extends JpaRepository<Modalidad, Long> {
    // Al ser catálogo estático no requiere filtros complejos adicionales
}