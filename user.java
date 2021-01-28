public class user{

    String username;
    int id;

    public user(int id, String username){
        System.out.println("new user:");
        System.out.println(id);
        System.out.println(username);
        this.username = username;
        this.id = id;
    }

    public user(){

    }

    public String getUsername(){
        return username;
    }

    public String getId(){
        return Integer.toString(id);
    }

    public String toString(){
        return username; 
    }

}