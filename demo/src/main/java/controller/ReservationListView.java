package controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import models.ReservationView;
import models.Room;
import models.RoomOrder;
import service.App;
import service.dao.MessageDAO;
import service.dao.ReservationViewDAO;
import service.dao.RoomDAO;
import service.dao.RoomOrderDAO;

public class ReservationListView {
    public TableView<ReservationView> reservationTable;
    public TableColumn<ReservationView, Integer> orderIdColumn;
    public TableColumn<ReservationView, String> roomColumn;
    public TableColumn<ReservationView, String> reservationStartColumn;
    public TableColumn<ReservationView, String> reservationEndColumn;
    public TableColumn<ReservationView, String> reservationStatusColumn;
    public TableColumn<ReservationView, String> roomOrderStatusColumn;

    @FXML
    public Button checkInButton;
    @FXML
    public Button checkOutButton;
    
    public void initialize() {
        reservationTable.setItems(ReservationViewDAO.getReservations());

        orderIdColumn.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        roomColumn.setCellValueFactory(new PropertyValueFactory<>("room"));
        reservationStartColumn.setCellValueFactory(new PropertyValueFactory<>("reservationStart"));
        reservationEndColumn.setCellValueFactory(new PropertyValueFactory<>("reservationEnd"));
        reservationStatusColumn.setCellValueFactory(new PropertyValueFactory<>("reservationStatusString"));
        roomOrderStatusColumn.setCellValueFactory(new PropertyValueFactory<>("roomOrderStatusString"));

        checkInButton.setDisable(true);
        checkOutButton.setDisable(true);

        reservationTable.setRowFactory( tv -> {
            TableRow<ReservationView> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1 && (! row.isEmpty()) ) {
                    ReservationView rowData = row.getItem();
                    int reservationStatus = rowData.getRoomOrderStatus();
                    switch(reservationStatus) {
                        case 0: // Request Denied
                            checkInButton.setDisable(true);
                            checkOutButton.setDisable(true);
                            break;
                        case 1: // Pending
                            checkInButton.setDisable(true);
                            
                            checkOutButton.setDisable(true);
                            break;
                        case 2: // Not Right Day (WIP)
                            checkInButton.setDisable(false); //Change back to true later (False for demonstration purposes)
                            checkOutButton.setDisable(false);  //Change back to true later (False for demonstration purposes)
                            break;
                        case 3: // Right Day (Can Check In) (WIP)
                            checkInButton.setDisable(false);
                            checkOutButton.setDisable(true);
                            break;
                        case 4: // Has Checked In (Can Check Out)
                            checkInButton.setDisable(true);
                            checkOutButton.setDisable(false);
                            break;
                        case 5: // Already checked in and out
                            checkInButton.setDisable(true);
                            checkOutButton.setDisable(true);
                            break;
                        default:
                            checkInButton.setDisable(true);
                            checkOutButton.setDisable(true);
                            break;
                    }
                }
            });
            return row ;
        });
    }

    @FXML
    private void checkIn() {
        ReservationView selectedReservation = reservationTable.getSelectionModel().getSelectedItem();
        
        //Update room status to checked in.
        Optional<RoomOrder> optionalRoomOrder = RoomOrderDAO.getRoomOrder(selectedReservation.getOrderId());
        RoomOrder roomOrder = optionalRoomOrder.get();
        RoomOrderDAO.update(new RoomOrder(
            roomOrder.getId(), 
            roomOrder.getRoomId(), 
            roomOrder.getRoomGuest(), 
            roomOrder.getNumAdult(), 
            roomOrder.getNumKid(), 
            roomOrder.getNumPet(), 
            roomOrder.getInvoiceTotal(), 
            roomOrder.getCheckInDate(), 
            roomOrder.getCheckOutDate(), 
            4));
        

        //Send message to the room owner
        Optional<Room> optionalRoom = RoomDAO.getRoom(roomOrder.getRoomId());
        Room room = optionalRoom.get();

        MessageDAO.insertMessage(
            roomOrder.getRoomId(), 
            roomOrder.getId(), 
            App.getUser(), 
            room.getRoomOwner(), 
            LocalDate.now().format(DateTimeFormatter.ofPattern("MM/dd/yyyy")), 
            App.getUser() + " has checked in", 
            "This message is automatically send to say that " + App.getUser() + " has checked into your room " + room.getRoomName() + "!", 
            0, 
            0);
        reservationTable.setItems(ReservationViewDAO.getReservations());
        checkInButton.setDisable(true);
        checkOutButton.setDisable(true);
    }

    @FXML
    private void checkOut() {
        ReservationView selectedReservation = reservationTable.getSelectionModel().getSelectedItem();
        
        //Update room status to checked in.
        Optional<RoomOrder> optionalRoomOrder = RoomOrderDAO.getRoomOrder(selectedReservation.getOrderId());
        RoomOrder roomOrder = optionalRoomOrder.get();
        RoomOrderDAO.update(new RoomOrder(
            roomOrder.getId(), 
            roomOrder.getRoomId(), 
            roomOrder.getRoomGuest(), 
            roomOrder.getNumAdult(), 
            roomOrder.getNumKid(), 
            roomOrder.getNumPet(), 
            roomOrder.getInvoiceTotal(), 
            roomOrder.getCheckInDate(), 
            roomOrder.getCheckOutDate(), 
            5));
        

        //Update Room Status to modify for next booking
        Optional<Room> optionalRoom = RoomDAO.getRoom(roomOrder.getRoomId());
        Room room = optionalRoom.get();
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
            3, 
            "", 
            room.getRoomOwner()));

        //Send message to the room owner
        MessageDAO.insertMessage(
            roomOrder.getRoomId(), 
            roomOrder.getId(), 
            App.getUser(), 
            room.getRoomOwner(), 
            LocalDate.now().format(DateTimeFormatter.ofPattern("MM/dd/yyyy")), 
            App.getUser() + " has checked out", 
            "This message is automatically send to say that " + App.getUser() + " has checked out your room " + room.getRoomName() + "!", 
            0, 
            0);
        reservationTable.setItems(ReservationViewDAO.getReservations());
        checkInButton.setDisable(true);
        checkOutButton.setDisable(true);
    }

    @FXML
    private void switchBackToMenu() throws IOException {
        App.setRoot("/view/menuPage");
    }

    public void handleExitButtonClicked(ActionEvent event) {
        Platform.exit();
        event.consume();
    }
}
