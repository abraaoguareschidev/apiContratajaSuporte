package com.contrataja.Contrataja.repository;

import com.contrataja.Contrataja.entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Integer> {

    boolean existsByEmail(String email);
    boolean existsByCpf(String cpf);

    Optional<UsuarioEntity> findByEmail(String email);

}
