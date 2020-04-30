package Messages;

public class UsernameTakenError extends Message{
    private static final long serialVersionUID = 100026L;

    public UsernameTakenError(){
        messageID = USERNAME_TAKEN_ERROR;
    }
}
