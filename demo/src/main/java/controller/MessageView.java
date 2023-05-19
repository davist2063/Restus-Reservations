package controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import models.Message;
import models.Room;
import models.RoomOrder;
import service.App;
import service.dao.MessageDAO;
import service.dao.RoomDAO;
import service.dao.RoomOrderDAO;

public class MessageView {
    private Message messageData;

    @FXML
    private VBox topBar;
    @FXML
    private Label fromUser;
    @FXML
    private Label date;
    @FXML
    private TextArea message;
    @FXML
    private HBox canCompleteButtons2;
    @FXML
    private Button sendMessageButton;
    @FXML
    private Button cancelMessageButton;
    @FXML
    private HBox canCompleteButtons1;
    @FXML
    private Button confirmReservationRequestButton;
    @FXML
    private Button denyReservationRequestButton;
    @FXML
    private Button cancelMessageButton1;
    @FXML
    private TextField toUser;
    @FXML
    private TextField subject;
    

    @FXML
    private void initialize() {
        messageData = (Message) App.getData();
        if(messageData != null) {
            if (messageData.getCanComplete() == 0) {
                canCompleteButtons1.setVisible(false);
                sendMessageButton.setVisible(false);
            }
            else {
                canCompleteButtons2.setVisible(false);
            }
            toUser.setText(App.getUser());
            subject.setText(messageData.getSubject());
            fromUser.setText(messageData.getSender());
            date.setText(messageData.getDate());
            message.setText(messageData.getMessage());
        }
        else {
            canCompleteButtons1.setVisible(false);
            fromUser.setText(App.getUser());
            date.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
        }
    }

    public void handleExitButtonClicked(ActionEvent event) throws IOException {
        App.setRoot("/view/messageListView");
        event.consume();
    }

    @FXML
    public void sendMessage(ActionEvent event) throws IOException {
        MessageDAO.insertMessage(
            2147483647, 
            2147483647, 
            App.getUser(),
            toUser.getText(),
            LocalDate.now().format(DateTimeFormatter.ofPattern("MM/dd/yyyy")), 
            subject.getText(),
            message.getText(),
            0,
            0);
        App.setRoot("/view/messageListView");
        event.consume();
    }

    @FXML
    public void confirmReservationMessage(ActionEvent event) throws IOException {
        //Update Current Message
        MessageDAO.update(new Message(
            messageData.getId(),
            messageData.getRoomId(),
            messageData.getOrderId(),
            messageData.getSender(),
            messageData.getRecipient(),
            messageData.getDate(),
            messageData.getSubject(),
            messageData.getMessage(),
            0,
            1
        ));

        //Attach room guest to the room and update the status
        //Get room order
        Optional<RoomOrder> roomOrder = RoomOrderDAO.getRoomOrder(messageData.getOrderId());
        RoomOrder order = roomOrder.get();
        
        //update room order
        RoomOrderDAO.update(new RoomOrder(
            order.getId(), 
            order.getRoomId(), 
            order.getRoomGuest(), 
            order.getNumAdult(), 
            order.getNumKid(), 
            order.getNumPet(), 
            order.getInvoiceTotal(), 
            order.getCheckInDate(), 
            order.getCheckOutDate(), 
            2));

        //Update the current Room Status
        //get room
        Optional<Room> roomOptional = RoomDAO.getRoom(messageData.getRoomId());
        Room room = roomOptional.get();

        //update room order
        RoomDAO.update(new Room(
            room.getId(), 
            room.getRoomName(), 
            room.getRoomDescription(), 
            room.getPricePerNight(), 
            room.getExtraFee(), 
            room.getNumBed(), 
            room.getNumBath(), 
            room.getStartDate(), 
            room.getEndDate(), 
            room.getAddress(), 
            room.getAddress2(), 
            room.getState(), 
            room.getCity(), 
            room.getZip(), 
            1, 
            messageData.getSender(), 
            room.getRoomOwner()));

        //Send Confirmation Response Message back to Room Guest
        MessageDAO.insertMessage(
            messageData.getRoomId(), 
            messageData.getOrderId(), 
            messageData.getRecipient(),
            messageData.getSender(),
            LocalDate.now().format(DateTimeFormatter.ofPattern("MM/dd/yyyy")), 
            "Reservation Confirmed",
            "Order: " + messageData.getOrderId() + "\nYour reservation has been confirmed! You can see more details in the my reservations tab.",
            0,
            0);
        App.setRoot("/view/messageListView");
        event.consume();
    }

    @FXML
    public void denyReservationMessage(ActionEvent event) throws IOException {
        //Update Current Message
        MessageDAO.update(new Message(
            messageData.getId(),
            messageData.getRoomId(),
            messageData.getOrderId(),
            messageData.getSender(),
            messageData.getRecipient(),
            messageData.getDate(),
            messageData.getSubject(),
            messageData.getMessage(),
            0,
            1
        ));

        //Attach room guest to the room and update the status
        //Get room order
        Optional<RoomOrder> roomOrder = RoomOrderDAO.getRoomOrder(messageData.getOrderId());
        RoomOrder order = roomOrder.get();
        
        //update room order
        RoomOrderDAO.update(new RoomOrder(
            order.getId(), 
            order.getRoomId(), 
            order.getRoomGuest(), 
            order.getNumAdult(), 
            order.getNumKid(), 
            order.getNumPet(), 
            order.getInvoiceTotal(), 
            order.getCheckInDate(), 
            order.getCheckOutDate(), 
            0));

        //Update the current Room Status
        //get room
        Optional<Room> roomOptional = RoomDAO.getRoom(messageData.getRoomId());
        Room room = roomOptional.get();

        //update room order
        RoomDAO.update(new Room(
            room.getId(), 
            room.getRoomName(), 
            room.getRoomDescription(), 
            room.getPricePerNight(), 
            room.getExtraFee(), 
            room.getNumBed(), 
            room.getNumBath(), 
            room.getStartDate(), 
            room.getEndDate(), 
            room.getAddress(), 
            room.getAddress2(), 
            room.getState(), 
            room.getCity(), 
            room.getZip(), 
            0, 
            "", 
            room.getRoomOwner()));

        //Send Confirmation Response Message back to Room Guest
        MessageDAO.insertMessage(
            messageData.getRoomId(), 
            messageData.getOrderId(), 
            messageData.getRecipient(),
            messageData.getSender(),
            LocalDate.now().format(DateTimeFormatter.ofPattern("MM/dd/yyyy")), 
            "Reservation DENIED",
            "Order: " + messageData.getOrderId() + "\nWe regret to inform you that your room reservation request has been denied!",
            0,
            0);
        App.setRoot("/view/messageListView");
        event.consume();
    }
}
