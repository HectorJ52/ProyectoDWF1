package sv.edu.udb.service;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sv.edu.udb.controller.response.ReservaEspacioResponse;
import sv.edu.udb.exception.EspacioOcupadoException;
import sv.edu.udb.implementation.ReservaEspacioServiceImpl;
import sv.edu.udb.model.Cliente;
import sv.edu.udb.model.Espacio;
import sv.edu.udb.model.ReservaEspacio;
import sv.edu.udb.repository.ClienteRepository;
import sv.edu.udb.repository.EspacioRepository;
import sv.edu.udb.repository.ReservaEspacioRepository;
import sv.edu.udb.service.mapper.ReservaEspacioMapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservaEspacioServiceTest {

    @Mock
    private ReservaEspacioRepository reservaRepository;

    @Mock
    private EspacioRepository espacioRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private ReservaEspacioMapper mapper;

    @InjectMocks
    private ReservaEspacioServiceImpl reservaEspacioService;

    private Cliente cliente;
    private Espacio espacio;
    private ReservaEspacio reservaEntrada;

    @BeforeEach
    void setUp() {
        cliente = new Cliente();
        cliente.setId(1L);

        espacio = new Espacio();
        espacio.setId(1L);

        reservaEntrada = ReservaEspacio.builder()
                .cliente(cliente)
                .espacio(espacio)
                .fechaInicio(LocalDate.of(2026, 11, 10))
                .fechaFin(LocalDate.of(2026, 11, 10))
                .horaInicio(LocalTime.of(9, 0))
                .horaFin(LocalTime.of(12, 0))
                .motivo("Capacitación corporativa")
                .montoTotal(new BigDecimal("150.00"))
                .estado(ReservaEspacio.EstadoReserva.PENDIENTE)
                .build();
    }

    // =========================================================
    // TEST 1: Caso de éxito - espacio libre
    // =========================================================
    @Test
    void debeRegistrarReservaCuandoEspacioEstaLibre() {
        // Arrange
        when(reservaRepository.findSolapamientos(
                anyLong(), any(), any(), any(), any(), isNull()
        )).thenReturn(Collections.emptyList()); // No hay conflictos

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(espacioRepository.findById(1L)).thenReturn(Optional.of(espacio));

        ReservaEspacio reservaGuardada = ReservaEspacio.builder()
                .id(10L)
                .cliente(cliente)
                .espacio(espacio)
                .fechaInicio(reservaEntrada.getFechaInicio())
                .fechaFin(reservaEntrada.getFechaFin())
                .horaInicio(reservaEntrada.getHoraInicio())
                .horaFin(reservaEntrada.getHoraFin())
                .motivo(reservaEntrada.getMotivo())
                .montoTotal(reservaEntrada.getMontoTotal())
                .estado(reservaEntrada.getEstado())
                .build();

        when(reservaRepository.save(any(ReservaEspacio.class))).thenReturn(reservaGuardada);
        when(mapper.toResponse(any(ReservaEspacio.class))).thenReturn(new ReservaEspacioResponse());

        // Act
        ReservaEspacioResponse resultado = reservaEspacioService.save(reservaEntrada);

        // Assert
        assertNotNull(resultado);
        verify(reservaRepository).findSolapamientos(anyLong(), any(), any(), any(), any(), isNull());
        verify(reservaRepository).save(any(ReservaEspacio.class));
        verify(clienteRepository).findById(1L);
        verify(espacioRepository).findById(1L);
    }

    // =========================================================
    // TEST 2: Caso de fallo por solapamiento
    // =========================================================
    @Test
    void debeLanzarExcepcionCuandoHaySolapamiento() {
        // Arrange
        ReservaEspacio reservaExistente = ReservaEspacio.builder()
                .id(99L)
                .build();

        when(reservaRepository.findSolapamientos(
                anyLong(), any(), any(), any(), any(), isNull()
        )).thenReturn(List.of(reservaExistente)); // Sí hay conflicto

        // Act & Assert
        EspacioOcupadoException exception = assertThrows(
                EspacioOcupadoException.class,
                () -> reservaEspacioService.save(reservaEntrada)
        );

        assertTrue(exception.getMessage().contains("ya está reservado"));
        assertTrue(exception.getMessage().contains("99"));

        // No debe intentar guardar
        verify(reservaRepository, never()).save(any());
        verify(clienteRepository, never()).findById(anyLong());
    }

    // =========================================================
    // TEST 3: Caso de fallo por lógica de negocio
    // =========================================================
    @Test
    void debeLanzarExcepcionCuandoClienteNoExiste() {
        // Arrange
        when(reservaRepository.findSolapamientos(
                anyLong(), any(), any(), any(), any(), isNull()
        )).thenReturn(Collections.emptyList()); // Espacio libre

        when(clienteRepository.findById(1L)).thenReturn(Optional.empty()); // Cliente no existe

        // Act & Assert
        assertThrows(
                EntityNotFoundException.class,
                () -> reservaEspacioService.save(reservaEntrada)
        );

        // No debe guardar
        verify(reservaRepository, never()).save(any());
    }

    // =========================================================
    // TEST EXTRA (opcional pero recomendado)
    // =========================================================
    @Test
    void debeLanzarExcepcionCuandoNoSeProporcionaEspacio() {
        // Arrange
        reservaEntrada.setEspacio(null);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> reservaEspacioService.save(reservaEntrada)
        );

        assertTrue(exception.getMessage().contains("espacio válido"));
        verify(reservaRepository, never()).findSolapamientos(anyLong(), any(), any(), any(), any(), any());
        verify(reservaRepository, never()).save(any());
    }
}