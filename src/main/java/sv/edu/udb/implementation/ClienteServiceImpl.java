package sv.edu.udb.implementation;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sv.edu.udb.controller.response.ClienteResponse;
import sv.edu.udb.model.Cliente;
import sv.edu.udb.repository.ClienteRepository;
import sv.edu.udb.service.ClienteService;
import sv.edu.udb.service.mapper.ClienteMapper;

@Service
@RequiredArgsConstructor
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;
    private final ClienteMapper clienteMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<ClienteResponse> findAllPaginated(Boolean activo, Pageable pageable) {
        return clienteRepository.findByActivo(activo, pageable)
                .map(clienteMapper::toClienteResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public ClienteResponse findById(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado con ID: " + id));
        return clienteMapper.toClienteResponse(cliente);
    }

    @Override
    @Transactional
    public ClienteResponse save(Cliente cliente) {
        if (clienteRepository.existsByDocumento(cliente.getDocumento())) {
            throw new IllegalArgumentException("Ya existe un cliente con el documento: " + cliente.getDocumento());
        }
        cliente.setActivo(true);
        return clienteMapper.toClienteResponse(clienteRepository.save(cliente));
    }

    @Override
    @Transactional
    public ClienteResponse update(Long id, Cliente cliente) {
        Cliente clienteExistente = clienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado con ID: " + id));

        if (!clienteExistente.getDocumento().equals(cliente.getDocumento())
                && clienteRepository.existsByDocumento(cliente.getDocumento())) {
            throw new IllegalArgumentException("Ya existe otro cliente con el documento: " + cliente.getDocumento());
        }

        clienteExistente.setNombres(cliente.getNombres());
        clienteExistente.setApellidos(cliente.getApellidos());
        clienteExistente.setDocumento(cliente.getDocumento());
        clienteExistente.setCorreo(cliente.getCorreo());
        clienteExistente.setTelefono(cliente.getTelefono());
        clienteExistente.setDireccion(cliente.getDireccion());

        return clienteMapper.toClienteResponse(clienteRepository.save(clienteExistente));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado con ID: " + id));
        cliente.setActivo(false);
        clienteRepository.save(cliente);
    }
}