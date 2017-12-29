package br.inatel.pos.mobile.dm110.utils;

/*Classe com métodos utilizados para gerar uma lista de ips
 *  Pelos métodos serem públicos e estáticos, podemos invocá-los em outros pacotes sem precisarmos instanciar a classe NetworkIpGen.
*/
public class NetworkIpGen {

	//Método que recebe dois parâmetros: networkIp (String) e cidr (int), retornando um array de Strings
	public static String[] generateIps(String networkIp, int cidr) {
		int rangeSize = 0;
		for (int i = 0; i < 32 - cidr; i++) {
			rangeSize |= 1 << i;
		}
		long networkAddress = fromIp(networkIp);
		String[] ips = new String[rangeSize - 1];
		for (int i = 1; i < rangeSize; i++) {
			ips[i - 1] = toIp(networkAddress + i);
		}
		
		return ips;
	}

	private static long fromIp(String ip) {
		String[] octs = ip.split("\\.");
		long octs1 = Long.parseLong(octs[0]);
		long octs2 = Long.parseLong(octs[1]);
		long octs3 = Long.parseLong(octs[2]);
		long octs4 = Long.parseLong(octs[3]);
		return (octs1 << 24) | (octs2 << 16) | (octs3 << 8) | octs4;
	}

	private static String toIp(long value) {
		return String.format("%s.%s.%s.%s", value >> 24, (value >> 16) & 255, (value >> 8) & 255, value & 255);
	}
	
}

