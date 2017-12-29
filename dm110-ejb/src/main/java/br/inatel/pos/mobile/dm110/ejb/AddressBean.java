package br.inatel.pos.mobile.dm110.ejb;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.Session;

import br.inatel.pos.mobile.dm110.dao.NetworkAddressDAO;

import br.inatel.pos.mobile.dm110.entities.NetworkAddressList;
import br.inatel.pos.mobile.dm110.interfaces.AddressLocal;
import br.inatel.pos.mobile.dm110.interfaces.AddressRemote;
import br.inatel.pos.mobile.dm110.utils.NetworkIpGen;

//Classe AddressBean implementa as Interfaces AddressLocal e AddressRemote
@Stateless
@Local(AddressLocal.class)
@Remote(AddressRemote.class)
public class AddressBean implements AddressLocal, AddressRemote{

	@EJB
	private NetworkAddressDAO dao;

	@Resource(lookup = "java:/ConnectionFactory")
	private ConnectionFactory connectionFactory;
	//nome da fila no Wildfly
	@Resource(lookup = "java:/jms/queue/IpAddressQueue")
	private Queue queue;	
	
	//M�todo startCalculation, recebe como par�metros ip (String) e mask (String)
	@Override
	public void startCalculation(String ip, String mask) {

		//convers�o de mask para int j� que o m�todo generateIps espera como segundo par�metro de entrada um int 
		int maskInt = Integer.parseInt(mask);
		
		//o m�todo generateIps � invocado, e seu retorno � armazenado em um array de Strings addresses
		String[] addresses = NetworkIpGen.generateIps(ip, maskInt);

		//convers�o de tipos para List<Strings>
		List<String> addressesL = Arrays.asList(addresses);
		
		//cria��o de uma lista do tipo NetworkAddressList
		NetworkAddressList addressList = new NetworkAddressList();
		
		try (Connection connection = connectionFactory.createConnection();
				//cria��o da sess�o e do producer com base na queue
				Session session = connection.createSession();
				MessageProducer producer = session.createProducer(queue);) {
				
				/*Atendendo o requisito: "Cria mensagens JMS sendo que cada mensagem dever� conter uma lista de no m�ximo 10 endere�os IP."
				 * foi criado um for que divide a lista em grupos de 10 elementos e cada um desses grupos � enviado separadamente na mensagem JMS				 
				 */			
			    for (int start = 0; start < addressesL.size(); start += 10) {
				   
				   int end = Math.min(start + 10, addressesL.size());
				   //Cria uma sublista com 10 elementos
			       List<String> list1 = new ArrayList<String>(addressesL.subList(start, end));
			       //addressList recebe o conte�do da lista list1
			       addressList.setNetworkAddresses(list1);
			       
			       /*Para confirmar que cada sublista possui 10 elementos (a �ltima pode possuir menos elementos dependendo da entrada realizada pelo usu�rio)
			       System.out.println("BEGIN");
			       System.out.println(addressList.getNetworkAddresses());
			       System.out.println("END");
			       */
			       
			       //Um objeto do tipo ObjectMessage � criado com o conet�do de addressList
			       ObjectMessage objMessage = session.createObjectMessage(addressList);
			       producer.send(objMessage);
					
			    }
			
		} catch (JMSException e) {
			throw new RuntimeException(e);
		}
		
		
	}
	/*m�todo que checa o status de um endere�o, recebendo como par�metro um endere�o do tipo String
	 * e retornando uma string como resposta.
	 */
	@Override
	public String getStatus(String ip) {
		return dao.check(ip);

	}

	
}
