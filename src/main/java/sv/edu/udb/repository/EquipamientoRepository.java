package sv.edu.udb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sv.edu.udb.model.Equipamiento;

import java.util.Optional;

public interface EquipamientoRepository extends JpaRepository<Equipamiento, Long> {
    Optional<Equipamiento> findByNombreIgnoreCase(String nombre);
    boolean existsByNombreIgnoreCase(String nombre);
}