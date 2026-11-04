package com.contrataja.Contrataja.controller;

import com.contrataja.Contrataja.dto.AbrirChamadoDTO;
import com.contrataja.Contrataja.entity.ChamadoEntity;
import com.contrataja.Contrataja.service.ChamadoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chamados")
public class ChamadoController {

    private final ChamadoService chamadoService;

    public ChamadoController(ChamadoService chamadoService) {
        this.chamadoService = chamadoService;
    }

    @GetMapping
    public List<ChamadoEntity> listarChamados(){
        return chamadoService.listarChamados();
    }

    @PostMapping
    public ChamadoEntity cadastrarChamado(@RequestBody AbrirChamadoDTO dto) {
        return chamadoService.cadastrarChamado(dto);
    }
}
