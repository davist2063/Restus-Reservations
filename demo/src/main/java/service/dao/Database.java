package service.dao;

import service.App;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Database {
    /**
     * Location of database
     */
    private static final String location = App.class.getResource("database/database.db").toExternalForm();

    /**
     * Currently only table needed
     */
    private static final String requiredTable = "Persons";

    public static boolean isOK() {
        if (!checkDrivers()) return false; //driver errors
        System.out.println(checkDrivers());

        if (!checkConnection()) return false; //can't connect to db
        System.out.println(checkConnection());

        return checkTables(); //tables didn't exist
    }

    private static boolean checkDrivers() {
        try {
            Class.forName("org.sqlite.JDBC");
            DriverManager.registerDriver(new org.sqlite.JDBC());
            return true;
        } catch (ClassNotFoundException | SQLException classNotFoundException) {
            Logger.getAnonymousLogger().log(Level.SEVERE, LocalDateTime.now() + ": Could not start SQLite Drivers");
            return false;
        }
    }

    private static boolean checkConnection() {
        try (Connection connection = connect()) {
            return connection != null;
        } catch (SQLException e) {
            Logger.getAnonymousLogger().log(Level.SEVERE, LocalDateTime.now() + ": Could not connect to database");
            return false;
        }
    }

    private static boolean checkTables() {
        String checkTables =
                "select DISTINCT tbl_name from sqlite_master where tbl_name = '" + requiredTable + "'";

        try (Connection connection = Database.connect()) {
            PreparedStatement statement = connection.prepareStatement(checkTables);
            ResultSet rs = statement.executeQuery();
            return true;
            // while (rs.next()) {
            //     if (rs.getString("tbl_name").equals(requiredTable)) return true;
            // }
        } catch (SQLException exception) {
            Logger.getAnonymousLogger().log(Level.SEVERE, LocalDateTime.now() + ": Could not find tables in database");
            return false;
        }
        // return true;
    }

    protected static Connection connect() {
        String dbPrefix = "jdbc:sqlite:";
        Connection connection;
        try {
            connection = DriverManager.getConnection(dbPrefix + location);
        } catch (SQLException exception) {
            Logger.getAnonymousLogger().log(Level.SEVERE,
                    LocalDateTime.now() + ": Could not connect to SQLite DB at " +
                            location);
            return null;
        }
        return connection;
    }
}
