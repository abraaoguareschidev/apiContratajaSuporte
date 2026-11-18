package com.contrataja.Contrataja.repository;

import com.contrataja.Contrataja.entity.VerificacaoEmailEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VerificacaoEmailRepository extends JpaRepository<VerificacaoEmailEntity, Integer> {
    Optional<VerificacaoEmailEntity> findByToken(String token);
}
