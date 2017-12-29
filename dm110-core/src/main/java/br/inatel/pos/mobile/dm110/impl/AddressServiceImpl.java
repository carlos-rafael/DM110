package br.inatel.pos.mobile.dm110.impl;

import java.util.regex.Pattern;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;

import br.inatel.pos.mobile.dm110.api.AddressService;
import br.inatel.pos.mobile.dm110.interfaces.AddressRemote;

//Classe que implementa a interface AddressService
@RequestScoped
public class AddressServiceImpl implements AddressService {

	//Através do objeto addressBean, da classe AddressRemote, os métodos startCalculation e getStatus são acessados.
	@EJB(lookup = "java:app/dm110-ejb-1.0.0-SNAPSHOT/AddressBean!br.inatel.pos.mobile.dm110.interfaces.AddressRemote")
	private AddressRemote addressBean;

    //Neste momento resolvi fazer a validação da máscara inserida pelo usuário
	@Override
	public void startCalculation(String ip, String mask) {
		
		//conversão do atributo mask para int na variável m, com o intuito de realizar sua validação
		int m = Integer.parseInt(mask);
		//validação da máscara e endereço inseridos pelo usuário
		if((m<=30 && m >=8) && validateIP(ip)){
			addressBean.startCalculation(ip, mask);
		}
		else{
			System.out.println("Por favor insira um endereço IP e máscara válidos");
		}
	}

	/*Neste momento resolvi fazer a validação do endereço, antes de iniciar a invocação do método.
	 * A validação é realizada através de um método booleano que checa o endereço através de uma expressão regular 
	 */
	@Override
	public String getStatus(String ip) { 
		if(validateIP(ip)){
			return addressBean.getStatus(ip);
		}else{
			return "Por favor, entre com um endereço IP válido";
		}
	}
	
	//método que compara o endereço IP inserido pelo usuário com uma expressão regular
	public static boolean validateIP(String ipStr) {
	    String regex = "\\b((25[0–5]|2[0–4]\\d|[01]?\\d\\d?)(\\.)){3}(25[0–5]|2[0–4]\\d|[01]?\\d\\d?)\\b";
	    return Pattern.matches(regex, ipStr);
	}
}
