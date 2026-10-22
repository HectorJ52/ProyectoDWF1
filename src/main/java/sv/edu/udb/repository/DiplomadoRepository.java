package sv.edu.udb.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sv.edu.udb.model.Diplomado;

@Repository
public interface DiplomadoRepository extends JpaRepository<Diplomado, Long> {
    Page<Diplomado> findByActivo(Boolean activo, Pageable pageable);
    Page<Diplomado> findByActivoAndCategoriaIdAndModalidadId(Boolean activo, Long categoriaId, Long modalidadId, Pageable pageable);
}