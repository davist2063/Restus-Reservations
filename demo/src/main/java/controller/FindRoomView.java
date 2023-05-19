package controller;

import service.App;
import service.dao.MessageDAO;
import service.dao.RoomDAO;
import service.dao.RoomOrderDAO;
import models.Room;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.beans.binding.Bindings;
import javafx.scene.control.Button;

import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.function.UnaryOperator;

public class FindRoomView {    
    public TableView<Room> roomTable;
    public TableColumn<Room, String> roomNameColumn;
    public TableColumn<Room, Double> pricePerNightColumn;
    public TableColumn<Room, String> startDateColumn;
    public TableColumn<Room, String> endDateColumn;
    public TableColumn<Room, String> roomStatusColumn;
    
    public Button viewButton;

    @FXML
    private void switchBackToMenu() throws IOException {
        App.setRoot("/view/menuPage");
    }

    public void initialize() {
        roomTable.setItems(RoomDAO.getRooms());

        roomNameColumn.setCellValueFactory(new PropertyValueFactory<>("roomName"));
        pricePerNightColumn.setCellValueFactory(new PropertyValueFactory<>("pricePerNight"));
        startDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        endDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        roomStatusColumn.setCellValueFactory(new PropertyValueFactory<>("findRoomStatusString"));
        
        viewButton.disableProperty().bind(Bindings.isEmpty(roomTable.getSelectionModel().getSelectedItems()));
    }

    public void handleExitButtonClicked(ActionEvent event) {
        Platform.exit();
        event.consume();
    }

    @FXML
    public void viewRoom(ActionEvent event) {
        if (roomTable.getSelectionModel().getSelectedItems().size() != 1) {
            System.out.println("Error: Room viewing error, One room must be selected before viewing");
        }
        else {
            Dialog<Room> dialog = viewRoomDialog(roomTable.getSelectionModel().getSelectedItem());
            Optional<Room> optionalRoom = dialog.showAndWait();
            optionalRoom.ifPresent(RoomDAO::update);
        }
        event.consume();
    }

