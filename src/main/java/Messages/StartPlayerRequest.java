package Messages;

public class StartPlayerRequest extends Message {
    private static final long serialVersionUID = 100009L;
    private static final int messageID = 104;

    public int getMessageID() {
        return messageID;
    }
}