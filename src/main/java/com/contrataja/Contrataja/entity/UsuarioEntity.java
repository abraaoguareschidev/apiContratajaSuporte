package com.contrataja.Contrataja.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "usuario")
@Inheritance(strategy = InheritanceType.JOINED)
public class UsuarioEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Integer idUsuario;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(name = "tipo_usuario", nullable = false, length = 20)
    private String tipoUsuario;

    @Column(nullable = false,unique = true ,length = 100)
    private String email;

    @Column(nullable = false,unique = true,length = 14)
    private String cpf;

    @Column(nullable = false,length = 15)
    private String telefone;

    @Column(name  = "foto_perfil" , length = 255)
    private String fotoPerfil;

    @Column(nullable = false,length = 255)
    private String senha;

    @Column(length = 100)
    private String rua;

    @Column(name = "numero_casa", length = 10)
    private String numeroCasa;

    @Column(length = 100)
    private String complemento;

    @Column(length = 80)
    private String bairro;

    @Column(length = 80)
    private String cidade;

    @Column(length = 2)
    private String estado;

    @Column(length = 9)
    private String cep;

    @Column(name = "data_nasc")
    private LocalDate dataNasc;

    @Column(nullable = false, length = 20)
    private String status = "ativo";

    @Column(name = "data_cadastro", insertable = false, updatable = false)
    private LocalDateTime dataCadastro;


}
