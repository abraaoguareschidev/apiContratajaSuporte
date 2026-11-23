package com.contrataja.Contrataja.dto;

import java.time.LocalDate;

public record AtualizarPerfilSuporteDTO(String nome,
                                        String email,
                                        String telefone,
                                        LocalDate dataNasc,
                                        String cep,
                                        String estado,
                                        String cidade,
                                        String bairro,
                                        String rua,
                                        String numeroCasa,
                                        String complemento) {
}
