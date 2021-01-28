import java.net.*;
import java.io.*;
import java.util.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class serverHandler extends Thread {

    Socket ligacao;
    InputStream inputstream;
    int length;
    String read;
    DataOutputStream outToClient = null;
    data global;
    user user;

    static final String HTML_START = "<html>";

    static final String HTML_END = "</html>";

    public serverHandler(Socket ligacao, data global) {
        this.ligacao = ligacao;
        this.global = global;
    }

    // public String goString(String filepath) {
    //     String content = "";
    //     try {
    //         BufferedReader in = new BufferedReader(new FileReader(filepath));
    //         String str;
    //         while ((str = in.readLine()) != null) {
    //             content += str;
    //         }
    //         in.close();
    //     } catch (IOException e) {
    //     }
    //     return content;
    // }

    //funcao responsavel por enviar todas as responses aos http requests
    public void sendResponse(int statusCode, String responseString, boolean isFile, boolean ishtml) throws Exception {

        outToClient = new DataOutputStream(ligacao.getOutputStream());
        String statusLine = null;
        String serverdetails = "Server: Java HTTPServer";
        String contentLengthLine = null;
        String fileName = null;
        String contentTypeLine = "Content-Type: text/html" + "\r\n";
        FileInputStream fin = null;

        if (statusCode == 200)
            statusLine = "HTTP/1.1 200 OK" + "\r\n";
        else
            statusLine = "HTTP/1.1 404 Not Found" + "\r\n";

        if (isFile) {
            fileName = responseString;
            fin = new FileInputStream(fileName);
            contentLengthLine = "Content-Length: " + Integer.toString(fin.available()) + "\r\n";
            if (!fileName.endsWith(".htm") && !fileName.endsWith(".html"))
                contentTypeLine = "Content-Type: \r\n";
        } else if (!ishtml && !isFile) {
            contentLengthLine = "Content-Length: " + responseString.length() + "\r\n";
        } else {
            responseString = serverHandler.HTML_START + responseString + serverHandler.HTML_END;
            contentLengthLine = "Content-Length: " + responseString.length() + "\r\n";
        }

        outToClient.writeBytes(statusLine);
        outToClient.writeBytes(serverdetails);
        outToClient.writeBytes(contentTypeLine);
        outToClient.writeBytes(contentLengthLine);
        outToClient.writeBytes("Connection: close\r\n");
        outToClient.writeBytes("\r\n");

        if (isFile)
            sendFile(fin, outToClient);
        else
            outToClient.writeBytes(responseString);

        // outToClient.close();
    }

    // funcao utilizada para enviar um ficheiro
    public void sendFile(FileInputStream fin, DataOutputStream out) throws Exception {
        byte[] buffer = new byte[1024];
        int bytesRead;

        while ((bytesRead = fin.read(buffer)) != -1) {
            out.write(buffer, 0, bytesRead);
        }
        fin.close();
    }

    // adiciona novo utilizador à class data
    public void newUser(String user) {
        global.newUser(user);
        setUser(user);
    }

    // vai buscar todos os utilizadores registados na class data
    public String getUsers() {
        return global.getUsers().toString();
    }

    //fixa o utilizador em questao a variavel local user
    public void setUser(String s){
        user = global.getUser(s);
        System.out.println("user local: " + user);
    }

    public String getMensagens(){
        return global.getMensagens().toString();
    }
    public void postMensagem(String id, String corpo, String hora, String username){
        global.newMensagem(id, corpo, hora, username);

    }

    // public user getUser(){
    //    return user;
    // }

    public void run() {
        try {
            System.out.println("ligacao efetuada");

            // inputstream = new FileInputStream("jscript.js");

            // int data = inputstream.read();
            // while (data != -1) {
            // // System.out.println(data);
            // data = inputstream.read();
            // }

            // String content = "";
            // try {
            // BufferedReader in = new BufferedReader(new FileReader("index.html"));
            // String str;
            // while ((str = in.readLine()) != null) {
            // content += str;
            // }
            // in.close();
            // } catch (IOException e) {
            // }

            BufferedReader bf = new BufferedReader(new InputStreamReader(ligacao.getInputStream()));

            //while (!Thread.currentThread().isInterrupted()) {
                try {
                    read = bf.readLine();
                    if (read != null) {
                        System.out.println(read + "fim");
                        StringBuilder payload = new StringBuilder();
                        while (bf.ready()) {
                            payload.append((char) bf.read());
                        }
                        System.out.println("payload:" + payload);
                        if (read.equals("GET / HTTP/1.1")) {
                            System.out.println("get html");
                            // String httpResponse = "HTTP/1.1 200 OK\r\n\r\n" + goString("index.html");
                            // out.write(httpResponse.getBytes("UTF-8"));
                            // out.flush();
                            //sendResponse(200, goString("index.html"), false, true);
                            sendResponse(200, "template/startbootstrap-sb-admin-2-gh-pages/index.html", true, true);
                        } else if (read.contains("/jscript")) {
                            System.out.println("get jscript");
                            sendResponse(200, "jscript.js", true, false);
                        } else if (read.contains("/postUser")) {
                            String result = "";
                            // System.out.println("payload if: " + webData);
                            // System.out.println("sua linda payload: " + payload.toString());
                            System.out.println("chegou pois");
                            //retira o valor do username do http request
                            for (int i = 0; i < payload.toString().length(); i++) {
                                // System.out.println("entrou for");
                                if (payload.toString().charAt(i) == 'u') {
                                    if (payload.toString().substring(i, i + 8).equals("username")) {
                                        System.out.println("temos username");
                                        for (int d = i + 11; d < payload.toString().length() - 2; d++) {
                                            result += payload.toString().charAt(d);
                                        }
                                        break;
                                    }
                                }
                            }
                            System.out.println(result);
                            
                            newUser(result);
                            sendResponse(200, "sucess" + user.getId(), false, false);

                        }else if(read.contains("/vendor/fontawesome-free/css/all.min.css")){
                            System.out.println("sending fontawesome");
                            sendResponse(200, "template/startbootstrap-sb-admin-2-gh-pages/vendor/fontawesome-free/css/all.min.css", true, false);
                        }else if(read.contains("/vendor/bootstrap/js/bootstrap.bundle.min.js")){
                            System.out.println("sending bootstrap");
                            sendResponse(200, "template/startbootstrap-sb-admin-2-gh-pages/vendor/bootstrap/js/bootstrap.bundle.min.js", true, false);
                        }else if(read.contains("/js/sb-admin-2.min.js")){
                            System.out.println("sending /js/sb-admin-2.min.js");
                            sendResponse(200, "template/startbootstrap-sb-admin-2-gh-pages/js/sb-admin-2.min.js", true, false);
                        }else if(read.contains("/vendor/jquery/jquery.min.js")){
                            System.out.println("sending jquery");
                            sendResponse(200, "template/startbootstrap-sb-admin-2-gh-pages/vendor/jquery/jquery.min.js", true, false);
                        }else if(read.contains("/css/sb-admin-2.min.css")){
                            System.out.println("sending css");
                            sendResponse(200, "template/startbootstrap-sb-admin-2-gh-pages/css/sb-admin-2.min.css", true, false);
                        }else if(read.contains("/getUsers")){
                            System.out.println("get users");
                            sendResponse(200, getUsers(), false, false);
                        }else if(read.contains("/getMensagens")){
                            System.out.println("get mensagens");
                            //sendResponse(200, "users:" + getUsers() + "mensagens" + getMensagens(), false, false);
                            sendResponse(200, getMensagens(), false, false);
                        }
                        else if(read.contains("/postMensagem")){
                            String set;
                            System.out.println("/postMensagem");
                            for (int i = 0; i < payload.toString().length(); i++) {
                                // System.out.println("entrou for");
                                if (payload.toString().charAt(i) == 'i') {
                                    if (payload.toString().substring(i, i + 6).equals("idUser")) {
                                        System.out.println("temos mensagem");
                                        set = payload.toString().substring(i);
                                        System.out.println(set);
                                        String[] arrOfStr = set.split("\"");
                                        System.out.println("array: " + arrOfStr[2] + arrOfStr[6] + arrOfStr[10] + arrOfStr[14]);
                                        System.out.println(arrOfStr);
                                        postMensagem(arrOfStr[2], arrOfStr[6], arrOfStr[14], arrOfStr[10]);

                                        // for (int d = i + 11; d < payload.toString().length() - 2; d++) {
                                        //     result += payload.toString().charAt(d);
                                        // }
                                        // break;
                                    }
                                }
                            }
                            sendResponse(200, "success", false, false);
                        }
                        // if (read.contains("jscript")) {
                        // System.out.println("get jscript");
                        // // String httpResponse = "HTTP/1.1 200 OK\r\n\r\n" + goString("index.html");
                        // // out.write(httpResponse.getBytes("UTF-8"));
                        // // out.flush();
                        // sendResponse(200, "jscript.js", true, false);
                        // }
                        // if (read.contains("/getuser")) {
                        // System.out.println("get chatuser");
                        // // if (payload.toString().contains("username")) {
                        // // //global
                        // // System.out.println("username: " + payload.toString());
                        // // }
                        // System.out.println("payload: " + payload.toString());
                        // // String httpResponse = "HTTP/1.1 200 OK\r\n\r\n" + goString("index.html");
                        // // out.write(httpResponse.getBytes("UTF-8"));
                        // // out.flush();
                        // System.out.println(getUsers());
                        // sendResponse(200, "exemploHTML.html", true, true);
                        // }
                        // if (payload.toString().contains("username")) {
                        // //global
                        // System.out.println("username: " + payload.toString());
                        // }
                        // if (read.contains("/gethope")) {
                        // System.out.println("entrou gethpe");
                        // sendResponse(200, "resposta linda", false, false);
                        // }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            //}

        } catch (

        Exception e) {
            e.printStackTrace();
        }

    }
}