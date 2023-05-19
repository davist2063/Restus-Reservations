package models;

import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.ReadOnlyLongProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleLongProperty;

public class User {
    private final int id;
    private final ReadOnlyStringProperty username;
    private final ReadOnlyStringProperty password;
    private final ReadOnlyStringProperty firstName;
    private final ReadOnlyStringProperty lastName;
    private final ReadOnlyIntegerProperty age;
    private final ReadOnlyIntegerProperty accountType;
    private final ReadOnlyStringProperty email;
    private final ReadOnlyLongProperty phoneNumber;

    public User(int id, String username, String password, String firstName, String lastName, Integer age, Integer accountType, String email, Long phoneNumber) {
        this.id = id;
        this.username = new SimpleStringProperty(username);
        this.password = new SimpleStringProperty(password);
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.age = new SimpleIntegerProperty(age);
        this.accountType = new SimpleIntegerProperty(accountType);
        this.email = new SimpleStringProperty(email);
        if (phoneNumber != null) {
            this.phoneNumber = new SimpleLongProperty(phoneNumber);
        }
        else {
            this.phoneNumber = new SimpleLongProperty(0);
        }
        
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username.get();
    }

    public ReadOnlyStringProperty usernameProperty() {
        return username;
    }

    public String getPassword() {
        return password.get();
    }

    public ReadOnlyStringProperty passwordProperty() {
        return password;
    }

    public String getFirstName() {
        return firstName.get();
    }

    public ReadOnlyStringProperty firstNameProperty() {
        return firstName;
    }

    public String getLastName() {
        return lastName.get();
    }

    public ReadOnlyStringProperty lastNameProperty() {
        return lastName;
    }

    public int getAge() {
        return age.get();
    }

    public ReadOnlyIntegerProperty ageProperty() {
        return age;
    }

    public int getAccountType() {
        return accountType.get();
    }

    public ReadOnlyIntegerProperty accountTypeProperty() {
        return accountType;
    }

    public String getEmail() {
        return email.get();
    }

    public ReadOnlyStringProperty emailProperty() {
        return email;
    }

    public long getPhoneNumber() {
        return phoneNumber.get();
    }

    public ReadOnlyLongProperty phoneNumberProperty() {
        return phoneNumber;
    }

    @Override
    public String toString() {
        return "User [" + username.get() + " " + password.get() + " " + firstName.get() + " " + lastName.get() + ", aged " + age.get() + " with id " + id + " and account type: " + accountType.get() + "]";
    }
}
