package com.contrataja.Contrataja.controller;


import com.contrataja.Contrataja.service.ContratacaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/contratacoes")
@RequiredArgsConstructor
public class ContratacaoController {
    private final ContratacaoService contratacaoService;

    @GetMapping("/cliente/{idCliente}")
    public List<Map<String, Object>> listarContratacoesCliente(@PathVariable Integer idCliente) {

        return contratacaoService.listarContratacoesCliente(idCliente);
    }

    @GetMapping("/profissional/{idProfissional}")
    public List<Map<String, Object>> listarContratacoesProfissional(@PathVariable Integer idProfissional) {

        return contratacaoService.listarContratacoesProfissional(idProfissional);
    }

}
