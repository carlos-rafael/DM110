package br.inatel.pos.mobile.dm110.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

//Interface utilizada na aplica��o, acessada atrav�s do caminho /poller
@Path("/poller")
public interface AddressService {

	/*
	 * M�todo GET que recebe como par�metro um endere�o IP e uma m�scara, ambos recebidos como String Params
	 * Este m�todo n�o possui retorno, por�m atrav�s do System.out.printLn() � poss�vel validar os passos sendo executados nele.
	 * */
	@GET
	@Path("/start/{ip}/{mask}")
	void startCalculation(@PathParam("ip") String ip, @PathParam("mask") String mask);

	/*
	 * M�todo GET que recebe como par�metro um endere�o IP e retorna uma string, com o estado do endere�o IP.
	 * */
	@GET
	@Path("/status/{ip}")
	@Produces(MediaType.APPLICATION_JSON)
	String getStatus(@PathParam("ip") String ip);

}
