package com.example.backend.repository;

import com.example.backend.model.Beneficio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;

@Repository
public interface BeneficioRepository extends JpaRepository<Beneficio, Long> {

    /**
     * Busca benefícios ativos
     */
    List<Beneficio> findByAtivoTrue();

    /**
     * Busca benefício por ID com lock pessimista para operações de transferência
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT b FROM Beneficio b WHERE b.id = :id")
    Optional<Beneficio> findByIdWithLock(@Param("id") Long id);

    /**
     * Busca benefícios por nome (case insensitive)
     */
    List<Beneficio> findByNomeContainingIgnoreCase(String nome);
}






