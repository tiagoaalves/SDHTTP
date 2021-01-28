import java.net.*;
import java.io.*;
import java.util.*;

public class client{

    /**
     *
     */

    //static final int DEFAULT_PORT = 8081;
    //static final String DEFAULT_HOST = "127.0.0.1";

    public static void main(String[] args) {

        String hostname = "127.0.0.1";
        int port = 8081;
        InetAddress endereco = null;

        try {
            endereco = InetAddress.getByName(hostname);
        } catch (Exception e) {
            System.out.println("endereço desconhecido: " + e);
            System.exit(-1);
        }
 
        try (Socket ligacao = new Socket(hostname, port)) {
 
            InputStream input = ligacao.getInputStream();
            InputStreamReader reader = new InputStreamReader(input);
            //InputStream si = new InputStream(System.in);
            
            BufferedReader bf = new BufferedReader(reader);
            
            PrintWriter pw = new PrintWriter(ligacao.getOutputStream(), true);
            
            InputStreamReader readerSI = new InputStreamReader(System.in);
            BufferedReader br = new BufferedReader(readerSI);
            //PrintWriter.println(br.readLine());
            String message = "anda la";
            pw.println(message);
            // while(true){
            //     message = br.readLine();
            //     pw.println(message);
            //     System.out.println(br.readLine());
            // }
 
            // while ((character = reader.read()) != -1) {
            //     data.append((char) character);
            // }
 
            //System.out.println(data);
 
        ligacao.close();
        } catch (UnknownHostException ex) {
 
            System.out.println("Server not found: " + ex.getMessage());
 
        } catch (IOException ex) {
 
            System.out.println("I/O error: " + ex.getMessage());
        }

        // String host = DEFAULT_HOST;
        // int port = DEFAULT_PORT;
        
        // InetAddress endereco = null;
		// try{
		// 	endereco = InetAddress.getByName(host);
		// }catch(UnknownHostException e){
		// 	System.out.println("Endereco desconhecido:" + e);
		// 	System.exit(-1);
        // }
        
        // try{
		// 	ligacao = new Socket(endereco, port);
		// }catch(Exception e){
		// 	System.out.println("erro ao criar Socket Client");
		// 	e.printStackTrace();
		// 	System.exit(-1);
		// }
        // BufferedReader bf = new BufferedReader(new InputStreamReader(ligacao.getInputStream()));
        // PrintWriter pw = new PrintWriter(ligacao.getOutputStream(), true);
        // BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        // String message = null;
        // while(true){
        //     message = br.readLine();
        //     pw.println(message);
        //     System.out.println(br.readline());
        // }
    }

}