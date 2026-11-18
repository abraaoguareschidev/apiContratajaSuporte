package com.contrataja.Contrataja.service;

import com.contrataja.Contrataja.dto.CadastrarSuporteDTO;
import com.contrataja.Contrataja.dto.LoginDTO;
import com.contrataja.Contrataja.entity.SuporteEntity;
import com.contrataja.Contrataja.entity.UsuarioEntity;
import com.contrataja.Contrataja.entity.VerificacaoEmailEntity;
import com.contrataja.Contrataja.repository.UsuarioRepository;
import com.contrataja.Contrataja.repository.VerificacaoEmailRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificacaoEmailRepository verificacaoEmailRepository;

    @Autowired
    private EmailService emailService;

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
        suporte.setStatus("PENDENTE");
        SuporteEntity suporteSalvo = usuarioRepository.save(suporte);
        String token = UUID.randomUUID().toString();
        VerificacaoEmailEntity verificacao = new VerificacaoEmailEntity();

        verificacao.setUsuario(suporteSalvo);
        verificacao.setToken(token);
        verificacao.setExpiracao(LocalDateTime.now().plusMinutes(10));

        verificacaoEmailRepository.save(verificacao);
        String link = "http://localhost:8080/auth/verificar-email?token=" + token;

        emailService.sendEmail(suporteSalvo.getEmail(),"Confirme seu e-mail - ContrataJá","Olá, " + suporteSalvo.getNome() +"!\n\nClique no link para confirmar seu e-mail:\n" + link);
        return suporteSalvo;
        // Enviar um email pra verificar a conta

    }

    public void verificarEmail(String token) {

        Optional<VerificacaoEmailEntity> verificacao =
                verificacaoEmailRepository.findByToken(token);

        if (verificacao.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Link de verificação inválido."
            );
        }

        if (!verificacao.get().getExpiracao().isAfter(LocalDateTime.now())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "O link de verificação expirou."
            );
        }
        UsuarioEntity usuario = verificacao.get().getUsuario();

        usuario.setStatus("ATIVO");

        usuarioRepository.save(usuario);
        verificacaoEmailRepository.delete(verificacao.get());

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

        if (!"SUPORTE".equals(usuario.get().getTipoUsuario())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "email ou senha inválidos.");
        }

        if (!"ATIVO".equals(usuario.get().getStatus())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Confirme seu e-mail antes de fazer login.");
        }

    }

}
