package com.contrataja.Contrataja.dto;

import java.time.LocalDateTime;

// dto é um objeto usaddo só para transportar dados, esse define o fformato da mensagem que o servidor devolve para quem esta no chat
//usei record porque é imutavel e não precisa de getters e setters
public record MensagemSuporteResponseDTO(
        //identificador unico da mensagem no banco de dados
        Integer idMensagem,
        // identificador do usuario que enviou a mensagem, que pode ser o cliente/profissional ou o suporte
        Integer idRemetente,
        String nomeRemetente,
        String conteudo,
        LocalDateTime dataEnvio,
        Boolean statusLeitura
) {
}