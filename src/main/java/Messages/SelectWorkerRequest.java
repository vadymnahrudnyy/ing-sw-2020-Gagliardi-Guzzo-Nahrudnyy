package Messages;

public class SelectWorkerRequest extends Message {
    private static final long serialVersionUID = 100013L;
    private static final int messageID = 106;

    public int getMessageID() {
        return messageID;
    }
}
