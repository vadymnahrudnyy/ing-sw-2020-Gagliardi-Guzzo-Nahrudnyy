package Messages;

/**
 * Abstract class implementing the messages that will be sent between Server and Client.
 */
public abstract class Message {
    private final int messageID;

    protected Message() {
        messageID = 0;
    }

    public int getMessageID() {
        return messageID;
    }
}
