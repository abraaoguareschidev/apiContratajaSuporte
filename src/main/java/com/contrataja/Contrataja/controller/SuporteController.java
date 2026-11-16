package com.contrataja.Contrataja.controller;


import com.contrataja.Contrataja.dto.CadastrarSuporteDTO;
import com.contrataja.Contrataja.dto.ErrorResponse;
import com.contrataja.Contrataja.dto.SuporteResponseDTO;
import com.contrataja.Contrataja.entity.SuporteEntity;
import com.contrataja.Contrataja.service.UsuarioService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/usuarios")
@AllArgsConstructor
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

}
