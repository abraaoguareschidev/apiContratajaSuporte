package com.contrataja.Contrataja.repository;

import com.contrataja.Contrataja.entity.MensagensSuporteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MensagensSuporteRepository extends JpaRepository<MensagensSuporteEntity, Integer> {
    List<MensagensSuporteEntity> findByChamado_IdChamadoOrderByDataEnvioAsc(Integer idChamado);
}
