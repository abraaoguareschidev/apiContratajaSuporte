package com.contrataja.Contrataja.service;

import com.contrataja.Contrataja.repository.ContratacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ContratacaoService {

    private final ContratacaoRepository contratacaoRepository;

    public List<Map<String, Object>> listarContratacoesCliente(
            Integer idCliente) {

        return contratacaoRepository.listarContratacoesCliente(idCliente);
    }

    public List<Map<String, Object>> listarContratacoesProfissional(
            Integer idProfissional) {

        return contratacaoRepository.listarContratacoesProfissional(idProfissional);
    }

}
