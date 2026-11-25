package com.contrataja.Contrataja.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


@Repository
@RequiredArgsConstructor
public class ConversaRepository {
    private final JdbcTemplate jdbcTemplate;

    public List<Map<String, Object>> listarMensagens(
            Integer idCliente, Integer idProfissional) {

        String sql = """
                SELECT
                    c.id_conversa,
                    m.id_mensagem,
                    m.id_remetente,
                    u.nome AS nome_remetente,
                    m.conteudo,
                    m.data_envio
                FROM conversa c
                INNER JOIN mensagens m
                    ON c.id_conversa = m.id_conversa
                INNER JOIN usuario u
                    ON m.id_remetente = u.id_usuario
                WHERE c.id_cliente = ?
                    AND c.id_profissional = ?
                    AND m.id_remetente IN (
                        c.id_cliente, c.id_profissional
                    )
                ORDER BY m.data_envio ASC, m.id_mensagem ASC
                """;

        return jdbcTemplate.queryForList(
                sql, idCliente, idProfissional
        );
    }
}
