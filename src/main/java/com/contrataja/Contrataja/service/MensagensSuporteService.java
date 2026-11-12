package com.contrataja.Contrataja.service;

import com.contrataja.Contrataja.dto.MensagemSuporteResponseDTO;
import com.contrataja.Contrataja.entity.MensagensSuporteEntity;
import com.contrataja.Contrataja.repository.ChamadoRepository;
import com.contrataja.Contrataja.repository.MensagensSuporteRepository;
import com.contrataja.Contrataja.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor//anotação do lombok que cria automaticamente o construtor com os atributos final da classe é por ele que o spring injeta as dependencias
public class MensagensSuporteService {

    // repositorios usados para acessar o banco, coloco final para o spring injetar pelo construtor
    private final MensagensSuporteRepository mensagensRepository;
    private final UsuarioRepository usuarioRepository;
    private final ChamadoRepository chamadoRepository;

    //busca no banco todas as chamadas de um chamado em ordem de envio e converte cada uma para dto antes de devolver
    public List<MensagemSuporteResponseDTO> listarPorChamado(Integer idChamado) {
        return mensagensRepository.findByChamado_IdChamadoOrderByDataEnvioAsc(idChamado).stream().map(this::converterParaDTO).toList();
    }

    //Recebe o chamado, o remetente e o texto, valida se o remetente pode falar naquele chamado, salva a mensagem no banco e devolve ela já convertida em dto
    public MensagemSuporteResponseDTO enviarMensagem(
            Integer idChamado, Integer idRemetente, String conteudo) {
        //busca o chamado no banco pelo id Se não existir lança uma exceção e interrompe o envio
        var chamado = chamadoRepository.findById(idChamado).orElseThrow();
        // busca no banco o usuário que está enviando a mensagem  Se não existir lança uma exceção e interrompe o envio
        var remetente = usuarioRepository.findById(idRemetente).orElseThrow();
        //verifica se o remetente é o cliente que abriu o chamado comparando o id do usuario que  abriu chamado com o id do remetente
        boolean ehCliente = chamado.getUsuario().getIdUsuario().equals(remetente.getIdUsuario());
        //verifica se o remetente é o atendente responsável pelo chamado. Primeiro confere se já existe um suporte atribuído para não dar erro de valor nulo, e depois compara os ids
        boolean ehSuporte = chamado.getSuporte() != null && chamado.getSuporte().getIdUsuario().equals(remetente.getIdUsuario());
        //se o remetente não for nem o cliete / profisionl nem o atendente do chamado, lança uma exceção e bloqueia o envio.
        if (!ehCliente && !ehSuporte) {
            throw new RuntimeException("Usuário não autorizado para este chamado.");
        }
        // Cria uma nova mensagem que ainda não foi salva no banco. Nas próximas linhas eu preencho os dados dela
        MensagensSuporteEntity mensagem = new MensagensSuporteEntity();
        // Preenche a mensagem com o chamado ao qual ela pertence, o usuário que enviou e o texto digitado
        mensagem.setChamado(chamado);
        mensagem.setRemetente(remetente);
        mensagem.setConteudo(conteudo);
        // Grava a mensagem no banco de dados o save devolve a mensagem já salva
        MensagensSuporteEntity salva = mensagensRepository.save(mensagem);
        // Converte a mensagem salva em dto e devolve esse dto é o que o controller manda para o  tópico para os usuários do chat
        return converterParaDTO(salva);
    }
    @Transactional
    public void marcarMensagensComoLidas(Integer idChamado, Integer idUsuario) {
        mensagensRepository.marcarComoLidas(idChamado, idUsuario);
    }
    // método  que transforma a entidade do banco em dto pegando só os dados que o chat precisa
    private MensagemSuporteResponseDTO converterParaDTO(MensagensSuporteEntity mensagem) {
        return new MensagemSuporteResponseDTO(
                mensagem.getIdMensagem(),
                mensagem.getRemetente().getIdUsuario(),
                mensagem.getRemetente().getNome(),
                mensagem.getConteudo(),
                mensagem.getDataEnvio(),
                mensagem.getStatusLeitura()
        );
    }
}
