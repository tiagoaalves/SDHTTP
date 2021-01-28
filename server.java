import java.net.*;
import java.io.*;

public class server {
	static int DEFAULT_PORT=8081;
	
	public static void main(String[] args) {
		int port=DEFAULT_PORT;
        ServerSocket servidor = null;
        
	
		try	{ 
			servidor = new ServerSocket(port);
		} catch (Exception e) { 
			System.err.println("erro ao criar socket servidor...");
			e.printStackTrace();
			System.exit(-1);
		}
		
		data d = new data();
			
		System.out.println("Servidor a' espera de ligacoes no porto " + port);
		
		while(true) {
			try {
				Socket ligacao = servidor.accept();
								
				serverHandler t = new serverHandler(ligacao, d);
				t.start();
				
			} catch (IOException e) {
				System.out.println("Erro na execucao do servidor: "+e);
				System.exit(1);
			}
		}
	}
}