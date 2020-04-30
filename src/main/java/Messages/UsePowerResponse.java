package Messages;

public class UsePowerResponse extends Message{
    private static final long serialVersionUID = 100028L;
    private final boolean usePower;

    public UsePowerResponse(boolean userAnswer){
        messageID = USE_POWER_RESPONSE;
        usePower = userAnswer;
    }

    public boolean wantUsePower(){return usePower;}
}
