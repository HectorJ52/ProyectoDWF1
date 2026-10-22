package sv.edu.udb.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sv.edu.udb.controller.response.UsuarioResponse;
import sv.edu.udb.model.Usuario;

public interface UsuarioService {
    Page<UsuarioResponse> findAll(Pageable pageable);
    UsuarioResponse findById(Long id);
    UsuarioResponse save(Usuario usuario);
    UsuarioResponse update(Long id, Usuario usuario);
    void delete(Long id);
}