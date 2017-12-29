package br.inatel.pos.mobile.dm110.mdb;
import java.util.List;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import br.inatel.pos.mobile.dm110.dao.NetworkAddressDAO;
import br.inatel.pos.mobile.dm110.entities.NetworkAddressList;
import br.inatel.pos.mobile.dm110.to.NetworkAddressTO;
import br.inatel.pos.mobile.dm110.utils.PingExec;

@MessageDriven(activationConfig = {
		@ActivationConfigProperty(propertyName = "destinationType",
								  propertyValue = "javax.jms.Queue"),
		@ActivationConfigProperty(propertyName = "destination",
								  propertyValue = "java:/jms/queue/IpAddressQueue"),
		@ActivationConfigProperty(propertyName = "maxSession",
								  propertyValue = "10")
})
//Classe AddressMDB implementa a Interface MessageListener
public class AddressMDB implements MessageListener{
	
	@EJB
	private NetworkAddressDAO networkAddressDAO;
	
	//m�todo onMessage definido na Interface MessageListener � implementado aqui
	@Override
	public void onMessage(Message message) {
		try 
		{
			if(message instanceof ObjectMessage)
			{
				
				ObjectMessage objMessage = (ObjectMessage) message;
				Object object = objMessage.getObject();
				
				//checa se o objeto � uma int�ncia de NetworkAddressList, a qual estamos enviando em AddressBean
				if(object instanceof NetworkAddressList)
				{
					
					//boolean que receber� o valor de pingResult
					boolean status;
					
					//Instanciei um novo objeto address da classe NetworkAddress
					NetworkAddressTO address = new NetworkAddressTO();
					
					//Instanciei ipListObj do tipo NetworkAddressList recebendo o casting de object para NetworkAddressList
					NetworkAddressList ipListObj = (NetworkAddressList) object;
					
					//Cria��o de uma lista do tipo String
					List<String> ipListStr;
					//ipList recebe o conte�do de ipListObj
					ipListStr = ipListObj.getNetworkAddresses();
					
					/*para cada endere�o presente na lista de strings,
					 *o objeto address receber� este endere�o no atributo ip, 
					 *enquanto o atributo status receber� o retorno do m�todo execPing para este endere�o.
					 *Ap�s estas opera��es, o m�todo insert de networkAddressDAO � invocado 
					 */
					for (String networkAddress : ipListStr) {
						//address.ip recebe o ip corrrente
						address.setIp(networkAddress);
						
						//status recebe o retorno do m�todo execPing (true ou false)
						status = PingExec.execPing(networkAddress);
						
						//address.status recebe status					
						address.setStatus(status);
						
						//o m�todo insert de networkAddressDAO � invocado, recebendo como par�metro um objeto do tipo NetworkAddress
						networkAddressDAO.insert(address);
					}
				
				} else {
					System.out.println("O objeto n�o � uma inst�ncia de NetworkAddressList");
				}
			
			} else {
				System.out.println("Message n�o � uma inst�ncia de ObjectMessage");
			}
		} catch (JMSException e) {
			
			e.printStackTrace();
		}
		
	}
	
	
}
