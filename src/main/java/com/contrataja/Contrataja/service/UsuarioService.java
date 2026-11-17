package com.contrataja.Contrataja.service;

import com.contrataja.Contrataja.dto.CadastrarSuporteDTO;
import com.contrataja.Contrataja.dto.LoginDTO;
import com.contrataja.Contrataja.entity.SuporteEntity;
import com.contrataja.Contrataja.entity.UsuarioEntity;
import com.contrataja.Contrataja.repository.UsuarioRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public SuporteEntity cadastrarSuporte(CadastrarSuporteDTO dto) {
        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "E-mail já cadastrado");
        }
        if (usuarioRepository.existsByCpf(dto.getCpf())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "CPF já cadastrado");
        }
        SuporteEntity suporte = new SuporteEntity();
        suporte.setNome(dto.getNome());
        suporte.setCpf(dto.getCpf());
        suporte.setEmail(dto.getEmail());
        suporte.setTelefone(dto.getTelefone());
        suporte.setDataNasc(dto.getDataNasc());
        suporte.setRua(dto.getRua());
        suporte.setNumeroCasa(dto.getNumeroCasa());
        suporte.setComplemento(dto.getComplemento());
        suporte.setBairro(dto.getBairro());
        suporte.setCidade(dto.getCidade());
        suporte.setEstado(dto.getEstado());
        suporte.setCep(dto.getCep());
        suporte.setSenha(passwordEncoder.encode(dto.getSenha()));
        suporte.setTipoUsuario("SUPORTE");
        suporte.setStatus("ATIVO");
        return usuarioRepository.save(suporte);

    }

    public void logar(LoginDTO dto) {
        //procurar usuario pelo email do dto
        Optional<UsuarioEntity> usuario = usuarioRepository.findByEmail(dto.getLogin());
        if (usuario.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "email ou senha inválidos.");
        }

        if (!passwordEncoder.matches(dto.getSenha(), usuario.get().getSenha())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "email ou senha inválidos.");
        }

    }

}
