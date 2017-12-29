package br.inatel.pos.mobile.dm110.dao;


import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import br.inatel.pos.mobile.dm110.entities.NetworkAddress;
import br.inatel.pos.mobile.dm110.to.NetworkAddressTO;

//classe NetworkAddressDAO � respons�vel por realizar as opera��es com o banco
@Stateless
public class NetworkAddressDAO {
	
	//informa o nome do banco utilizado na aplica��o
	@PersistenceContext(unitName = "inventory")
	private EntityManager em;

	//m�todo que realiza a inser��o de um objeto NetworkAddress no banco
	public void insert(NetworkAddressTO addressTO) {
		
		//Cria��o de um objeto do tipo NetworkAddress, o qual receber� os atributos do objeto NetworkAddressTO
		NetworkAddress address = new NetworkAddress();
		address.setIp(addressTO.getIp());
		address.setStatus(addressTO.getStatus());
		
		//merge foi utilizado no lugar de persist pois se o objeto j� existir no banco ser� feito um update do mesmo, caso contr�rio ser� feito um insert na tabela address
		em.merge(address);
	}
	
	//m�todo que checa no banco o status de um endere�o ip
	public String check(String ip) {

		String res="";
		//confirma se o endere�o est� realmente presente no banco, caso n�o esteja retorna a mensagem: "IP n�o cadastrado no banco de dados"
		if ((em.createQuery("from NetworkAddress na where na.ip=:ip", NetworkAddress.class).setParameter("ip", ip).getResultList()).size() == 0){
			res = "IP n�o cadastrado no banco de dados";
		}else{
			//caso o endere�o esteja presente no banco, checa seu status. Como o campo no banco � booleano, retornando true ou false, a vari�vel res retornada � editada para "IP Ativo" ou "IP Inativo", conforme o escopo da aplica��o 
			if (em.createQuery("from NetworkAddress na where na.ip=:ip", NetworkAddress.class).setParameter("ip", ip).getResultList().get(0).getStatus()){
				res = "IP Ativo"; 						
			}else{
				res = "IP Inativo";
			}				
		}
		//a vari�vel res � retornada para quem invocou este m�todo 
		return res;
	}
}
