package eu.telecomnancy.labfx.messagerie;

import java.sql.Timestamp;

public class Message {
    private int messageId;
    private int conversationId;
    private int senderId;
    private int receiverId;
    private String messageText;
    private Timestamp sendTime;
    private boolean isRead;

    // Constructeur
    public Message(int messageId, int conversationId, int senderId,int receiver_id, String messageText, Timestamp sendTime, boolean isRead) {
        this.messageId = messageId;
        this.conversationId = conversationId;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.messageText = messageText;
        this.sendTime = sendTime;
        this.isRead = isRead;
    }

    // Getters et setters de messageId
    public int getMessageId() {
        return messageId;
    }
    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    // Getters et setters de conversationId
    public int getConversationId() {
        return conversationId;
    }
    public void setConversationId(int conversationId) {
        this.conversationId = conversationId;
    }

    // Getters et setters de senderId
    public int getSenderId() {
        return senderId;
    }
    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    // Getters et setters de receiverId
    public int getReceiverId() {
        return receiverId;
    }
    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
    }

    // Getters et setters de messageText
    public String getMessageText() {
        return messageText;
    }
    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    // Getters et setters de timestamp
    public Timestamp getSendTime() {
        return sendTime;
    }
    public void setSendTime(Timestamp sendTime) {
        this.sendTime = sendTime;
    }

    // Getters et setters de isRead
    public boolean isRead() {
        return isRead;
    }
    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }

    

}
