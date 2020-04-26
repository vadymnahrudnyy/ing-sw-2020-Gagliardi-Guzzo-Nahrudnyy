package Messages;

public abstract class Message {
    private final int messageID;

    protected Message() {
        messageID = 0;
    }

    public int getMessageID() {
        return messageID;
    }
}
