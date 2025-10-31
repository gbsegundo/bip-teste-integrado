package com.example.backend.service;

import com.example.backend.model.Beneficio;
import com.example.backend.repository.BeneficioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class BeneficioService {

    private final BeneficioRepository beneficioRepository;

    public BeneficioService(BeneficioRepository beneficioRepository) {
        this.beneficioRepository = beneficioRepository;
    }

    /**
     * Lista todos os benefícios
     */
    @Transactional(readOnly = true)
    public List<Beneficio> listarTodos() {
        return beneficioRepository.findAll();
    }

    /**
     * Lista apenas benefícios ativos
     */
    @Transactional(readOnly = true)
    public List<Beneficio> listarAtivos() {
        return beneficioRepository.findByAtivoTrue();
    }

    /**
     * Busca benefício por ID
     */
    @Transactional(readOnly = true)
    public Beneficio buscarPorId(Long id) {
        return beneficioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Benefício não encontrado com ID: " + id));
    }

    /**
     * Cria um novo benefício
     */
    @Transactional
    public Beneficio criar(Beneficio beneficio) {
        if (beneficio.getId() != null) {
            throw new IllegalArgumentException("Novo benefício não deve ter ID");
        }
        return beneficioRepository.save(beneficio);
    }

    /**
     * Atualiza um benefício existente
     */
    @Transactional
    public Beneficio atualizar(Long id, Beneficio beneficioAtualizado) {
        Beneficio beneficio = buscarPorId(id);
        
        beneficio.setNome(beneficioAtualizado.getNome());
        beneficio.setDescricao(beneficioAtualizado.getDescricao());
        beneficio.setValor(beneficioAtualizado.getValor());
        beneficio.setAtivo(beneficioAtualizado.getAtivo());
        
        return beneficioRepository.save(beneficio);
    }

    /**
     * Remove um benefício (soft delete)
     */
    @Transactional
    public void desativar(Long id) {
        Beneficio beneficio = buscarPorId(id);
        beneficio.setAtivo(false);
        beneficioRepository.save(beneficio);
    }

    /**
     * Remove permanentemente um benefício
     */
    @Transactional
    public void deletar(Long id) {
        if (!beneficioRepository.existsById(id)) {
            throw new IllegalArgumentException("Benefício não encontrado com ID: " + id);
        }
        beneficioRepository.deleteById(id);
    }

    /**
     * Transfere valor entre benefícios com validações e locking
     */
    @Transactional
    public void transferir(Long fromId, Long toId, BigDecimal amount) {
        // Validação 1: Verificar parâmetros nulos
        if (fromId == null) {
            throw new IllegalArgumentException("ID de origem não pode ser nulo");
        }
        if (toId == null) {
            throw new IllegalArgumentException("ID de destino não pode ser nulo");
        }
        if (amount == null) {
            throw new IllegalArgumentException("Valor da transferência não pode ser nulo");
        }

        // Validação 2: Verificar se o valor é positivo
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor da transferência deve ser maior que zero");
        }

        // Validação 3: Verificar se origem e destino são diferentes
        if (fromId.equals(toId)) {
            throw new IllegalArgumentException("Origem e destino não podem ser iguais");
        }

        // Bloquear registros na ordem consistente (menor ID primeiro) para evitar deadlock
        Long firstId = fromId < toId ? fromId : toId;
        Long secondId = fromId < toId ? toId : fromId;

        Beneficio first = beneficioRepository.findByIdWithLock(firstId)
                .orElseThrow(() -> new IllegalArgumentException("Benefício com ID " + firstId + " não encontrado"));
        Beneficio second = beneficioRepository.findByIdWithLock(secondId)
                .orElseThrow(() -> new IllegalArgumentException("Benefício com ID " + secondId + " não encontrado"));

        Beneficio from = fromId.equals(firstId) ? first : second;
        Beneficio to = toId.equals(firstId) ? first : second;

        // Validação 4: Verificar se os benefícios estão ativos
        if (!Boolean.TRUE.equals(from.getAtivo())) {
            throw new IllegalStateException("Benefício de origem não está ativo");
        }
        if (!Boolean.TRUE.equals(to.getAtivo())) {
            throw new IllegalStateException("Benefício de destino não está ativo");
        }

        // Validação 5: Verificar saldo suficiente (evitar saldo negativo)
        BigDecimal newFromValor = from.getValor().subtract(amount);
        if (newFromValor.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalStateException(
                    String.format("Saldo insuficiente. Saldo atual: %s, Valor da transferência: %s",
                            from.getValor(), amount)
            );
        }

        // Realizar a transferência
        from.setValor(newFromValor);
        to.setValor(to.getValor().add(amount));

        // As entidades são gerenciadas, mudanças serão persistidas no commit
    }

    /**
     * Busca benefícios por nome
     */
    @Transactional(readOnly = true)
    public List<Beneficio> buscarPorNome(String nome) {
        return beneficioRepository.findByNomeContainingIgnoreCase(nome);
    }
}






