package br.inatel.pos.mobile.dm110.to;

import java.io.Serializable;

//Classe NetworkAddressTO implementa serializable, e reflete o conteúdo da classe NetworkAddress
public class NetworkAddressTO implements Serializable{

	private static final long serialVersionUID = 8442897013712418625L;

	private String ip;
	
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
