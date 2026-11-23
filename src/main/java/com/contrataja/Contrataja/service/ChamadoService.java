package com.contrataja.Contrataja.service;

import com.contrataja.Contrataja.dto.AbrirChamadoDTO;
import com.contrataja.Contrataja.entity.ChamadoEntity;
import com.contrataja.Contrataja.entity.SuporteEntity;
import com.contrataja.Contrataja.entity.UsuarioEntity;
import com.contrataja.Contrataja.repository.ChamadoRepository;
import com.contrataja.Contrataja.repository.UsuarioRepository;
import lombok.AllArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class ChamadoService {
    private final ChamadoRepository chamadoRepository;
    private final UsuarioRepository usuarioRepository;
    private final SimpMessagingTemplate messagingTemplate;


    public List<ChamadoEntity> listarChamados(){
        return chamadoRepository.findAll();
    }

    public ChamadoEntity buscarChamadoPorId(Integer id){
        return chamadoRepository.findById(id).orElse(null);
    }

    public ChamadoEntity encerrarChamado(Integer idChamado, String solucao) {

        ChamadoEntity chamado = buscarChamadoPorId(idChamado);

        if (chamado == null) {
            throw new RuntimeException("Chamado não encontrado.");
        }

        if ("fechado".equalsIgnoreCase(chamado.getStatus())) {
            throw new RuntimeException("Este chamado já está fechado.");
        }

        if (solucao == null || solucao.isBlank()) {
            throw new RuntimeException("Informe a solução do chamado.");
        }

        chamado.setSolucao(solucao.trim());
        chamado.setStatus("fechado");
        chamado.setDataFechamento(LocalDateTime.now());

        ChamadoEntity chamadoSalvo = chamadoRepository.save(chamado);

        messagingTemplate.convertAndSend(
                "/topic/chamados",
                "CHAMADO_ENCERRADO"
        );

        return chamadoSalvo;
    }

    public ChamadoEntity cadastrarChamado(AbrirChamadoDTO dto) {
        UsuarioEntity usuario = usuarioRepository.findById(dto.getIdUsuario()).orElse(null);
        if (usuario == null) { throw new RuntimeException("Usuário não encontrado."); }
        ChamadoEntity chamado = new ChamadoEntity();
        chamado.setUsuario(usuario);
        chamado.setAssunto(dto.getAssunto());
        chamado.setDescricao(dto.getDescricao());
        chamado.setStatus("aberto");

        ChamadoEntity chamadoSalvo = chamadoRepository.save(chamado);

        messagingTemplate.convertAndSend("/topic/chamados","NOVO_CHAMADO");

        return chamadoSalvo;

    }

    public ChamadoEntity assumirChamado(Integer idChamado, Integer idSuporte) {
        ChamadoEntity chamado = buscarChamadoPorId(idChamado);

        if (chamado == null) {
            throw new RuntimeException("Chamado não encontrado.");
        }
        if (!"aberto".equalsIgnoreCase(chamado.getStatus())) {
            throw new RuntimeException("Este chamado não está disponível para atendimento.");
        }
        UsuarioEntity usuario = usuarioRepository.findById(idSuporte)
                .orElseThrow(() -> new RuntimeException("Suporte não encontrado."));

        if (!(usuario instanceof SuporteEntity)) {
            throw new RuntimeException("O usuário informado não é um suporte.");
        }

        chamado.setSuporte((SuporteEntity) usuario);

        chamado.setStatus("em_atendimento");

        ChamadoEntity chamadoSalvo = chamadoRepository.save(chamado);

        messagingTemplate.convertAndSend(
                "/topic/chamados",
                "CHAMADO_ASSUMIDO"
        );

        return chamadoSalvo;
    }

}