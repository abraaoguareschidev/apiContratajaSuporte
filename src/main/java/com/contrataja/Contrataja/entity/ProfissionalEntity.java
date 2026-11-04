package com.contrataja.Contrataja.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@PrimaryKeyJoinColumn(name = "id_usuario")
public class ProfissionalEntity extends ClienteEntity{

    @Column(name = "nota_media",precision =  3,scale = 2)
    private BigDecimal notaMedia;

    @Column(length = 30)
    private String disponibiliadade;

    @Column(name = "valor_hora", precision =  3,scale = 2)
    private BigDecimal valorHora;

    @Column(columnDefinition = "text")
    private String experiencia;

    @Column(name = "descricao_profissional", columnDefinition = "text")
    private String descricaoProfissional;

}

