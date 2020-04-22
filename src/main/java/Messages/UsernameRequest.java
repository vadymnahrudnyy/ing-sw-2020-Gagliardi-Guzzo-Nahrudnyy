package Messages;

public class UsernameRequest extends Message{
    private static final long serialVersionUID = 100001L;
    private static final int messageID = 101;

    public int getID (){
        return messageID;
    }
}
