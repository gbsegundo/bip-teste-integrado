package com.example.ejb;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import java.math.BigDecimal;

@Stateless
public class BeneficioEjbService {

    @PersistenceContext
    private EntityManager em;

    public void transfer(Long fromId, Long toId, BigDecimal amount) {
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

        // CORREÇÃO: Usar PESSIMISTIC_WRITE lock para evitar lost updates
        // Bloquear registros na ordem consistente (menor ID primeiro) para evitar deadlock
        Long firstId = fromId < toId ? fromId : toId;
        Long secondId = fromId < toId ? toId : fromId;

        Beneficio first = em.find(Beneficio.class, firstId, LockModeType.PESSIMISTIC_WRITE);
        Beneficio second = em.find(Beneficio.class, secondId, LockModeType.PESSIMISTIC_WRITE);

        // Validação 4: Verificar se os benefícios existem
        Beneficio from = fromId.equals(firstId) ? first : second;
        Beneficio to = toId.equals(firstId) ? first : second;

        if (from == null) {
            throw new IllegalArgumentException("Benefício de origem com ID " + fromId + " não encontrado");
        }
        if (to == null) {
            throw new IllegalArgumentException("Benefício de destino com ID " + toId + " não encontrado");
        }

        // Validação 5: Verificar se os benefícios estão ativos
        if (!Boolean.TRUE.equals(from.getAtivo())) {
            throw new IllegalStateException("Benefício de origem não está ativo");
        }
        if (!Boolean.TRUE.equals(to.getAtivo())) {
            throw new IllegalStateException("Benefício de destino não está ativo");
        }

        // Validação 6: Verificar saldo suficiente (evitar saldo negativo)
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

        // O merge é desnecessário pois as entidades já estão gerenciadas (managed)
        // e as mudanças serão automaticamente sincronizadas no commit da transação
    }
}
