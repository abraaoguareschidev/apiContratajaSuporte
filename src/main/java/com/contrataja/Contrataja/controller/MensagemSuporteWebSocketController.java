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
    // Ferramenta do Spring que envia mensagens para os usuários inscritos num destino do webSocket
    private final SimpMessagingTemplate messagingTemplate;
    // define qual endereço este método atende, igual a uma rota, quando o front envia uma mensagem para /app/chamado/ e o id do chamado, o spring chama este método, e guarda o id na variável idChamado, que está entre chaves
    @MessageMapping("/chamado/{idChamado}")

    //método que recebe a mensagem enviada pelo front salva no banco e manda para todos os inscritos no chamado, é void porque não devolve resposta direta, quem envia é o messagingTemplate
    public void receberMensagem(
            // pega o id do chamado direto do endereço da mensagem, igual ao pathvariable do http
            @DestinationVariable Integer idChamado
            // pega o corpo da mensagem enviada pelo front, que chega em json, e converte para o objeto mensagemenvio com o id do remetente e o conteúdo
            , @Payload MensagemEnvio mensagem) {
        // chama o service para validar o remetente e salvar a mensagem no banco, o retorno já vem convertido em dto
        MensagemSuporteResponseDTO resposta =
                mensagensService.enviarMensagem(idChamado, mensagem.idRemetente(), mensagem.conteudo()
                );
        // publica a mensagem salva no tópico do chamado, todos os usuários inscritos em /topic/chamado/ e o id recebem na hora, é isso que faz o chat funcionar em tempo real
        messagingTemplate.convertAndSend(
                "/topic/chamado/" + idChamado,
                resposta
        );
    }
    // formato da mensagem que o front envia, com o id de quem está enviando e o texto, usei record porque são só dados que não mudam e o java gera o construtor e os métodos de leitura sozinho
    public record MensagemEnvio(Integer idRemetente, String conteudo) {}
}