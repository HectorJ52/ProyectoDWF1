package sv.edu.udb.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import sv.edu.udb.model.Usuario;
import sv.edu.udb.repository.UsuarioRepository;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        Usuario usuario = usuarioRepository
                .findByUsername(username)
                .orElseGet(() ->
                        usuarioRepository
                                .findByCorreo(username)
                                .orElseThrow(() ->
                                        new UsernameNotFoundException(
                                                "Usuario no encontrado: " + username
                                        )
                                )
                );

        if (!usuario.isActivo()) {
            throw new UsernameNotFoundException(
                    "El usuario está inactivo"
            );
        }

        return User.builder()
                .username(usuario.getUsername())
                .password(usuario.getPassword())
                .roles(usuario.getRol().getNombre().name())
                .build();
    }
}