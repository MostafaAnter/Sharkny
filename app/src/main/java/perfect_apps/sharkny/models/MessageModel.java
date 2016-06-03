package perfect_apps.sharkny.models;

/**
 * Created by mostafa on 03/06/16.
 */
public class MessageModel {
    private String userName, messageBody;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    public MessageModel(){

    }

    public MessageModel(String userName, String messageBody){
        this.userName = userName;
        this.messageBody = messageBody;
    }
}
