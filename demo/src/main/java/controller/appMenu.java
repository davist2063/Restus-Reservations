package controller;

import java.io.IOException;
import java.util.Optional;
import java.util.function.UnaryOperator;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import models.User;
import service.App;
import service.dao.UserDAO;

public class appMenu {

    @FXML
    private Label welcomeLabel;

    @FXML
    private Button adminButton;

    @FXML
    private void initialize() {
        welcomeLabel.setText("Welcome " + App.getUser() + "!");
        adminButton.setVisible(UserDAO.isAdmin(App.getUser()));
    }

    @FXML
    private void logOut(ActionEvent event) throws IOException {
        App.setUser("");
        App.setRoot("/view/loginPage");
    }

    @FXML
    private void switchToAdminUserView(ActionEvent event) throws IOException {
        App.setRoot("/view/userView");
    }

    @FXML
    private void switchToMessageView(ActionEvent event) throws IOException {
        App.setRoot("/view/messageListView");
    }
    
    @FXML
    private void switchToMyRoomView(ActionEvent event) throws IOException {
        App.setRoot("/view/myRoomView", "MyRoom");
    }

    @FXML
    private void switchToFindRoomView(ActionEvent event) throws IOException {
        App.setRoot("/view/findRoomView", "FindRoom");
    }

    @FXML
    private void switchToFindReservationListView(ActionEvent event) throws IOException {
        App.setRoot("/view/reservationListView");
    }

    public void handleExitButtonClicked(ActionEvent event) {
        Platform.exit();
        event.consume();
    }

    public void editUser(ActionEvent event) {
        Dialog<User> dialog = editUserDialog();
        Optional<User> optionalUser = dialog.showAndWait();
        optionalUser.ifPresent(UserDAO::update);
        event.consume();
    }

    private Dialog<User> editUserDialog() {
        User user = UserDAO.getUser(App.getUser()).get();

        //create the dialog itself
        Dialog<User> dialog = new Dialog<>();
        dialog.setTitle("User Settings");
        dialog.setHeaderText("Change User Settings");

        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        Stage dialogWindow = (Stage) dialog.getDialogPane().getScene().getWindow();

        //create the form for the user to fill in
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        TextField password = new TextField();
        password.setPromptText("Password");
        TextField firstName = new TextField();
        firstName.setPromptText("First Name");
        TextField lastName = new TextField();
        lastName.setPromptText("Last Name");
        TextField age = new TextField();
        age.setPromptText("Age");
        TextField email = new TextField();
        email.setPromptText("Email");
        TextField phoneNumber = new TextField();
        phoneNumber.setPromptText("Phone Number");
        grid.add(new Label("Password:"), 0, 0);
        grid.add(password, 1, 0);
        grid.add(new Label("First Name:"), 0, 1);
        grid.add(firstName, 1, 1);
        grid.add(new Label("Last Name:"), 0, 2);
        grid.add(lastName, 1, 2);
        grid.add(new Label("Age:"), 0, 3);
        grid.add(age, 1, 3);
        grid.add(new Label("Email:"), 0, 4);
        grid.add(email, 1, 4);
        grid.add(new Label("Phone Number:"), 0, 5);
        grid.add(phoneNumber, 1, 5);
        dialog.getDialogPane().setContent(grid);


        //disable the OK button if the fields haven't been filled in
        dialog.getDialogPane().lookupButton(ButtonType.OK).disableProperty().bind(
                Bindings.createBooleanBinding(() -> firstName.getText().trim().isEmpty(), firstName.textProperty())
                        .or(Bindings.createBooleanBinding(() -> lastName.getText().trim().isEmpty(), lastName.textProperty())
                                .or(Bindings.createBooleanBinding(() -> age.getText().trim().isEmpty(), age.textProperty())
                                )));

        //ensure only numeric input (integers) in age text field
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
        age.setTextFormatter(new TextFormatter<Object>(numberValidationFormatter));
        phoneNumber.setTextFormatter(new TextFormatter<Object>(numberValidationFormatter));

        //make sure the dialog returns a user if it's available
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                int id = -1;
                if (user != null) id = user.getId();
                return new User(id, user.getUsername(), password.getText(), firstName.getText(), lastName.getText(), Integer.valueOf(age.getText()), user.getAccountType(), email.getText(), Long.valueOf(phoneNumber.getText()));
            }
            return null;
        });

        //if a record is supplied, use it to fill in the fields automatically
        if (user != null) {
            password.setText(user.getPassword());
            firstName.setText(user.getFirstName());
            lastName.setText(user.getLastName());
            age.setText(String.valueOf(user.getAge()));
            email.setText(user.getEmail());
            phoneNumber.setText(String.valueOf(user.getPhoneNumber()));
        }

        return dialog;
    }
}
