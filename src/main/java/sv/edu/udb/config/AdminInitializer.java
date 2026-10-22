package sv.edu.udb.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import sv.edu.udb.model.Rol;
import sv.edu.udb.model.Usuario;
import sv.edu.udb.repository.RolRepository;
import sv.edu.udb.repository.UsuarioRepository;

@Component
@RequiredArgsConstructor
@Order(2)
public class AdminInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        if (usuarioRepository.findByUsername("admin").isPresent()) {
            return;
        }

        Rol rolAdmin = rolRepository
                .findByNombre(Rol.NombreRol.ADMIN)
                .orElseThrow(() ->
                        new IllegalStateException(
                                "El rol ADMIN no existe"
                        )
                );

        Usuario admin = Usuario.builder()
                .username("admin")
                .correo("admin@catedra.com")
                .password(
                        passwordEncoder.encode("Admin123!")
                )
                .activo(true)
                .rol(rolAdmin)
                .build();

        usuarioRepository.save(admin);
    }
}