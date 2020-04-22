package Messages;

public class NumPlayersRequest extends Message{
    private static final long serialVersionUID = 100003L;
    private static final int messageID = 102;

    public int getID (){
        return messageID;
    }
}
