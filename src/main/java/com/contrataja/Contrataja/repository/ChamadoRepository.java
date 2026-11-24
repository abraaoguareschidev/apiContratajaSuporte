package com.contrataja.Contrataja.repository;

import com.contrataja.Contrataja.entity.ChamadoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChamadoRepository extends JpaRepository<ChamadoEntity, Integer> {
    List<ChamadoEntity> findBySuporte_IdUsuario(Integer idSuporte);
}
