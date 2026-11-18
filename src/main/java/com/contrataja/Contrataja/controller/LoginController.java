package com.contrataja.Contrataja.controller;

import com.contrataja.Contrataja.dto.LoginDTO;
import com.contrataja.Contrataja.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class LoginController {

    private final UsuarioService usuarioService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO){
        usuarioService.logar(loginDTO);
        return ResponseEntity.ok("Login realizado com sucesso");
    }

    @GetMapping("/verificar-email")
    public ResponseEntity<String> verificarEmail(@RequestParam String token) {

        usuarioService.verificarEmail(token);

        return ResponseEntity.ok("E-mail verificado com sucesso!");
    }

}
