public class mensagem{
    
    String id;
    String corpo;
    String hora;
    String username;

    public mensagem(String id, String corpo, String hora, String username){
        System.out.println("mensagem regist: " + id + corpo + hora);
        this.id = id;
        this.corpo = corpo;
        this.hora = hora;
        this.username = username;
    }

    public String toString(){
        return id + corpo + hora + username; 
    }



}