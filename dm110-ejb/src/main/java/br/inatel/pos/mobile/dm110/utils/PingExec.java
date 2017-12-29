package br.inatel.pos.mobile.dm110.utils;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

/*Classe com métodos utilizados para realizar o ping em um endereço IP específico
 *  Pelos métodos serem públicos e estáticos, podemos invocá-los em outros pacotes sem precisarmos instanciar a classe PingExec.
*/
public class PingExec {
	
	public static boolean execPing(String address) {
		try {
			Runtime runtime = Runtime.getRuntime();
			Process process = runtime.exec("ping -n 1 " + address);
			InputStream is = process.getInputStream();
			InputStream es = process.getErrorStream();
			String input = processStream(is);
			String error = processStream(es);
			int code = process.waitFor();
			if (code != 0) {
				return false;
			}
			if (error.length() > 0) {
				return false;
			}
			if (input.contains("Host de destino inacess")) {
				return false;
			}
			return true;
		} catch (IOException | InterruptedException e) {
			return false;
		}
	}

	public static String processStream(InputStream is) {
		StringBuilder input = new StringBuilder();
		try (Scanner scanner = new Scanner(is)) {
			while (scanner.hasNextLine()) {
				input.append(scanner.nextLine()).append("\n");
			}
		}
		return input.toString();
	}
}
