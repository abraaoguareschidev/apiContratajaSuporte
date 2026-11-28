package com.contrataja.Contrataja.dto;

import java.time.LocalDate;

public record SuportePerfilDTO(Integer idUsuario,String nome,String email,String telefone,String cpf,LocalDate dataNasc,String cep,String estado,String cidade,String bairro,String rua,String numeroCasa,String complemento,String fotoPerfil) {
}
