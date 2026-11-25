package com.contrataja.Contrataja.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class ContratacaoRepository {

    private final JdbcTemplate jdbcTemplate;

    public List<Map<String, Object>> listarContratacoesCliente(
            Integer idCliente) {

        String sql = """
                SELECT
                    c.id_contratacao,
                    pd.id_cliente,
                    cliente.nome AS nome_cliente,
                    p.id_profissional,
                    profissional.nome AS nome_profissional,
                    s.nome AS nome_servico,
                    pd.descricao AS descricao_pedido,
                    p.descricao AS descricao_proposta,
                    c.data_contratacao,
                    c.data_inicio,
                    c.data_conclusao,
                    c.valor_final,
                    c.status
                FROM contratacao c
                INNER JOIN proposta p
                    ON c.id_proposta = p.id_proposta
                INNER JOIN pedido pd
                    ON p.id_pedido = pd.id_pedido
                INNER JOIN servico s
                    ON c.id_servico = s.id_servico
                INNER JOIN usuario cliente
                    ON pd.id_cliente = cliente.id_usuario
                INNER JOIN usuario profissional
                    ON p.id_profissional = profissional.id_usuario
                WHERE pd.id_cliente = ?
                ORDER BY c.data_contratacao DESC
                """;

        return jdbcTemplate.queryForList(sql, idCliente);
    }

    public List<Map<String, Object>> listarContratacoesProfissional(Integer idProfissional) {
        String sql = """
                SELECT
                   c.id_contratacao,
                   pd.id_cliente,
                   cliente.nome AS nome_cliente,
                   p.id_profissional,
                   profissional.nome AS nome_profissional,
                   s.nome AS nome_servico,
                   pd.descricao AS descricao_pedido,
                   p.descricao AS descricao_proposta,
                   c.data_contratacao,
                   c.data_inicio,
                   c.data_conclusao,
                   c.valor_final,
                   c.status
                   FROM contratacao c
                   INNER JOIN proposta p
                   ON c.id_proposta = p.id_proposta
                   INNER JOIN pedido pd
                   ON p.id_pedido = pd.id_pedido
                   INNER JOIN servico s
                   ON c.id_servico = s.id_servico
                   INNER JOIN usuario cliente
                   ON pd.id_cliente = cliente.id_usuario
                   INNER JOIN usuario profissional
                   ON p.id_profissional = profissional.id_usuario
                   WHERE p.id_profissional = ?
                   ORDER BY c.data_contratacao DESC
                """;

        return jdbcTemplate.queryForList(sql, idProfissional);
    }

}