    private Dialog<Room> viewRoomDialog(Room room) {
        //create the dialog itself
        Dialog<Room> dialog = new Dialog<>();
        dialog.setTitle("Viewing Room Details: " + room.getRoomName());
        dialog.setHeaderText("Edit a database record");

        ButtonType saveOrderRequest = new ButtonType("Request Room", ButtonBar.ButtonData.OK_DONE);

        Stage dialogWindow = (Stage) dialog.getDialogPane().getScene().getWindow();

        //create the form for the user to fill in
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        // Section that displays the room.
        TextField roomName = new TextField();
        roomName.setEditable(false);
        TextArea roomDescription = new TextArea();
        roomDescription.setEditable(false);
        TextField pricePerNight = new TextField();
        pricePerNight.setEditable(false);
        TextField extraFee = new TextField();
        extraFee.setEditable(false);
        TextField numBed = new TextField();
        numBed.setEditable(false);
        TextField numBath = new TextField();
        numBath.setEditable(false);
        DatePicker startDate = new DatePicker();
        startDate.setEditable(false);
        DatePicker endDate = new DatePicker();
        endDate.setEditable(false);
        TextField address = new TextField();
        address.setEditable(false);
        TextField address2 = new TextField();
        address2.setEditable(false);
        TextField state = new TextField();
        state.setEditable(false);
        TextField city = new TextField();
        city.setEditable(false);
        TextField zip = new TextField();
        zip.setEditable(false);
        TextField roomOwner = new TextField();
        roomOwner.setEditable(false);

        grid.add(new Label("Room Name:"), 0, 0);
        grid.add(roomName, 1, 0);
        grid.add(new Label("Room Description:"), 0, 1);
        grid.add(roomDescription, 1, 1);
        grid.add(new Label("Price Per Night:"), 0, 2);
        grid.add(pricePerNight, 1, 2);
        grid.add(new Label("Extra Fee:"), 0, 3);
        grid.add(extraFee, 1, 3);
        grid.add(new Label("Number of Bedrooms:"), 0, 4);
        grid.add(numBed, 1, 4);
        grid.add(new Label("Number of Bathrooms:"), 0, 5);
        grid.add(numBath, 1, 5);
        grid.add(new Label("Start Date:"), 0, 6);
        grid.add(startDate, 1, 6);
        grid.add(new Label("End Date:"), 0, 7);
        grid.add(endDate, 1, 7);
        grid.add(new Label("Address:"), 0, 8);
        grid.add(address, 1, 8);
        grid.add(new Label("Address 2:"), 0, 9);
        grid.add(address2, 1, 9);
        grid.add(new Label("State:"), 0, 10);
        grid.add(state, 1, 10);
        grid.add(new Label("City:"), 0, 11);
        grid.add(city, 1, 11);
        grid.add(new Label("Zip:"), 0, 12);
        grid.add(zip, 1, 12);
        grid.add(new Label("Room Owner:"), 0, 13);
        grid.add(roomOwner, 1, 13);

        // Section that allows users to create a room order request.
        TextField numAdult = new TextField();
        numAdult.setPromptText("Number of Adults");
        TextField numKid = new TextField();
        numKid.setPromptText("Number of Children");
        TextField numPet = new TextField();
        numPet.setPromptText("Number of Pets");
        DatePicker checkInDate  = new DatePicker();
        checkInDate.setPromptText("");
        DatePicker checkOutDate = new DatePicker();
        checkOutDate.setPromptText("");
        TextField invoiceTotal = new TextField(); // Calculated Value


        grid.add(new Label("Create a Room Order Request Here: *"), 3, 2);
        grid.add(new Line(), 2, 3);
        grid.add(new Label("Number of Adults: *"), 3, 4);
        grid.add(numAdult, 4, 4);
        grid.add(new Label("Number of Children: *"), 3, 5);
        grid.add(numKid, 4, 5);
        grid.add(new Label("Number of Pets: *"), 3, 6);
        grid.add(numPet, 4, 6);
        grid.add(new Label("Check In Date: *"), 3, 7);
        grid.add(checkInDate, 4, 7);
        grid.add(new Label("Check Out Date: *"), 3, 8);
        grid.add(checkOutDate, 4, 8);
        grid.add(new Label("Invoice Total:"), 3, 9);
        grid.add(invoiceTotal, 4, 9);

        dialog.getDialogPane().setContent(grid);

        //if a record is supplied, use it to fill in the fields automatically
        if (room != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

            roomName.setText(room.getRoomName());
            roomDescription.setText(room.getRoomDescription());
            pricePerNight.setText(String.valueOf(room.getPricePerNight()));
            extraFee.setText(String.valueOf(room.getExtraFee()));
            numBed.setText(String.valueOf(room.getNumBath()));
            numBath.setText(String.valueOf(room.getNumBath()));
            startDate.setValue(LocalDate.parse(room.getStartDate(), formatter));
            endDate.setValue(LocalDate.parse(room.getEndDate(), formatter));
            address.setText(room.getAddress());
            address2.setText(room.getAddress2());
            state.setText(room.getState());
            city.setText(room.getCity());
            zip.setText(room.getZip());
            roomOwner.setText(room.getRoomOwner());
        }

        // Toggles the create room order request button
        if(room.getRoomStatus() == 0) {
            dialog.getDialogPane().getButtonTypes().addAll(saveOrderRequest, ButtonType.CANCEL);

            //disable the OK button if the fields haven't been filled in
            dialog.getDialogPane().lookupButton(saveOrderRequest).disableProperty().bind(
                    Bindings.createBooleanBinding(() -> numAdult.getText().trim().isEmpty(), numAdult.textProperty())
                    .or(Bindings.createBooleanBinding(() -> invoiceTotal.getText().trim().isEmpty(), invoiceTotal.textProperty())
            ));
        }
        else {
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL);
        }

        //ensure only numeric input (integers) in age text field
        UnaryOperator<TextFormatter.Change> numberValidationFormatter = change -> {
            if (change.getText().matches("\\d+") || change.getText().equals("")) {
                int adults = 0;
                int kids = 0;
                int pets = 0;
                int days = 0;
                double total = 0.00;

                if (numAdult.getText() != "") {
                    adults = Integer.parseInt(numAdult.getText());
                }
                if (numKid.getText() != "") {
                    kids = Integer.parseInt(numKid.getText());
                }
                if (numPet.getText() != "") {
                    pets = Integer.parseInt(numPet.getText());
                }
                if (checkInDate.getValue() != null && checkOutDate.getValue() != null) {
                    LocalDate date1 = checkInDate.getValue();
                    LocalDate date2 = checkOutDate.getValue();

                    days = (int) ChronoUnit.DAYS.between(date1, date2);
                }
                total = Math.round(100.00 *(((adults + kids + pets) * room.getPricePerNight()) * days + room.getExtraFee())) / 100.00;
                if (checkInDate.getValue() != null && checkOutDate.getValue() != null) {
                    invoiceTotal.setText(String.valueOf(total));
                }
                return change; //if change is a number or if a deletion is being made
            } else {
                change.setText(""); //else make no change
                change.setRange(    //don't remove any selected text either.
                        change.getRangeStart(),
                        change.getRangeStart()
                );
                return change;
            }
        };
        numAdult.setTextFormatter(new TextFormatter<Object>(numberValidationFormatter));
        numKid.setTextFormatter(new TextFormatter<Object>(numberValidationFormatter));
        numPet.setTextFormatter(new TextFormatter<Object>(numberValidationFormatter));

