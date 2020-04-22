package Messages;

public class UsernameResponse extends Message{
    private static final long serialVersionUID = 100002L;
    private static final int messageID = 201;
    private final String playerUsername;

    public UsernameResponse (String username){
        playerUsername = username;
    }
    public int getID (){
        return messageID;
    }
    public String getUsername (){
        return playerUsername;
    }
}
