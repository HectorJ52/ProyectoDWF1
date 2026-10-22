package sv.edu.udb.implementation;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sv.edu.udb.controller.response.UsuarioResponse;
import sv.edu.udb.model.Rol;
import sv.edu.udb.model.Usuario;
import sv.edu.udb.repository.RolRepository;
import sv.edu.udb.repository.UsuarioRepository;
import sv.edu.udb.service.UsuarioService;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public Page<UsuarioResponse> findAll(Pageable pageable) {
        return usuarioRepository.findAll(pageable)
                .map(this::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioResponse findById(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + id));
        return toResponse(usuario);
    }

    @Override
    @Transactional
    public UsuarioResponse save(Usuario usuario) {
        if (usuarioRepository.existsByUsername(usuario.getUsername())) {
            throw new IllegalArgumentException("El nombre de usuario ya está registrado");
        }
        if (usuarioRepository.existsByCorreo(usuario.getCorreo())) {
            throw new IllegalArgumentException("El correo ya está registrado");
        }
        if (usuario.getRol() == null || usuario.getRol().getNombre() == null) {
            throw new IllegalArgumentException("Debe proporcionar un rol válido para el usuario");
        }

        String nombreRolStr = usuario.getRol().getNombre().name();
        
        // Si en tu repositorio findByNombre espera el enum NombreRol directamente, 
        // pasamos el objeto original: usuario.getRol().getNombre()
        Rol rol = rolRepository.findByNombre(usuario.getRol().getNombre())
                .orElseThrow(() -> new EntityNotFoundException("Rol no encontrado: " + nombreRolStr));

        Usuario nuevoUsuario = Usuario.builder()
                .username(usuario.getUsername())
                .correo(usuario.getCorreo())
                .password(passwordEncoder.encode(usuario.getPassword()))
                .activo(true)
                .rol(rol)
                .build();

        return toResponse(usuarioRepository.save(nuevoUsuario));
    }

    @Override
    @Transactional
    public UsuarioResponse update(Long id, Usuario deEntrada) {
        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + id));

        usuarioRepository.findByUsername(deEntrada.getUsername())
                .filter(u -> !u.getId().equals(id))
                .ifPresent(u -> {
                    throw new IllegalArgumentException("El nombre de usuario ya está registrado");
                });

        usuarioRepository.findByCorreo(deEntrada.getCorreo())
                .filter(u -> !u.getId().equals(id))
                .ifPresent(u -> {
                    throw new IllegalArgumentException("El correo ya está registrado");
                });

        if (deEntrada.getRol() == null || deEntrada.getRol().getNombre() == null) {
            throw new IllegalArgumentException("Debe proporcionar un rol válido");
        }

        String nombreRolStr = deEntrada.getRol().getNombre().name();

        Rol rol = rolRepository.findByNombre(deEntrada.getRol().getNombre())
                .orElseThrow(() -> new EntityNotFoundException("Rol no encontrado: " + nombreRolStr));

        usuarioExistente.setUsername(deEntrada.getUsername());
        usuarioExistente.setCorreo(deEntrada.getCorreo());
        usuarioExistente.setPassword(passwordEncoder.encode(deEntrada.getPassword()));
        usuarioExistente.setRol(rol);

        return toResponse(usuarioRepository.save(usuarioExistente));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + id));
        usuario.setActivo(false);
        usuarioRepository.save(usuario);
    }

    private UsuarioResponse toResponse(Usuario usuario) {
        return UsuarioResponse.builder()
                .id(usuario.getId())
                .username(usuario.getUsername())
                .correo(usuario.getCorreo())
                .activo(usuario.isActivo())
                .rol(usuario.getRol().getNombre())
                .build();
    }
}