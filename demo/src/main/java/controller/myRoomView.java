package controller;

import service.App;
import service.dao.RoomDAO;
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
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.function.UnaryOperator;

public class myRoomView {    
    public TableView<Room> roomTable;
    public TableColumn<Room, String> roomNameColumn;
    public TableColumn<Room, Double> pricePerNightColumn;
    public TableColumn<Room, String> startDateColumn;
    public TableColumn<Room, String> endDateColumn;
    public TableColumn<Room, String> roomStatusColumn;
    
    public Button editButton;
    public Button deleteButton;
    public Button switchButton;

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
        roomStatusColumn.setCellValueFactory(new PropertyValueFactory<>("myRoomStatusString"));
        
        editButton.disableProperty().bind(Bindings.isEmpty(roomTable.getSelectionModel().getSelectedItems()));
        deleteButton.disableProperty().bind(Bindings.isEmpty(roomTable.getSelectionModel().getSelectedItems()));
    }

    public void handleExitButtonClicked(ActionEvent event) {
        Platform.exit();
        event.consume();
    }

    @FXML
    public void addRoom(ActionEvent event) {
        Dialog<Room> addRoomDialog = createRoomDialog(null);
        Optional<Room> result = addRoomDialog.showAndWait();
        result.ifPresent(room ->
                RoomDAO.insertRoom(
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
                    room.getRoomStatus(),
                    room.getRoomGuest(),
                    room.getRoomOwner()
                ));
        event.consume();
    }

    @FXML
    public void deleteRoom(ActionEvent event) {
        for (Room room : roomTable.getSelectionModel().getSelectedItems()) {
            RoomDAO.delete(room.getId());
            if(roomTable.getSelectionModel().getSelectedItems().size() == 0) {
                break;
            }
        }
        event.consume();
    }

    @FXML
    public void editRoom(ActionEvent event) {
        if (roomTable.getSelectionModel().getSelectedItems().size() != 1) {
            System.out.println("Error: Room editing error, One room must be selected before editing");
        }
        else {
            Dialog<Room> dialog = createRoomDialog(roomTable.getSelectionModel().getSelectedItem());
            Optional<Room> optionalRoom = dialog.showAndWait();
            optionalRoom.ifPresent(RoomDAO::update);
        }
        event.consume();
    }

    private Dialog<Room> createRoomDialog(Room room) {
        //create the dialog itself
        Dialog<Room> dialog = new Dialog<>();
        if(room==null){
            dialog.setTitle("Create Room");
            dialog.setHeaderText("Add a new room for bookings");
        } else {
            dialog.setTitle("Edit Room");
            dialog.setHeaderText("Edit a room for bookings");
        }
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        Stage dialogWindow = (Stage) dialog.getDialogPane().getScene().getWindow();

        //create the form for the user to fill in
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        TextField roomName = new TextField();
        roomName.setPromptText("Room Name");
        TextArea roomDescription = new TextArea();
        roomDescription.setPromptText("Room Description");
        TextField pricePerNight = new TextField();
        pricePerNight.setPromptText("Price Per Night");
        TextField extraFee = new TextField();
        extraFee.setPromptText("Extra Fee");
        TextField numBed = new TextField();
        numBed.setPromptText("Number of Bedrooms");
        TextField numBath = new TextField();
        numBath.setPromptText("Number of Bathrooms");
        DatePicker startDate = new DatePicker();
        startDate.setPromptText("Start Date");
        DatePicker endDate = new DatePicker();
        endDate.setPromptText("End Date");
        TextField address = new TextField();
        address.setPromptText("Address");
        TextField address2 = new TextField();
        address2.setPromptText("Address Line 2");
        TextField state = new TextField();
        state.setPromptText("State");
        TextField city = new TextField();
        city.setPromptText("City");
        TextField zip = new TextField();
        zip.setPromptText("ZIP Code");

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
        dialog.getDialogPane().setContent(grid);

        //disable the OK button if the fields haven't been filled in
        dialog.getDialogPane().lookupButton(ButtonType.OK).disableProperty().bind(
                Bindings.createBooleanBinding(() -> roomName.getText().trim().isEmpty(), roomName.textProperty())
                    .or(Bindings.createBooleanBinding(() -> roomDescription.getText().trim().isEmpty(), roomDescription.textProperty())
                    .or(Bindings.createBooleanBinding(() -> pricePerNight.getText().trim().isEmpty(), pricePerNight.textProperty())
                    .or(Bindings.createBooleanBinding(() -> numBed.getText().trim().isEmpty(), numBed.textProperty())
                    .or(Bindings.createBooleanBinding(() -> numBath.getText().trim().isEmpty(), numBath.textProperty())
                    .or(Bindings.createBooleanBinding(() -> startDate.getPromptText().trim().isEmpty(), startDate.promptTextProperty())
                    .or(Bindings.createBooleanBinding(() -> endDate.getPromptText().trim().isEmpty(), endDate.promptTextProperty())
                    .or(Bindings.createBooleanBinding(() -> address.getText().trim().isEmpty(), address.textProperty())
                    .or(Bindings.createBooleanBinding(() -> state.getText().trim().isEmpty(), state.textProperty())
                    .or(Bindings.createBooleanBinding(() -> city.getText().trim().isEmpty(), city.textProperty())
                    .or(Bindings.createBooleanBinding(() -> zip.getText().trim().isEmpty(), zip.textProperty())
        )))))))))));

        //ensure only numeric input (integers) in text field
        UnaryOperator<TextFormatter.Change> numberValidationFormatter = change -> {
            if (change.getText().matches("\\d+") || change.getText().equals("")) {
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
        numBed.setTextFormatter(new TextFormatter<Object>(numberValidationFormatter));
        numBath.setTextFormatter(new TextFormatter<Object>(numberValidationFormatter));
        
        //ensure only numeric input (decimal) in text field
        UnaryOperator<TextFormatter.Change> decimalValidationFormatter = change -> {
            if (change.getText().matches("^\\d*\\.?\\d*$") || change.getText().equals("")) {
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
        pricePerNight.setTextFormatter(new TextFormatter<Object>(decimalValidationFormatter));
        extraFee.setTextFormatter(new TextFormatter<Object>(decimalValidationFormatter));

        //make sure the dialog returns a user if it's available
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                int id = -1;
                if (room != null) id = room.getId();
                return new Room(id, roomName.getText(), roomDescription.getText(), Double.valueOf(pricePerNight.getText()), 
                    Double.valueOf(extraFee.getText()), Integer.valueOf(numBed.getText()), Integer.valueOf(numBath.getText()),
                    startDate.getValue().format(DateTimeFormatter.ofPattern("MM/dd/yyyy")), 
                    endDate.getValue().format(DateTimeFormatter.ofPattern("MM/dd/yyyy")), 
                    address.getText(), address2.getText(), state.getText(), 
                    city.getText(), zip.getText(), 0, "", App.getUser());
            }
            return null;
        });

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
        }

        return dialog;
    }
}
