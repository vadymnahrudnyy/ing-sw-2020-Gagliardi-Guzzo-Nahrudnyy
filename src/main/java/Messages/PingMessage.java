package Messages;

public class PingMessage extends Message {
    private static final long serialVersionUID = 10022L;

    public PingMessage(){
        messageID = PING_MESSAGE;
    }
}
