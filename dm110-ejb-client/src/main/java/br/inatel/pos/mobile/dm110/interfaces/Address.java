package br.inatel.pos.mobile.dm110.interfaces;


//Interface Address 
public interface Address {
	
	//método startCalculation recebe dois parâmetros do tipo String: ip e mask
	void startCalculation(String ip, String mask);

	//método getStatus recebe um parâmetro do tipo String: ip, e retorna uma string como resposta, contendo o status do endereço inserido como parâmetro de entrada
	String getStatus(String ip);
}
