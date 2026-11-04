package com.contrataja.Contrataja.service;

import com.contrataja.Contrataja.dto.MensagemSuporteResponseDTO;
import com.contrataja.Contrataja.entity.MensagensSuporteEntity;
import com.contrataja.Contrataja.repository.ChamadoRepository;
import com.contrataja.Contrataja.repository.MensagensSuporteRepository;
import com.contrataja.Contrataja.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MensagensSuporteService {

    private final MensagensSuporteRepository mensagensRepository;
    private final UsuarioRepository usuarioRepository;
    private final ChamadoRepository chamadoRepository;

    public List<MensagemSuporteResponseDTO> listarPorChamado(Integer idChamado) {
        return mensagensRepository.findByChamado_IdChamadoOrderByDataEnvioAsc(idChamado).stream().map(this::converterParaDTO).toList();
    }

    public MensagemSuporteResponseDTO enviarMensagem(
            Integer idChamado, Integer idRemetente, String conteudo) {

        var chamado = chamadoRepository.findById(idChamado)
                .orElseThrow();

        var remetente = usuarioRepository.findById(idRemetente)
                .orElseThrow();

        boolean ehCliente = chamado.getUsuario().getIdUsuario()
                .equals(remetente.getIdUsuario());

        boolean ehSuporte = chamado.getSuporte() != null &&
                chamado.getSuporte().getIdUsuario()
                        .equals(remetente.getIdUsuario());

        if (!ehCliente && !ehSuporte) {
            throw new RuntimeException("Usuário não autorizado para este chamado.");
        }

        MensagensSuporteEntity mensagem = new MensagensSuporteEntity();

        mensagem.setChamado(chamado);
        mensagem.setRemetente(remetente);
        mensagem.setConteudo(conteudo);

        MensagensSuporteEntity salva = mensagensRepository.save(mensagem);

        return converterParaDTO(salva);
    }

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