        checkInDate.valueProperty().addListener(e -> {
            int adults = 0;
            int kids = 0;
            int pets = 0;
            int days = 0;
            double total = 0.00;

            if (numAdult.getText() != "") {
                adults = Integer.parseInt(numAdult.getText());
            }
            if (numKid.getText() != "") {
                kids = Integer.parseInt(numKid.getText());
            }
            if (numPet.getText() != "") {
                pets = Integer.parseInt(numPet.getText());
            }
            if (checkInDate.getValue() != null && checkOutDate.getValue() != null) {
                LocalDate date1 = checkInDate.getValue();
                LocalDate date2 = checkOutDate.getValue();
                days = (int) ChronoUnit.DAYS.between(date1, date2);
            }
            total = Math.round(100.00 *(((adults + kids + pets) * room.getPricePerNight()) * days + room.getExtraFee())) / 100.00;
            if (checkInDate.getValue() != null && checkOutDate.getValue() != null) {
                invoiceTotal.setText(String.valueOf(total));
            }
        });

        checkOutDate.valueProperty().addListener(e -> {
            int adults = 0;
            int kids = 0;
            int pets = 0;
            int days = 0;
            double total = 0.00;

            if (numAdult.getText() != "") {
                adults = Integer.parseInt(numAdult.getText());
            }
            if (numKid.getText() != "") {
                kids = Integer.parseInt(numKid.getText());
            }
            if (numPet.getText() != "") {
                pets = Integer.parseInt(numPet.getText());
            }
            if (checkInDate.getValue() != null && checkOutDate.getValue() != null) {
                LocalDate date1 = checkInDate.getValue();
                LocalDate date2 = checkOutDate.getValue();
                days = (int) ChronoUnit.DAYS.between(date1, date2);
            }
            total = Math.round(100.00 *(((adults + kids + pets) * room.getPricePerNight()) * days + room.getExtraFee())) / 100.00;
            if (checkInDate.getValue() != null && checkOutDate.getValue() != null) {
                invoiceTotal.setText(String.valueOf(total));
            }
        });
        
        //make sure the dialog returns a room if it's available
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveOrderRequest) {
                // Create the room order
                int orderID = RoomOrderDAO.insertRoomOrder(room.getId(), App.getUser(), 
                    Integer.valueOf(numAdult.getText()), Integer.valueOf(numKid.getText()), 
                    Integer.valueOf(numPet.getText()), Double.valueOf(invoiceTotal.getText()), 
                    checkInDate.getValue().format(DateTimeFormatter.ofPattern("MM/dd/yyyy")), 
                    checkOutDate.getValue().format(DateTimeFormatter.ofPattern("MM/dd/yyyy")), 
                    1 );

                // Create the message request
                MessageDAO.insertMessage(room.getId(), orderID, App.getUser(), room.getRoomOwner(), 
                    LocalDate.now().format(DateTimeFormatter.ofPattern("MM/dd/yyyy")), 
                    "NEW Room Guest found for your room \"" + roomName.getText() + "\"",
                    "Dear " + room.getRoomOwner() + ",\n" + 
                    App.getUser() + " wants to stay in your room \"" + roomName.getText() + "\"! Would you like to allow it?\n"
                    + "Room Order: " + orderID + "\n"
                    + "Number of Adults: " + numAdult.getText() + "\n"
                    + "Number of Kids: " + numKid.getText() + "\n"
                    + "Number of Pets: " + numPet.getText() + "\n"
                    + "Reservation Start Date: " + checkInDate.getValue().format(DateTimeFormatter.ofPattern("MM/dd/yyyy")) + "\n"
                    + "Reservation End Date: " + checkOutDate.getValue().format(DateTimeFormatter.ofPattern("MM/dd/yyyy")) + "\n"
                    + "Invoice Total: $" + invoiceTotal.getText(),
                    1, 0);
                
                // Return the room with the status change and room guest.
                return new Room(room.getId(), roomName.getText(), roomDescription.getText(), Double.valueOf(pricePerNight.getText()), 
                    Double.valueOf(extraFee.getText()), Integer.valueOf(numBed.getText()), Integer.valueOf(numBath.getText()),
                    startDate.getValue().format(DateTimeFormatter.ofPattern("MM/dd/yyyy")), 
                    endDate.getValue().format(DateTimeFormatter.ofPattern("MM/dd/yyyy")), 
                    address.getText(), address2.getText(), state.getText(), 
                    city.getText(), zip.getText(), 2, App.getUser(), room.getRoomOwner());
            }
            return null;
        });

        return dialog;
    }
}
