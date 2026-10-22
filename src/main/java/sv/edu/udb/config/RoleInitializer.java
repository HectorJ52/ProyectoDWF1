package sv.edu.udb.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import sv.edu.udb.model.Rol;
import sv.edu.udb.repository.RolRepository;

@Component
@RequiredArgsConstructor
@Order(1)
public class RoleInitializer implements CommandLineRunner {

    private final RolRepository rolRepository;

    @Override
    public void run(String... args) {

        crearRolSiNoExiste(Rol.NombreRol.ADMIN);
        crearRolSiNoExiste(Rol.NombreRol.RECEPCIONISTA);
        crearRolSiNoExiste(Rol.NombreRol.CLIENTE);
        crearRolSiNoExiste(Rol.NombreRol.CONTABILIDAD);
    }

    private void crearRolSiNoExiste(Rol.NombreRol nombreRol) {

        if (rolRepository.findByNombre(nombreRol).isEmpty()) {

            Rol rol = Rol.builder()
                    .nombre(nombreRol)
                    .build();

            rolRepository.save(rol);
        }
    }
}