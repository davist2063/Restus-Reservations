package models;

import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Message {
    private int id;
    private int roomId;
    private int orderId;
    private ReadOnlyStringProperty sender;
    private ReadOnlyStringProperty recipient;
    private ReadOnlyStringProperty date;
    private ReadOnlyStringProperty subject;
    private ReadOnlyStringProperty message;
    private ReadOnlyIntegerProperty canComplete;
    private ReadOnlyIntegerProperty isComplete;

    public Message(int id, int roomId, int orderId, String sender, String recipient,
            String date, String subject, String message,
            Integer canComplete, Integer isComplete) {
        this.id = id;
        this.roomId = roomId;
        this.orderId = orderId;
        this.sender = new SimpleStringProperty(sender);
        this.recipient = new SimpleStringProperty(recipient);
        this.date = new SimpleStringProperty(date);
        this.subject = new SimpleStringProperty(subject);
        this.message = new SimpleStringProperty(message);
        this.canComplete = new SimpleIntegerProperty(canComplete);
        this.isComplete = new SimpleIntegerProperty(isComplete);
    }

    public int getId() {
        return id;
    }

    public int getRoomId() {
        return roomId;
    }

    public int getOrderId() {
        return orderId;
    }

    public String getSender() {
        return sender.get();
    }

    public ReadOnlyStringProperty senderProperty() {
        return sender;
    }

    public String getRecipient() {
        return recipient.get();
    }

    public ReadOnlyStringProperty recipientProperty() {
        return recipient;
    }
    
    public String getDate() {
        return date.get();
    }
    
    public ReadOnlyStringProperty dateProperty() {
        return date;
    }

    public String getSubject() {
        return subject.get();
    }

    public ReadOnlyStringProperty subjectProperty() {
        return subject;
    }

    public String getMessage() {
        return message.get();
    }

    public ReadOnlyStringProperty messageProperty() {
        return message;
    }

    public int getCanComplete() {
        return canComplete.get();
    }

    public ReadOnlyIntegerProperty canCompleteProperty() {
        return canComplete;
    }

    public int getIsComplete() {
        return isComplete.get();
    }

    public ReadOnlyIntegerProperty isCompleteProperty() {
        return isComplete;
    }

    @Override
    public String toString() {
        //Replace with String
        return super.toString();
    }
}
