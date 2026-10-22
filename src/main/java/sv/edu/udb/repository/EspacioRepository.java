package sv.edu.udb.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sv.edu.udb.model.Espacio;
import sv.edu.udb.model.Espacio.TipoEspacio;

public interface EspacioRepository extends JpaRepository<Espacio, Long> {

    Page<Espacio> findByActivoTrue(Pageable pageable);

    Page<Espacio> findByTipoEspacio(TipoEspacio tipoEspacio, Pageable pageable);

    @Query("SELECT e FROM Espacio e WHERE e.activo = true AND e.capacidad >= :capacidadMinima")
    Page<Espacio> findDisponiblesPorCapacidad(@Param("capacidadMinima") Integer capacidadMinima, Pageable pageable);
}