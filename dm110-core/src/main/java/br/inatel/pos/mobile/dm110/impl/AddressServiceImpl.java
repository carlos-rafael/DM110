package br.inatel.pos.mobile.dm110.impl;

import java.util.regex.Pattern;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;

import br.inatel.pos.mobile.dm110.api.AddressService;
import br.inatel.pos.mobile.dm110.interfaces.AddressRemote;

//Classe que implementa a interface AddressService
@RequestScoped
public class AddressServiceImpl implements AddressService {

	//Atrav�s do objeto addressBean, da classe AddressRemote, os m�todos startCalculation e getStatus s�o acessados.
	@EJB(lookup = "java:app/dm110-ejb-1.0.0-SNAPSHOT/AddressBean!br.inatel.pos.mobile.dm110.interfaces.AddressRemote")
	private AddressRemote addressBean;

    //Neste momento resolvi fazer a valida��o da m�scara inserida pelo usu�rio
	@Override
	public void startCalculation(String ip, String mask) {
		
		//convers�o do atributo mask para int na vari�vel m, com o intuito de realizar sua valida��o
		int m = Integer.parseInt(mask);
		//valida��o da m�scara e endere�o inseridos pelo usu�rio
		if((m<=30 && m >=8) && validateIP(ip)){
			addressBean.startCalculation(ip, mask);
		}
		else{
			System.out.println("Por favor insira um endere�o IP e m�scara v�lidos");
		}
	}

	/*Neste momento resolvi fazer a valida��o do endere�o, antes de iniciar a invoca��o do m�todo.
	 * A valida��o � realizada atrav�s de um m�todo booleano que checa o endere�o atrav�s de uma express�o regular 
	 */
	@Override
	public String getStatus(String ip) { 
		if(validateIP(ip)){
			return addressBean.getStatus(ip);
		}else{
			return "Por favor, entre com um endere�o IP v�lido";
		}
	}
	
	//m�todo que compara o endere�o IP inserido pelo usu�rio com uma express�o regular
	public static boolean validateIP(String ipStr) {
	    String regex = "\\b((25[0�5]|2[0�4]\\d|[01]?\\d\\d?)(\\.)){3}(25[0�5]|2[0�4]\\d|[01]?\\d\\d?)\\b";
	    return Pattern.matches(regex, ipStr);
	}
}
