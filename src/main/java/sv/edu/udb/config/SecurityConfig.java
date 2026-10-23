package sv.edu.udb.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import sv.edu.udb.security.JwtAuthenticationFilter;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth

                        // Públicos
                        .requestMatchers(
                                "/api/auth/**",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-ui.html"
                        ).permitAll()

                        // Solo ADMIN: usuarios y roles
                        .requestMatchers("/api/usuarios/**").hasRole("ADMIN")

                        // ADMIN y CONTABILIDAD: pagos
                        .requestMatchers("/api/pagos/**")
                        .hasAnyRole("ADMIN", "CONTABILIDAD")

                        // ADMIN y RECEPCIONISTA: operaciones diarias
                        .requestMatchers(
                                "/api/clientes/**",
                                "/api/inscripciones/**",
                                "/api/reservas-espacio/**",
                                "/api/solicitudes-catering/**",
                                "/api/cotizaciones/**",
                                "/api/detalles-cotizacion/**"
                        ).hasAnyRole("ADMIN", "RECEPCIONISTA")

                        // Catálogos: lectura para varios roles, escritura solo ADMIN/RECEPCIONISTA
                        .requestMatchers(HttpMethod.GET,
                                "/api/espacios/**",
                                "/api/equipamientos/**",
                                "/api/servicios-catering/**",
                                "/api/cursos/**",
                                "/api/diplomados/**",
                                "/api/categorias/**",
                                "/api/modalidades/**",
                                "/api/docentes/**"
                        ).hasAnyRole("ADMIN", "RECEPCIONISTA", "CLIENTE", "CONTABILIDAD")

                        .requestMatchers(HttpMethod.POST, "/api/espacios/**", "/api/equipamientos/**",
                                "/api/servicios-catering/**", "/api/cursos/**", "/api/diplomados/**",
                                "/api/categorias/**", "/api/modalidades/**", "/api/docentes/**")
                        .hasAnyRole("ADMIN", "RECEPCIONISTA")

                        .requestMatchers(HttpMethod.PUT, "/api/espacios/**", "/api/equipamientos/**",
                                "/api/servicios-catering/**", "/api/cursos/**", "/api/diplomados/**",
                                "/api/categorias/**", "/api/modalidades/**", "/api/docentes/**")
                        .hasAnyRole("ADMIN", "RECEPCIONISTA")

                        .requestMatchers(HttpMethod.DELETE, "/api/espacios/**", "/api/equipamientos/**",
                                "/api/servicios-catering/**", "/api/cursos/**", "/api/diplomados/**",
                                "/api/categorias/**", "/api/modalidades/**", "/api/docentes/**")
                        .hasRole("ADMIN")

                        // CLIENTE: solo consultas propias
                        // El resto requiere autenticación
                        .anyRequest().authenticated()
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}