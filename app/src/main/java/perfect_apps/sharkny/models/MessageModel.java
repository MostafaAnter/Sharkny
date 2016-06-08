package perfect_apps.sharkny.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mostafa on 03/06/16.
 */
public class MessageModel implements Parcelable {
    private String userName;
    private String messageBody;

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    private String sender_id;

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    private boolean read;
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

    public MessageModel(String userName, String messageBody, String sender_id, Boolean read){
        this.userName = userName;
        this.messageBody = messageBody;
        this.read = read;
        this.sender_id = sender_id;
    }

    protected MessageModel(Parcel in) {
        userName = in.readString();
        messageBody = in.readString();
        sender_id = in.readString();
        read = in.readByte() != 0x00;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userName);
        dest.writeString(messageBody);
        dest.writeString(sender_id);
        dest.writeByte((byte) (read ? 0x01 : 0x00));
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<MessageModel> CREATOR = new Parcelable.Creator<MessageModel>() {
        @Override
        public MessageModel createFromParcel(Parcel in) {
            return new MessageModel(in);
        }

        @Override
        public MessageModel[] newArray(int size) {
            return new MessageModel[size];
        }
    };
}