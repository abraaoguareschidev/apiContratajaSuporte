package com.contrataja.Contrataja.controller;

import com.contrataja.Contrataja.service.ConversaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/conversas")
@RequiredArgsConstructor
public class ConversaController {

    private final ConversaService conversaService;

    @GetMapping("/mensagens")
    public List<Map<String, Object>> listarMensagens(
            @RequestParam Integer idCliente,
            @RequestParam Integer idProfissional) {

        return conversaService.listarMensagens(
                idCliente, idProfissional
        );
    }
}
