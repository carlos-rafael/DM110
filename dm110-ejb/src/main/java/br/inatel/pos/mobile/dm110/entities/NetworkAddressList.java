package br.inatel.pos.mobile.dm110.entities;

import java.io.Serializable;
import java.util.List;

//Classe NetworkAddressList implementa a Interface Serializable
public class NetworkAddressList implements Serializable{

	private static final long serialVersionUID = -510903936352309344L;
	
	//O atributo networkAddresses é uma lista de Strings
	private List<String> networkAddresses;
	
	//getters e setters da classe
	public List<String> getNetworkAddresses() {
		return networkAddresses;
	}

	public void setNetworkAddresses(List<String> networkAddresses) {
		this.networkAddresses = networkAddresses;
	}

	
}
