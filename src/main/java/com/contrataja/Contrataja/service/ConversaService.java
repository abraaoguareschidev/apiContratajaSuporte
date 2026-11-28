package com.contrataja.Contrataja.service;

import com.contrataja.Contrataja.repository.ConversaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ConversaService {

    private final ConversaRepository conversaRepository;

    public List<Map<String, Object>> listarMensagens(Integer idCliente, Integer idProfissional) {

        return conversaRepository.listarMensagens(idCliente, idProfissional);
    }

}
