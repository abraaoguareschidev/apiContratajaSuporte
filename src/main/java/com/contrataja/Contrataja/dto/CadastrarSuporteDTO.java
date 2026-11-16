package com.contrataja.Contrataja.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CadastrarSuporteDTO {
    private String nome;
    private String cpf;
    private String email;
    private String telefone;
    private String senha;
    private LocalDate dataNasc;
    private String rua;
    private String numeroCasa;
    private String complemento;
    private String bairro;
    private String cidade;
    private String estado;
    private String cep;
}
