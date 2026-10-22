package sv.edu.udb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sv.edu.udb.model.Docente;

@Repository
public interface DocenteRepository extends JpaRepository<Docente, Long> {
}