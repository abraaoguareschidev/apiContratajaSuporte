package com.contrataja.Contrataja.dto;

import java.time.LocalDateTime;

public record MensagemSuporteResponseDTO(
        Integer idMensagem,
        Integer idRemetente,
        String nomeRemetente,
        String conteudo,
        LocalDateTime dataEnvio,
        Boolean statusLeitura
) {
}