package com.contrataja.Contrataja.repository;

import com.contrataja.Contrataja.entity.VerificacaoEmailEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface VerificacaoEmailRepository extends JpaRepository<VerificacaoEmailEntity, Integer> {
    Optional<VerificacaoEmailEntity> findByToken(String token);
    void deleteByUsuario_IdUsuario(Integer idUsuario);
    boolean existsByUsuario_IdUsuarioAndExpiracaoAfter(Integer idUsuario, LocalDateTime expiracao);
}
