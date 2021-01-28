import java.util.ArrayList;

public class data{

    ArrayList<user> usersList = new ArrayList<user>();
    ArrayList<mensagem> mensagensList = new ArrayList<mensagem>();
    int id = 1;

    public data(){
        
    }

    private void addUser(user user){
        usersList.add(user);
    }

    public ArrayList getUsers(){
        return usersList;
    }

    public user getUser(String s){
        user result = new user( );
        for(user u: usersList){
            if(u.getUsername() == s){
                result = u;
            }
        }
        return result;
    }
    
    public void newUser(String username){
        user u = new user(id, username);
        addUser(u);
        id++;
        System.out.println("users: " + getUsers());
    }

    public void newMensagem(String id, String corpo, String hora, String username){
        mensagem m = new mensagem(id, corpo, hora, username);
        addMensagem(m);
    }

    public void addMensagem(mensagem m){
        mensagensList.add(m);
    }

    public ArrayList getMensagens(){
        return mensagensList;
    }

    public String toString(){
        System.out.println("tostring data");
        return usersList.toString();
    }

}