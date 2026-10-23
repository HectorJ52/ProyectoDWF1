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

        // Crear los 4 usuarios de prueba (uno por cada rol)
        crearUsuarioSiNoExiste(
                "admin",
                "admin@catedra.com",
                "Admin123!",
                Rol.NombreRol.ADMIN
        );

        crearUsuarioSiNoExiste(
                "recepcion",
                "recepcion@catedra.com",
                "Recepcion123!",
                Rol.NombreRol.RECEPCIONISTA
        );

        crearUsuarioSiNoExiste(
                "cliente1",
                "cliente@catedra.com",
                "Cliente123!",
                Rol.NombreRol.CLIENTE
        );

        crearUsuarioSiNoExiste(
                "contabilidad",
                "conta@catedra.com",
                "Conta123!",
                Rol.NombreRol.CONTABILIDAD
        );
    }

    private void crearUsuarioSiNoExiste(
            String username,
            String correo,
            String passwordPlano,
            Rol.NombreRol nombreRol
    ) {
        // Si ya existe, no lo vuelve a crear
        if (usuarioRepository.findByUsername(username).isPresent()) {
            return;
        }

        // Buscar el rol
        Rol rol = rolRepository
                .findByNombre(nombreRol)
                .orElseThrow(() ->
                        new IllegalStateException(
                                "El rol " + nombreRol + " no existe. Revisa RoleInitializer."
                        )
                );

        // Crear el usuario con la contraseña encriptada
        Usuario usuario = Usuario.builder()
                .username(username)
                .correo(correo)
                .password(passwordEncoder.encode(passwordPlano))
                .activo(true)
                .rol(rol)
                .build();

        usuarioRepository.save(usuario);

        System.out.println("Usuario creado: " + username + " (" + nombreRol + ")");
    }
}