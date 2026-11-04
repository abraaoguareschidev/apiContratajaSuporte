package com.contrataja.Contrataja.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "suporte")
@PrimaryKeyJoinColumn(name = "id_usuario")
public class SuporteEntity extends UsuarioEntity {


}
