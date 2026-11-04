package com.contrataja.Contrataja.entity;

import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "cliente")
@PrimaryKeyJoinColumn(name = "id_usuario")
public class ClienteEntity extends UsuarioEntity {

	public ClienteEntity()
	{

    }
}
