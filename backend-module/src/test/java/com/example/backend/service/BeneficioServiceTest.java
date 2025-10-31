package com.example.backend.service;

import com.example.backend.model.Beneficio;
import com.example.backend.repository.BeneficioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BeneficioServiceTest {

    @Mock
    private BeneficioRepository beneficioRepository;

    @InjectMocks
    private BeneficioService beneficioService;

    private Beneficio beneficioOrigem;
    private Beneficio beneficioDestino;

    @BeforeEach
    void setUp() {
        beneficioOrigem = new Beneficio();
        beneficioOrigem.setId(1L);
        beneficioOrigem.setNome("Benefício Origem");
        beneficioOrigem.setValor(new BigDecimal("1000.00"));
        beneficioOrigem.setAtivo(true);

        beneficioDestino = new Beneficio();
        beneficioDestino.setId(2L);
        beneficioDestino.setNome("Benefício Destino");
        beneficioDestino.setValor(new BigDecimal("500.00"));
        beneficioDestino.setAtivo(true);
    }

    @Test
    void testTransferirComSucesso() {
        // Arrange
        BigDecimal valorTransferencia = new BigDecimal("200.00");
        when(beneficioRepository.findByIdWithLock(1L)).thenReturn(Optional.of(beneficioOrigem));
        when(beneficioRepository.findByIdWithLock(2L)).thenReturn(Optional.of(beneficioDestino));

        // Act
        beneficioService.transferir(1L, 2L, valorTransferencia);

        // Assert
        assertEquals(new BigDecimal("800.00"), beneficioOrigem.getValor());
        assertEquals(new BigDecimal("700.00"), beneficioDestino.getValor());
    }

    @Test
    void testTransferirComSaldoInsuficiente() {
        // Arrange
        BigDecimal valorTransferencia = new BigDecimal("1500.00");
        when(beneficioRepository.findByIdWithLock(1L)).thenReturn(Optional.of(beneficioOrigem));
        when(beneficioRepository.findByIdWithLock(2L)).thenReturn(Optional.of(beneficioDestino));

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> {
            beneficioService.transferir(1L, 2L, valorTransferencia);
        });
    }

    @Test
    void testTransferirComValorNegativo() {
        // Arrange
        BigDecimal valorTransferencia = new BigDecimal("-100.00");

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            beneficioService.transferir(1L, 2L, valorTransferencia);
        });
    }

    @Test
    void testTransferirParaMesmoId() {
        // Arrange
        BigDecimal valorTransferencia = new BigDecimal("100.00");

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            beneficioService.transferir(1L, 1L, valorTransferencia);
        });
    }

    @Test
    void testTransferirComBeneficioInativo() {
        // Arrange
        beneficioOrigem.setAtivo(false);
        BigDecimal valorTransferencia = new BigDecimal("100.00");
        when(beneficioRepository.findByIdWithLock(1L)).thenReturn(Optional.of(beneficioOrigem));
        when(beneficioRepository.findByIdWithLock(2L)).thenReturn(Optional.of(beneficioDestino));

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> {
            beneficioService.transferir(1L, 2L, valorTransferencia);
        });
    }

    @Test
    void testCriarBeneficio() {
        // Arrange
        Beneficio novoBeneficio = new Beneficio("Novo Benefício", "Descrição", new BigDecimal("300.00"));
        when(beneficioRepository.save(any(Beneficio.class))).thenReturn(novoBeneficio);

        // Act
        Beneficio resultado = beneficioService.criar(novoBeneficio);

        // Assert
        assertNotNull(resultado);
        assertEquals("Novo Benefício", resultado.getNome());
        verify(beneficioRepository, times(1)).save(novoBeneficio);
    }

    @Test
    void testBuscarPorId() {
        // Arrange
        when(beneficioRepository.findById(1L)).thenReturn(Optional.of(beneficioOrigem));

        // Act
        Beneficio resultado = beneficioService.buscarPorId(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Benefício Origem", resultado.getNome());
    }

    @Test
    void testBuscarPorIdNaoEncontrado() {
        // Arrange
        when(beneficioRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            beneficioService.buscarPorId(999L);
        });
    }
}






