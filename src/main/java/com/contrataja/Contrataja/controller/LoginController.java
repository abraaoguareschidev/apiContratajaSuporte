package com.contrataja.Contrataja.controller;

import com.contrataja.Contrataja.dto.LoginDTO;
import com.contrataja.Contrataja.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin("*")
public class LoginController {

    private final UsuarioService usuarioService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO){
        Integer idSuporte = usuarioService.logar(loginDTO);
        return ResponseEntity.ok(Map.of("idUsuario", idSuporte));
    }

    @PostMapping("/reenviar-verificacao")
    public ResponseEntity<String> reenviarVerificacao(@RequestParam String email) {

        usuarioService.reenviarEmailVerificacao(email);

        return ResponseEntity.ok("Se a conta estiver pendente e o reenvio estiver disponível, um novo e-mail será enviado.");
    }

    @GetMapping("/verificar-email")
    public ResponseEntity<String> verificarEmail(@RequestParam String token) {

        usuarioService.verificarEmail(token);

        return ResponseEntity.ok("E-mail verificado com sucesso!");
    }

}
