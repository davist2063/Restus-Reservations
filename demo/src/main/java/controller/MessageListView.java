package controller;

import service.App;
import service.dao.MessageDAO;
import service.dao.RoomDAO;
import service.dao.RoomOrderDAO;
import models.Message;
import models.Room;
import models.RoomOrder;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.beans.binding.Bindings;
import javafx.scene.control.Button;

import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class MessageListView {    
    public TableView<Message> messageTable;
    public TableColumn<Message, String> fromColumn;
    public TableColumn<Message, String> subjectColumn;
    public TableColumn<Message, String> dateReceivedColumn;
    
    public Button addButton;
    public Button viewButton;
    public Button deleteButton;
    public Button switchButton;

    @FXML
    private void switchBackToMenu() throws IOException {
        App.setRoot("/view/menuPage");
    }

    public void initialize() {
        messageTable.setItems(MessageDAO.getMessages());

        fromColumn.setCellValueFactory(new PropertyValueFactory<>("sender"));
        subjectColumn.setCellValueFactory(new PropertyValueFactory<>("subject"));
        dateReceivedColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        
        viewButton.disableProperty().bind(Bindings.isEmpty(messageTable.getSelectionModel().getSelectedItems()));
        deleteButton.disableProperty().bind(Bindings.isEmpty(messageTable.getSelectionModel().getSelectedItems()));
    }

    public void handleExitButtonClicked(ActionEvent event) {
        Platform.exit();
        event.consume();
    }

    @FXML
    public void addMessage(ActionEvent event) throws IOException {
        App.setRoot("/view/messageView");
        event.consume();
    }

    @FXML
    public void deleteMessage(ActionEvent event) {
        for (Message message : messageTable.getSelectionModel().getSelectedItems()) {
            if (message.getCanComplete() == 1) {
                //Update Current Message
                MessageDAO.update(new Message(
                    message.getId(),
                    message.getRoomId(),
                    message.getOrderId(),
                    message.getSender(),
                    message.getRecipient(),
                    message.getDate(),
                    message.getSubject(),
                    message.getMessage(),
                    0,
                    1
                ));

                //Attach room guest to the room and update the status
                //Get room order
                Optional<RoomOrder> roomOrder = RoomOrderDAO.getRoomOrder(message.getOrderId());
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
                Optional<Room> roomOptional = RoomDAO.getRoom(message.getRoomId());
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
                    message.getRoomId(), 
                    message.getOrderId(), 
                    message.getRecipient(),
                    message.getSender(),
                    LocalDate.now().format(DateTimeFormatter.ofPattern("MM/dd/yyyy")), 
                    "Reservation DENIED",
                    "Order: " + message.getOrderId() + "\nWe regret to inform you that your room reservation request has been denied!",
                    0,
                    0);
                event.consume();
            }
            MessageDAO.delete(message.getId());
            if(messageTable.getSelectionModel().getSelectedItems().size() == 0) {
                break;
            }
        }
        event.consume();
    }

    @FXML
    public void viewMessage(ActionEvent event) throws IOException {
        App.setRoot("/view/messageView", messageTable.getSelectionModel().getSelectedItem());
        event.consume();
    }
}
