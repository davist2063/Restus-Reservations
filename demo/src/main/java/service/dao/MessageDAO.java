package service.dao;

import models.Message;
import service.App;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MessageDAO {
    private static final String tableName = "Messages";
    private static final String idColumn = "id";
    private static final String roomIdColumn = "roomId";
    private static final String orderIdColumn = "orderId";
    private static final String senderColumn = "sender";
    private static final String recipientColumn = "recipient";
    private static final String dateColumn = "date";
    private static final String subjectColumn = "subject";
    private static final String messageColumn = "message";
    private static final String canCompleteColumn = "canComplete";
    private static final String isCompleteColumn = "isComplete";

    private static final ObservableList<Message> messages;

    static {
        messages = FXCollections.observableArrayList();
        updateMessagesFromDB();
    }

    public static ObservableList<Message> getMessages() {
        updateMessagesFromDB();
        return FXCollections.unmodifiableObservableList(messages);
    }

    private static void updateMessagesFromDB() {
        String query = "SELECT * FROM " + tableName + " WHERE " + tableName + ".Recipient='" + App.getUser() + "'";
        
        try(Connection connection = Database.connect()) {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            messages.clear();
            while (rs.next()) {
                messages.add(new Message(
                    rs.getInt(idColumn), 
                    rs.getInt(roomIdColumn), 
                    rs.getInt(orderIdColumn), 
                    rs.getString(senderColumn), 
                    rs.getString(recipientColumn), 
                    rs.getString(dateColumn), 
                    rs.getString(subjectColumn), 
                    rs.getString(messageColumn), 
                    rs.getInt(canCompleteColumn), 
                    rs.getInt(isCompleteColumn)));
            }
        } catch (SQLException e) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ": Could not load Messages from database ");
            messages.clear();
            return;
        }

        return;
    }

    public static void update(Message newMessage) {
        //update database
        long rows = CRUDHelper.update(
            tableName,
            new String[] {
                roomIdColumn,
                orderIdColumn,
                senderColumn,
                recipientColumn,
                dateColumn,
                subjectColumn,
                messageColumn,
                canCompleteColumn,
                isCompleteColumn,
            },
            new Object[] {
                newMessage.getRoomId(),
                newMessage.getOrderId(),
                newMessage.getSender(),
                newMessage.getRecipient(),
                newMessage.getDate(),
                newMessage.getSubject(),
                newMessage.getMessage(),
                newMessage.getCanComplete(),
                newMessage.getIsComplete(),
            },
            new int[] {
                Types.INTEGER,
                Types.INTEGER,
                Types.VARCHAR,
                Types.VARCHAR,
                Types.VARCHAR,
                Types.VARCHAR,
                Types.VARCHAR,
                Types.INTEGER,
                Types.INTEGER,
            },
            idColumn,
            Types.INTEGER,
            newMessage.getId()
        );

        if (rows == 0)
            throw new IllegalStateException("User to be updated with id " 
                + newMessage.getId() + " didn't exist in database");

        //update cache
        Optional<Message> optionalMessage = getMessage(newMessage.getId());
        optionalMessage.ifPresentOrElse((oldMessage) -> {
            messages.remove(oldMessage);
            messages.add(newMessage);
        }, () -> {
            throw new IllegalStateException("User to be updated with id " 
                + newMessage.getId() + " didn't exist in database");
        });
        return;
    }

    public static void insertMessage(int roomId, 
        int orderId, String sender, String recipient,
        String date, String subject, String message,
        Integer canComplete, Integer isComplete) {
        //update database
        int id = (int) CRUDHelper.create(
            tableName, 
            new String[]{
                roomIdColumn,
                orderIdColumn,
                senderColumn,
                recipientColumn,
                dateColumn,
                subjectColumn,
                messageColumn,
                canCompleteColumn,
                isCompleteColumn,
            },
            new Object[]{
                roomId,
                orderId,
                sender,
                recipient,
                date,
                subject,
                message,
                canComplete,
                isComplete
            },
            new int[]{
                Types.INTEGER,
                Types.INTEGER,
                Types.VARCHAR,
                Types.VARCHAR,
                Types.VARCHAR,
                Types.VARCHAR,
                Types.VARCHAR,
                Types.INTEGER,
                Types.INTEGER,
            });

        //update cache
        messages.add(new Message(
            id,
            roomId,
            orderId,
            sender,
            recipient,
            date,
            subject,
            message,
            canComplete,
            isComplete
        ));
    }

    public static void delete(int id) {
        //update database
        CRUDHelper.delete(tableName, id);

        //update cache
        Optional<Message> message = getMessage(id);
        message.ifPresent(messages::remove);
    }

    public static Optional<Message> getMessage(int id) {
        for (Message message : messages) {
            if (message.getId() == id) return Optional.of(message);
        }
        return Optional.empty();
    }
}
