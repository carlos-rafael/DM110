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
	
	//Método startCalculation, recebe como parâmetros ip (String) e mask (String)
	@Override
	public void startCalculation(String ip, String mask) {

		//conversão de mask para int já que o método generateIps espera como segundo parâmetro de entrada um int 
		int maskInt = Integer.parseInt(mask);
		
		//o método generateIps é invocado, e seu retorno é armazenado em um array de Strings addresses
		String[] addresses = NetworkIpGen.generateIps(ip, maskInt);

		//conversão de tipos para List<Strings>
		List<String> addressesL = Arrays.asList(addresses);
		
		//criação de uma lista do tipo NetworkAddressList
		NetworkAddressList addressList = new NetworkAddressList();
		
		try (Connection connection = connectionFactory.createConnection();
				//criação da sessão e do producer com base na queue
				Session session = connection.createSession();
				MessageProducer producer = session.createProducer(queue);) {
				
				/*Atendendo o requisito: "Cria mensagens JMS sendo que cada mensagem deverá conter uma lista de no máximo 10 endereços IP."
				 * foi criado um for que divide a lista em grupos de 10 elementos e cada um desses grupos é enviado separadamente na mensagem JMS				 
				 */			
			    for (int start = 0; start < addressesL.size(); start += 10) {
				   
				   int end = Math.min(start + 10, addressesL.size());
				   //Cria uma sublista com 10 elementos
			       List<String> list1 = new ArrayList<String>(addressesL.subList(start, end));
			       //addressList recebe o conteúdo da lista list1
			       addressList.setNetworkAddresses(list1);
			       
			       /*Para confirmar que cada sublista possui 10 elementos (a última pode possuir menos elementos dependendo da entrada realizada pelo usuário)
			       System.out.println("BEGIN");
			       System.out.println(addressList.getNetworkAddresses());
			       System.out.println("END");
			       */
			       
			       //Um objeto do tipo ObjectMessage é criado com o conetúdo de addressList
			       ObjectMessage objMessage = session.createObjectMessage(addressList);
			       producer.send(objMessage);
					
			    }
			
		} catch (JMSException e) {
			throw new RuntimeException(e);
		}
		
		
	}
	/*método que checa o status de um endereço, recebendo como parâmetro um endereço do tipo String
	 * e retornando uma string como resposta.
	 */
	@Override
	public String getStatus(String ip) {
		return dao.check(ip);

	}

	
}
