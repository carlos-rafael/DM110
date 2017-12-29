package br.inatel.pos.mobile.dm110.interfaces;


//Interface Address 
public interface Address {
	
	//m�todo startCalculation recebe dois par�metros do tipo String: ip e mask
	void startCalculation(String ip, String mask);

	//m�todo getStatus recebe um par�metro do tipo String: ip, e retorna uma string como resposta, contendo o status do endere�o inserido como par�metro de entrada
	String getStatus(String ip);
}
