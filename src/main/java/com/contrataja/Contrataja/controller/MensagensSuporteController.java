package com.contrataja.Contrataja.controller;

import com.contrataja.Contrataja.dto.MensagemSuporteResponseDTO;
import com.contrataja.Contrataja.entity.MensagensSuporteEntity;
import com.contrataja.Contrataja.service.MensagensSuporteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mensagens-suporte")
@RequiredArgsConstructor
public class MensagensSuporteController {

    private final MensagensSuporteService mensagensSuporteService;

    @GetMapping("/chamado/{idChamado}")
    public List<MensagemSuporteResponseDTO> listarPorChamado(@PathVariable Integer idChamado) {
        return mensagensSuporteService.listarPorChamado(idChamado);
    }
    @PostMapping("/enviar")
    public MensagemSuporteResponseDTO enviarMensagem(@RequestParam Integer idChamado, @RequestParam Integer idRemetente, @RequestParam String conteudo) {

        return mensagensSuporteService.enviarMensagem(idChamado, idRemetente, conteudo);
    }

    @PutMapping("/chamado/{idChamado}/lidas")
    public void marcarComoLidas(@PathVariable Integer idChamado,@RequestParam Integer idUsuario) {

        mensagensSuporteService.marcarMensagensComoLidas(idChamado,idUsuario);
    }

}
