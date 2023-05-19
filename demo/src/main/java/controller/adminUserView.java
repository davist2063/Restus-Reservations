package controller;

import service.App;
import service.dao.UserDAO;
import models.User;

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
import java.util.Optional;
import java.util.function.UnaryOperator;

public class adminUserView {
    public TableView<User> userTable;
    public TableColumn<User, Integer> idColumn;
    public TableColumn<User, String> usernameColumn;
    public TableColumn<User, String> passwordColumn;
    public TableColumn<User, String> firstNameColumn;
    public TableColumn<User, String> lastNameColumn;
    public TableColumn<User, Integer> ageColumn;
    public TableColumn<User, Integer> accountTypeColumn;
    public Button editButton;
    public Button deleteButton;
    public Button switchButton;

    @FXML
    private void switchToSqLiteView() throws IOException {
        App.setRoot("/view/sqLiteView");
    }

    @FXML
    private void switchBackToMenu() throws IOException {
        App.setRoot("/view/menuPage");
    }

    public void initialize() {
        userTable.setItems(UserDAO.getUsers());

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));
        accountTypeColumn.setCellValueFactory(new PropertyValueFactory<>("accountType"));

        editButton.disableProperty().bind(Bindings.isEmpty(userTable.getSelectionModel().getSelectedItems()));
        deleteButton.disableProperty().bind(Bindings.isEmpty(userTable.getSelectionModel().getSelectedItems()));
    }

    public void handleExitButtonClicked(ActionEvent event) {
        Platform.exit();
        event.consume();
    }

    public void addUser(ActionEvent event) {
        Dialog<User> addUserDialog = createUserDialog(null);
        Optional<User> result = addUserDialog.showAndWait();
        result.ifPresent(user ->
                UserDAO.insertUser(
                    user.getUsername(),
                    user.getPassword(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getAge(),
                    user.getAccountType(),
                    user.getEmail(),
                    user.getPhoneNumber()
                ));
        event.consume();
    }

    public void deleteUser(ActionEvent event) {
        for (User user : userTable.getSelectionModel().getSelectedItems()) {
            UserDAO.delete(user.getId());
            if(userTable.getSelectionModel().getSelectedItems().size() == 0) {
                break;
            }
        }
        event.consume();
    }

    public void editUser(ActionEvent event) {
        if (userTable.getSelectionModel().getSelectedItems().size() != 1) {
            System.out.println("Error: User editing error, One user must be selected before editing");
        }
        else {
            Dialog<User> dialog = createUserDialog(userTable.getSelectionModel().getSelectedItem());
            Optional<User> optionalUser = dialog.showAndWait();
            optionalUser.ifPresent(UserDAO::update);
        }
        event.consume();
    }

    private Dialog<User> createUserDialog(User user) {
        //create the dialog itself
        Dialog<User> dialog = new Dialog<>();
        dialog.setTitle("Add Dialog");
        if(user==null){
            dialog.setHeaderText("Add a new user to the database");
        } else {
            dialog.setHeaderText("Edit a database record");
        }
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        Stage dialogWindow = (Stage) dialog.getDialogPane().getScene().getWindow();

        //create the form for the user to fill in
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        TextField username = new TextField();
        username.setPromptText("Username");
        TextField password = new TextField();
        password.setPromptText("Password");
        TextField firstName = new TextField();
        firstName.setPromptText("First Name");
        TextField lastName = new TextField();
        lastName.setPromptText("Last Name");
        TextField age = new TextField();
        age.setPromptText("Age");
        TextField accountType = new TextField();
        accountType.setPromptText("Account Type");
        TextField email = new TextField();
        email.setPromptText("Email");
        TextField phoneNumber = new TextField();
        phoneNumber.setPromptText("Phone Number");
        grid.add(new Label("Username:"), 0, 0);
        grid.add(username, 1, 0);
        grid.add(new Label("Password:"), 0, 1);
        grid.add(password, 1, 1);
        grid.add(new Label("First Name:"), 0, 2);
        grid.add(firstName, 1, 2);
        grid.add(new Label("Last Name:"), 0, 3);
        grid.add(lastName, 1, 3);
        grid.add(new Label("Age:"), 0, 4);
        grid.add(age, 1, 4);
        grid.add(new Label("Account Type:"), 0, 5);
        grid.add(accountType, 1, 5);
        grid.add(new Label("Email:"), 0, 6);
        grid.add(email, 1, 6);
        grid.add(new Label("Phone Number:"), 0, 7);
        grid.add(phoneNumber, 1, 7);
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
        accountType.setTextFormatter(new TextFormatter<Object>(numberValidationFormatter));
        phoneNumber.setTextFormatter(new TextFormatter<Object>(numberValidationFormatter));

        //make sure the dialog returns a user if it's available
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                int id = -1;
                if (user != null) id = user.getId();
                return new User(id, username.getText(), password.getText(), firstName.getText(), lastName.getText(), Integer.valueOf(age.getText()), Integer.valueOf(accountType.getText()), email.getText(), Long.valueOf(phoneNumber.getText()));
            }
            return null;
        });

        //if a record is supplied, use it to fill in the fields automatically
        if (user != null) {
            username.setText(user.getUsername());
            password.setText(user.getPassword());
            firstName.setText(user.getFirstName());
            lastName.setText(user.getLastName());
            age.setText(String.valueOf(user.getAge()));
            accountType.setText(String.valueOf(user.getAccountType()));
            email.setText(user.getEmail());
            phoneNumber.setText(String.valueOf(user.getPhoneNumber()));
        }

        return dialog;
    }
}
