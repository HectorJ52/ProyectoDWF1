package sv.edu.udb.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import sv.edu.udb.controller.response.LoginResponse;
import sv.edu.udb.model.Usuario;
import sv.edu.udb.repository.UsuarioRepository;
import sv.edu.udb.security.JwtService;
import sv.edu.udb.security.LoginRequest;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticación", description = "Endpoints para inicio de sesión")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;

    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {

        // Buscamos al usuario de forma segura tolerando cualquier variación de columna
        Usuario usuario = usuarioRepository.findByUsername(request.getUsuario())
                .orElseGet(() -> usuarioRepository.findByCorreo(request.getUsuario())
                .orElseGet(() -> usuarioRepository.findAll().stream()
                        .filter(u -> u.getUsername() != null && u.getUsername().equalsIgnoreCase(request.getUsuario()))
                        .findFirst()
                        .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado en el sistema"))));

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        usuario.getUsername(),
                        request.getPassword()
                )
        );

        String token = jwtService.generateToken(usuario);

        LoginResponse response = LoginResponse.builder()
                        .token(token)
                        .tipo("Bearer")
                        .username(usuario.getUsername())
                        .rol(usuario.getRol().getNombre())
                        .build();

        return ResponseEntity.ok(response);
    }
}