package com.contrataja.Contrataja.service;

import com.contrataja.Contrataja.dto.AlterarSenhaDTO;
import com.contrataja.Contrataja.dto.AtualizarPerfilSuporteDTO;
import com.contrataja.Contrataja.dto.CadastrarSuporteDTO;
import com.contrataja.Contrataja.dto.LoginDTO;
import com.contrataja.Contrataja.entity.SuporteEntity;
import com.contrataja.Contrataja.entity.UsuarioEntity;
import com.contrataja.Contrataja.entity.VerificacaoEmailEntity;
import com.contrataja.Contrataja.repository.UsuarioRepository;
import com.contrataja.Contrataja.repository.VerificacaoEmailRepository;
import jakarta.transaction.Transactional;
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
        String email = dto.getEmail();

        String regex = "^(?=.{1,64}@)[A-Za-z0-9_%+-]+(?:\\.[A-Za-z0-9_%+-]+)*@(?:[A-Za-z0-9](?:[A-Za-z0-9-]{0,61}[A-Za-z0-9])?\\.)+[A-Za-z]{2,63}$";

        if (email == null || email.length() > 150 || !email.matches(regex)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "E-mail inválido."
            );
        }
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
        String link = "https://surpass-handled-recollect.ngrok-free.dev/auth/verificar-email?token=" + token;

        emailService.sendEmail(suporteSalvo.getEmail(), "Confirme seu e-mail - ContrataJá", "Olá, " + suporteSalvo.getNome() + "!\n\nClique no link para confirmar seu e-mail:\n" + link);
        return suporteSalvo;
        // Enviar um email pra verificar a conta

    }

    @Transactional
    public void reenviarEmailVerificacao(String email) {

        Optional<UsuarioEntity> usuario = usuarioRepository.findByEmail(email);

        if (usuario.isEmpty()) {
            return;
        }

        UsuarioEntity suporte = usuario.get();

        if (!"SUPORTE".equals(suporte.getTipoUsuario()) || !"PENDENTE".equals(suporte.getStatus())) {
            return;
        }

        if (verificacaoEmailRepository.existsByUsuario_IdUsuarioAndExpiracaoAfter(suporte.getIdUsuario(),LocalDateTime.now().plusMinutes(9))) {
            return;
        }

        verificacaoEmailRepository.deleteByUsuario_IdUsuario(suporte.getIdUsuario());

        String token = UUID.randomUUID().toString();

        VerificacaoEmailEntity verificacao = new VerificacaoEmailEntity();

        verificacao.setUsuario(suporte);
        verificacao.setToken(token);
        verificacao.setExpiracao(LocalDateTime.now().plusMinutes(10));

        verificacaoEmailRepository.save(verificacao);

        String link = "https://surpass-handled-recollect.ngrok-free.dev/auth/verificar-email?token=" + token;

        emailService.sendEmail(suporte.getEmail(),"Confirme seu e-mail - ContrataJá","Olá, " + suporte.getNome()+ "!\n\nClique no link para confirmar seu e-mail:\n" + link);
    }

    public void verificarEmail(String token) {

        Optional<VerificacaoEmailEntity> verificacao =
                verificacaoEmailRepository.findByToken(token);

        if (verificacao.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Link de verificação inválido.");
        }

        if (!verificacao.get().getExpiracao().isAfter(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"O link de verificação expirou.");
        }
        UsuarioEntity usuario = verificacao.get().getUsuario();

        usuario.setStatus("ATIVO");

        usuarioRepository.save(usuario);
        verificacaoEmailRepository.delete(verificacao.get());

    }

    public Integer logar(LoginDTO dto) {
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
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Confirme seu e-mail antes de fazer login.");
        }
        return usuario.get().getIdUsuario();
    }

    public SuporteEntity buscarSuportePorId(int idUsuario) {

        UsuarioEntity usuario = usuarioRepository.findById(idUsuario).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Usuário não encontrado."));

        if (!(usuario instanceof SuporteEntity suporte)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Suporte não encontrado.");
        }

        return suporte;
    }


    public void atualizarPerfilSuporte(
            int idUsuario,
            AtualizarPerfilSuporteDTO dto) {

        SuporteEntity suporte = buscarSuportePorId(idUsuario);

        if (dto.nome() == null || dto.nome().isBlank()
                || dto.email() == null || dto.email().isBlank()
                || dto.telefone() == null || dto.telefone().isBlank()) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Nome, e-mail e telefone são obrigatórios."
            );
        }

        String novoEmail = dto.email().trim();
        String regex = "^(?=.{1,64}@)[A-Za-z0-9_%+-]+(?:\\.[A-Za-z0-9_%+-]+)*@(?:[A-Za-z0-9](?:[A-Za-z0-9-]{0,61}[A-Za-z0-9])?\\.)+[A-Za-z]{2,63}$";

        if (novoEmail.length() > 150 || !novoEmail.matches(regex)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "E-mail inválido.");
        }
        if (!suporte.getEmail().equalsIgnoreCase(novoEmail)
                && usuarioRepository.existsByEmail(novoEmail)) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Este e-mail já está cadastrado."
            );
        }

        suporte.setNome(dto.nome().trim());
        suporte.setEmail(novoEmail);
        suporte.setTelefone(dto.telefone().trim());


        suporte.setCep(dto.cep());
        suporte.setEstado(dto.estado());
        suporte.setCidade(dto.cidade());
        suporte.setBairro(dto.bairro());
        suporte.setRua(dto.rua());
        suporte.setNumeroCasa(dto.numeroCasa());
        suporte.setComplemento(dto.complemento());

        usuarioRepository.save(suporte);
    }


    public void alterarSenha(int idUsuario, AlterarSenhaDTO dto) {

        // Verifica se os campos foram preenchidos
        if (dto == null
                || dto.senhaAtual() == null
                || dto.senhaAtual().isBlank()
                || dto.novaSenha() == null
                || dto.novaSenha().isBlank()) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Preencha a senha atual e a nova senha."
            );
        }

        // Busca o suporte no banco
        SuporteEntity suporte = buscarSuportePorId(idUsuario);

        // Confere se a senha atual está correta
        if (!passwordEncoder.matches(
                dto.senhaAtual(), suporte.getSenha())) {

            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Senha atual incorreta."
            );
        }

        // Valida a nova senha
        if (dto.novaSenha().length() < 8) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "A nova senha deve ter pelo menos 8 caracteres."
            );
        }

        // Criptografa e salva a nova senha
        suporte.setSenha(
                passwordEncoder.encode(dto.novaSenha())
        );

        usuarioRepository.save(suporte);
    }


}
