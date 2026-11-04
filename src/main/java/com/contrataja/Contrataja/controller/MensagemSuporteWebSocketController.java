package com.contrataja.Contrataja.controller;

import com.contrataja.Contrataja.dto.MensagemSuporteResponseDTO;
import com.contrataja.Contrataja.service.MensagensSuporteService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class MensagemSuporteWebSocketController {

    private final MensagensSuporteService mensagensService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chamado/{idChamado}")
    public void receberMensagem(
            @DestinationVariable Integer idChamado,
            @Payload MensagemEnvio mensagem) {

        MensagemSuporteResponseDTO resposta =
                mensagensService.enviarMensagem(
                        idChamado,
                        mensagem.idRemetente(),
                        mensagem.conteudo()
                );

        messagingTemplate.convertAndSend(
                "/topic/chamado/" + idChamado,
                resposta
        );
    }

    public record MensagemEnvio(Integer idRemetente, String conteudo) {}
}