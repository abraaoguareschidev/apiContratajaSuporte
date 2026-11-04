package com.contrataja.Contrataja.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@Table(name = "chamado")
@Entity
public class ChamadoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_chamado")
    private Integer idChamado;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "id_usuario",nullable = false)
    private UsuarioEntity usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_suporte")
    private SuporteEntity suporte;

    @Column(nullable = false, length = 150)
    private String assunto;

    @Column(columnDefinition = "text")
    private String descricao;

    @Column(name = "data_abertura", insertable = false, updatable = false)
    private LocalDateTime dataAbertura;

    @Column(name = "data_fechamento")
    private LocalDateTime dataFechamento;

    @Column(nullable = false, length = 20)
    private String status;
}
