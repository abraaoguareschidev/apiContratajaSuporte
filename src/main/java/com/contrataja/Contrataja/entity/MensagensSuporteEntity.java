package com.contrataja.Contrataja.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jdk.jfr.EventType;
import org.hibernate.annotations.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "mensagens_suporte")
@Getter
@Setter
@NoArgsConstructor
public class MensagensSuporteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_mensagem")
    private Integer idMensagem;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_chamado", nullable = false)
    private ChamadoEntity chamado;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_remetente", nullable = false)
    private UsuarioEntity remetente;

    @Column(nullable = false, columnDefinition = "text")
    private String conteudo;

    @Generated(event = org.hibernate.generator.EventType.INSERT)
    @Column(name = "data_envio",insertable = false, updatable = false)
    private LocalDateTime dataEnvio;

    @Column(name = "status_leitura", nullable = false)
    private Boolean statusLeitura = false;
}
