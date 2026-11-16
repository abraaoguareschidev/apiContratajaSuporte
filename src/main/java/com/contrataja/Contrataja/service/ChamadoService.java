package com.contrataja.Contrataja.service;

import com.contrataja.Contrataja.dto.AbrirChamadoDTO;
import com.contrataja.Contrataja.entity.ChamadoEntity;
import com.contrataja.Contrataja.entity.UsuarioEntity;
import com.contrataja.Contrataja.repository.ChamadoRepository;
import com.contrataja.Contrataja.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChamadoService {
    private final ChamadoRepository chamadoRepository;

    private final UsuarioRepository usuarioRepository;

    public ChamadoService(ChamadoRepository chamadoRepository, UsuarioRepository usuarioRepository) {
        this.chamadoRepository = chamadoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<ChamadoEntity> listarChamados(){
        return chamadoRepository.findAll();
    }

    public ChamadoEntity buscarChamadoPorId(Integer id){
        return chamadoRepository.findById(id).orElse(null);
    }

    public ChamadoEntity encerrarChamado(Integer idChamado) {

        ChamadoEntity chamado = buscarChamadoPorId(idChamado);

        if (chamado == null) {
            throw new RuntimeException("Chamado não encontrado.");
        }

        if ("fechado".equalsIgnoreCase(chamado.getStatus())) {
            throw new RuntimeException("Este chamado já está fechado.");
        }

        chamado.setStatus("fechado");
        chamado.setDataFechamento(LocalDateTime.now());

        return chamadoRepository.save(chamado);
    }

    public ChamadoEntity cadastrarChamado(AbrirChamadoDTO dto) {
        UsuarioEntity usuario = usuarioRepository.findById(dto.getIdUsuario()).orElse(null);
        if (usuario == null) { throw new RuntimeException("Usuário não encontrado."); }
        ChamadoEntity chamado = new ChamadoEntity();
        chamado.setUsuario(usuario);
        chamado.setAssunto(dto.getAssunto());
        chamado.setDescricao(dto.getDescricao());
        chamado.setStatus("aberto");
        return chamadoRepository.save(chamado);
    }

}