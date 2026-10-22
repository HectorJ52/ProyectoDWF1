package sv.edu.udb.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sv.edu.udb.model.Curso;

@Repository
public interface CursoRepository extends JpaRepository<Curso, Long> {
    Page<Curso> findByActivo(Boolean activo, Pageable pageable);
    Page<Curso> findByActivoAndCategoriaIdAndModalidadId(Boolean activo, Long categoriaId, Long modalidadId, Pageable pageable);
}