package br.inatel.pos.mobile.dm110.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
//Classe NetworkAddress implementa a Interface Serializable
@Entity
//O nome da tabela no banco é address
@Table(name = "address")
public class NetworkAddress implements Serializable{

	private static final long serialVersionUID = 5563565338667174482L;

	//O atributo ip é a chave primária na tabela address, ainda o atributo ip reflete a coluna ip na tabela address
	@Id
	@Column(name = "ip")
	private String ip;
	
	//O atributo status reflete a coluna status na tabela address
	@Column(name = "status")
	private boolean status;
	
	//getters e setters da classe
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public boolean getStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}	
	
}
