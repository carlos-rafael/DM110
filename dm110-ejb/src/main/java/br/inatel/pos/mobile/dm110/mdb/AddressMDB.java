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
	
	//método onMessage definido na Interface MessageListener é implementado aqui
	@Override
	public void onMessage(Message message) {
		try 
		{
			if(message instanceof ObjectMessage)
			{
				
				ObjectMessage objMessage = (ObjectMessage) message;
				Object object = objMessage.getObject();
				
				//checa se o objeto é uma intância de NetworkAddressList, a qual estamos enviando em AddressBean
				if(object instanceof NetworkAddressList)
				{
					
					//boolean que receberá o valor de pingResult
					boolean status;
					
					//Instanciei um novo objeto address da classe NetworkAddress
					NetworkAddressTO address = new NetworkAddressTO();
					
					//Instanciei ipListObj do tipo NetworkAddressList recebendo o casting de object para NetworkAddressList
					NetworkAddressList ipListObj = (NetworkAddressList) object;
					
					//Criação de uma lista do tipo String
					List<String> ipListStr;
					//ipList recebe o conteúdo de ipListObj
					ipListStr = ipListObj.getNetworkAddresses();
					
					/*para cada endereço presente na lista de strings,
					 *o objeto address receberá este endereço no atributo ip, 
					 *enquanto o atributo status receberá o retorno do método execPing para este endereço.
					 *Após estas operações, o método insert de networkAddressDAO é invocado 
					 */
					for (String networkAddress : ipListStr) {
						//address.ip recebe o ip corrrente
						address.setIp(networkAddress);
						
						//status recebe o retorno do método execPing (true ou false)
						status = PingExec.execPing(networkAddress);
						
						//address.status recebe status					
						address.setStatus(status);
						
						//o método insert de networkAddressDAO é invocado, recebendo como parâmetro um objeto do tipo NetworkAddress
						networkAddressDAO.insert(address);
					}
				
				} else {
					System.out.println("O objeto não é uma instância de NetworkAddressList");
				}
			
			} else {
				System.out.println("Message não é uma instância de ObjectMessage");
			}
		} catch (JMSException e) {
			
			e.printStackTrace();
		}
		
	}
	
	
}
