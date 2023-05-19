package service.dao;

import models.User;
import service.App;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserDAO {
    private static final String tableName = "Users";
    private static final String idColumn = "id";
    private static final String usernameColumn = "username";
    private static final String passwordColumn = "password";
    private static final String firstNameColumn = "firstName";
    private static final String lastNameColumn = "lastName";
    private static final String ageColumn = "age";
    private static final String accountTypeColumn = "accountType";
    private static final String emailColumn = "email";
    private static final String phoneNumberColumn = "phoneNumber";

    private static final ObservableList<User> users;

    static {
        users = FXCollections.observableArrayList();
        updateUsersFromDB();
    }

    public static boolean isValidUserLogin(String username, String password) {
        boolean result = false;
        User user;
        Optional<User> userOptional = getUser(username);
        if(userOptional.isPresent()) {
            user = userOptional.get();
        }
        else {
            return result;
        }

        String query = "SELECT COUNT(1) FROM " + tableName 
            + " WHERE USERNAME = '" + user.getUsername() 
            + "' AND PASSWORD = '" + password + "'";
        try (Connection connection = Database.connect()) {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            while(rs.next()) {
                if (rs.getInt(1) > 0) {
                    result = true;
                    break;
                }
                else {
                    result = false;
                    break;
                }
            }
        }
        catch (SQLException e) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ": Could not perform authentication");
        }
        return result;
    }

    public static boolean isAdmin(String username) {
        boolean result = false;
        String query = "SELECT ACCOUNTTYPE FROM " + tableName 
            + " WHERE USERNAME = '" + App.getUser() + "'";
        try (Connection connection = Database.connect()) {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            while(rs.next()) {
                if (rs.getInt(1) == 1) {
                    result = true;
                    break;
                }
                else {
                    result = false;
                    break;
                }
            }
        }
        catch (SQLException e) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ": Could not perform authentication");
        }
        return result;
    }

    public static ObservableList<User> getUsers() {
        return FXCollections.unmodifiableObservableList(users);
    }

    private static void updateUsersFromDB() {
        String query = "SELECT * FROM " + tableName;

        try (Connection connection = Database.connect()) {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            users.clear();
            while (rs.next()) {
                users.add(new User(
                        rs.getInt(idColumn),
                        rs.getString(usernameColumn),
                        rs.getString(passwordColumn),
                        rs.getString(firstNameColumn),
                        rs.getString(lastNameColumn),
                        rs.getInt(ageColumn),
                        rs.getInt(accountTypeColumn),
                        rs.getString(emailColumn),
                        rs.getLong(phoneNumberColumn)));
            }
        } catch (SQLException e) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ": Could not load Users from database ");
            users.clear();
            return;
        }
    }

    public static void update(User newUser) {
        //update database
        long rows = CRUDHelper.update(
            tableName,
            new String[]{
                usernameColumn, 
                passwordColumn, 
                firstNameColumn, 
                lastNameColumn, 
                ageColumn, 
                accountTypeColumn,
                emailColumn,
                phoneNumberColumn},
            new Object[]{
                newUser.getUsername(), 
                newUser.getPassword(), 
                newUser.getFirstName(), 
                newUser.getLastName(), 
                newUser.getAge(), 
                newUser.getAccountType(),
                newUser.getEmail(),
                newUser.getPhoneNumber()},
            new int[]{
                Types.VARCHAR, 
                Types.VARCHAR, 
                Types.VARCHAR, 
                Types.VARCHAR, 
                Types.INTEGER, 
                Types.INTEGER,
                Types.VARCHAR,
                Types.INTEGER},
            idColumn,
            Types.INTEGER,
            newUser.getId()
        );

        if (rows == 0)
            throw new IllegalStateException("User to be updated with id " 
                + newUser.getId() + " didn't exist in database");
        
        //update cache
        Optional<User> optionalUser = getUser(newUser.getId());
        optionalUser.ifPresentOrElse((oldUser) -> {
            users.remove(oldUser);
            users.add(newUser);
        }, () -> {
            throw new IllegalStateException("User to be updated with id " 
                + newUser.getId() + " didn't exist in database");
        });
        return;
    }

    public static void insertUser(String username, String password, 
        String firstName, String lastName, int age, int accountType, 
        String email, long phoneNumber) {
        //update database
        int id = (int) CRUDHelper.create(
            tableName, 
            new String[]{
                usernameColumn, 
                passwordColumn, 
                firstNameColumn, 
                lastNameColumn, 
                ageColumn, 
                accountTypeColumn,
                emailColumn,
                phoneNumberColumn},
            new Object[]{
                username, 
                password, 
                firstName, 
                lastName, 
                age, 
                accountType,
                email,
                phoneNumber},
            new int[]{
                Types.VARCHAR, 
                Types.VARCHAR, 
                Types.VARCHAR, 
                Types.VARCHAR, 
                Types.INTEGER, 
                Types.INTEGER,
                Types.VARCHAR,
                Types.BIGINT});

        //update cache
        users.add(new User(
            id,
            username,
            password,
            firstName,
            lastName,
            age,
            accountType,
            email,
            phoneNumber
        ));
    }

    public static void delete(int id) {
        //update database
        CRUDHelper.delete(tableName, id);

        //update cache
        Optional<User> user = getUser(id);
        user.ifPresent(users::remove);
    }

    public static Optional<User> getUser(int id) {
        for (User user : users) {
            if (user.getId() == id) return Optional.of(user);
        }
        return Optional.empty();
    }

    public static Optional<User> getUser(String username) {
        for (User user : users) {
            if (user.getUsername().equalsIgnoreCase(username)) {
                App.setUser(user.getUsername());
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }
}
