package br.inatel.pos.mobile.dm110.dao;


import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import br.inatel.pos.mobile.dm110.entities.NetworkAddress;
import br.inatel.pos.mobile.dm110.to.NetworkAddressTO;

//classe NetworkAddressDAO é responsável por realizar as operações com o banco
@Stateless
public class NetworkAddressDAO {
	
	//informa o nome do banco utilizado na aplicação
	@PersistenceContext(unitName = "inventory")
	private EntityManager em;

	//método que realiza a inserção de um objeto NetworkAddress no banco
	public void insert(NetworkAddressTO addressTO) {
		
		//Criação de um objeto do tipo NetworkAddress, o qual receberá os atributos do objeto NetworkAddressTO
		NetworkAddress address = new NetworkAddress();
		address.setIp(addressTO.getIp());
		address.setStatus(addressTO.getStatus());
		
		//merge foi utilizado no lugar de persist pois se o objeto já existir no banco será feito um update do mesmo, caso contrário será feito um insert na tabela address
		em.merge(address);
	}
	
	//método que checa no banco o status de um endereço ip
	public String check(String ip) {

		String res="";
		//confirma se o endereço está realmente presente no banco, caso não esteja retorna a mensagem: "IP não cadastrado no banco de dados"
		if ((em.createQuery("from NetworkAddress na where na.ip=:ip", NetworkAddress.class).setParameter("ip", ip).getResultList()).size() == 0){
			res = "IP não cadastrado no banco de dados";
		}else{
			//caso o endereço esteja presente no banco, checa seu status. Como o campo no banco é booleano, retornando true ou false, a variável res retornada é editada para "IP Ativo" ou "IP Inativo", conforme o escopo da aplicação 
			if (em.createQuery("from NetworkAddress na where na.ip=:ip", NetworkAddress.class).setParameter("ip", ip).getResultList().get(0).getStatus()){
				res = "IP Ativo"; 						
			}else{
				res = "IP Inativo";
			}				
		}
		//a variável res é retornada para quem invocou este método 
		return res;
	}
}
