package br.inatel.pos.mobile.dm110.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

//Interface utilizada na aplicação, acessada através do caminho /poller
@Path("/poller")
public interface AddressService {

	/*
	 * Método GET que recebe como parâmetro um endereço IP e uma máscara, ambos recebidos como String Params
	 * Este método não possui retorno, porém através do System.out.printLn() é possível validar os passos sendo executados nele.
	 * */
	@GET
	@Path("/start/{ip}/{mask}")
	void startCalculation(@PathParam("ip") String ip, @PathParam("mask") String mask);

	/*
	 * Método GET que recebe como parâmetro um endereço IP e retorna uma string, com o estado do endereço IP.
	 * */
	@GET
	@Path("/status/{ip}")
	@Produces(MediaType.APPLICATION_JSON)
	String getStatus(@PathParam("ip") String ip);

}
