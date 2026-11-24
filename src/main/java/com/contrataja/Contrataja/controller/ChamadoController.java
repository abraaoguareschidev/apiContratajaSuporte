package com.contrataja.Contrataja.controller;

import com.contrataja.Contrataja.dto.AbrirChamadoDTO;
import com.contrataja.Contrataja.entity.ChamadoEntity;
import com.contrataja.Contrataja.service.ChamadoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

    @PutMapping("/{idChamado}/encerrar")
    public ChamadoEntity encerrarChamado(
            @PathVariable Integer idChamado,
            @RequestBody Map<String, String> dados) {

        String solucao = dados.get("solucao");

        return chamadoService.encerrarChamado(idChamado, solucao);
    }

    @PutMapping("/{idChamado}/assumir")
    public ChamadoEntity assumirChamado(@PathVariable Integer idChamado,@RequestParam Integer idSuporte) {

        return chamadoService.assumirChamado(idChamado, idSuporte);
    }

    @GetMapping("/suporte/{idSuporte}")
    public List<ChamadoEntity> listarMeusChamados(
            @PathVariable Integer idSuporte) {

        return chamadoService.listarMeusChamados(idSuporte);
    }
}
