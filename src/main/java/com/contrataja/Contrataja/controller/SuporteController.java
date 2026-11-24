package com.contrataja.Contrataja.controller;


import com.contrataja.Contrataja.dto.*;
import com.contrataja.Contrataja.entity.SuporteEntity;
import com.contrataja.Contrataja.service.UsuarioService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/usuarios")
@AllArgsConstructor
@CrossOrigin("*")
public class SuporteController {

    private final UsuarioService usuarioService;

    @PostMapping("/suporte")
    public ResponseEntity<?> cadastrarSuporte(@RequestBody CadastrarSuporteDTO dto) {

        try {
            SuporteEntity suporte = usuarioService.cadastrarSuporte(dto);

            SuporteResponseDTO resposta = new SuporteResponseDTO();
            resposta.setIdUsuario(suporte.getIdUsuario());
            resposta.setNome(suporte.getNome());

            return ResponseEntity.status(HttpStatus.CREATED).body(resposta);

        } catch (ResponseStatusException e) {

            ErrorResponse erro = new ErrorResponse(e.getReason());

            return ResponseEntity.status(e.getStatusCode()).body(erro);
        }
    }


    @GetMapping("/suporte/{idUsuario}")
    public ResponseEntity<SuportePerfilDTO> buscarPerfil(
            @PathVariable Integer idUsuario) {

        SuporteEntity suporte = usuarioService.buscarSuportePorId(idUsuario);

        SuportePerfilDTO perfil = new SuportePerfilDTO(
                suporte.getIdUsuario(),
                suporte.getNome(),
                suporte.getEmail(),
                suporte.getTelefone(),
                suporte.getCpf(),
                suporte.getDataNasc(),
                suporte.getCep(),
                suporte.getEstado(),
                suporte.getCidade(),
                suporte.getBairro(),
                suporte.getRua(),
                suporte.getNumeroCasa(),
                suporte.getComplemento(),
                suporte.getFotoPerfil()
        );

        return ResponseEntity.ok(perfil);
    }


    @PutMapping("/suporte/{idUsuario}")
    public ResponseEntity<Void> atualizarPerfil(
            @PathVariable Integer idUsuario,
            @RequestBody AtualizarPerfilSuporteDTO dto) {

        usuarioService.atualizarPerfilSuporte(idUsuario, dto);

        return ResponseEntity.noContent().build();
    }


    @PutMapping("/suporte/{idUsuario}/senha")
    public ResponseEntity<Void> alterarSenha(
            @PathVariable Integer idUsuario,
            @RequestBody AlterarSenhaDTO dto) {

        usuarioService.alterarSenha(idUsuario, dto);

        return ResponseEntity.noContent().build();
    }



}
