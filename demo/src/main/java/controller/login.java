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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import models.User;
import service.App;
import service.dao.UserDAO;

public class login {

    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField passwordTextField;
    @FXML
    private Label loginMessageLabel;
    @FXML
    private Button loginButton;
    @FXML
    private Button createNewAccountButton;

    @FXML
    private void logIn(String user) throws IOException {
        App.setRoot("/view/menuPage");
    }

    public void loginButtonOnAction(ActionEvent e) throws IOException {
        
        if(usernameTextField.getText().isBlank() == false && passwordTextField.getText().isBlank() == false) {
            boolean result = UserDAO.isValidUserLogin(usernameTextField.getText(), passwordTextField.getText());
            if(result) {
                loginMessageLabel.setText("Logged In");
                this.logIn(usernameTextField.getText());
                System.out.println(App.getUser());
            }
            else {
                loginMessageLabel.setText("Wrong username or password");
                App.setUser("");
            }
        }
        else {
            loginMessageLabel.setText("Enter a Username and Password");
        }
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

    private Dialog<User> createUserDialog(User user) {
        //create the dialog itself
        Dialog<User> dialog = new Dialog<>();
        dialog.setTitle("New User");
        dialog.setHeaderText("Create a new Account");
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
        grid.add(new Label("Email:"), 0, 5);
        grid.add(email, 1, 5);
        grid.add(new Label("Phone Number:"), 0, 6);
        grid.add(phoneNumber, 1, 6);
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
                return new User(id, username.getText(), password.getText(), firstName.getText(), lastName.getText(), Integer.valueOf(age.getText()), Integer.valueOf(0), email.getText(), Long.valueOf(phoneNumber.getText()));
            }
            return null;
        });

        return dialog;
    }
}
