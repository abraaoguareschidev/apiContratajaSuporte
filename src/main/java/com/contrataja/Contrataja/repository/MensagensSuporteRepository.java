package com.contrataja.Contrataja.repository;

import com.contrataja.Contrataja.entity.MensagensSuporteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MensagensSuporteRepository extends JpaRepository<MensagensSuporteEntity, Integer> {
    List<MensagensSuporteEntity> findByChamado_IdChamadoOrderByDataEnvioAsc(Integer idChamado);
    //Informa ao Spring Data JPA que essa consulta vai modificar os dados do banco, em vez de apenas buscar informações
    @Modifying
    //Permite escrever uma consulta JPQL personalizada
    @Query(""" 
    UPDATE MensagensSuporteEntity m
    SET m.statusLeitura = true
    WHERE m.chamado.idChamado = :idChamado
    AND m.remetente.idUsuario <> :idUsuario
    AND m.statusLeitura = false """)
    void marcarComoLidas(
            @Param("idChamado") Integer idChamado,
            @Param("idUsuario") Integer idUsuario);
}
