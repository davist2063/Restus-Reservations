package service.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.ReservationView;
import service.App;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ReservationViewDAO {
    private static final String tableName = "ReservationView";
    private static final String orderIdColumn = "orderId";
    private static final String roomColumn = "room";
    private static final String reservationStart = "reservationStart";
    private static final String reservationEnd = "reservationEnd";
    private static final String reservationStatus = "reservationStatus";
    private static final String roomOrderStatus = "roomOrderStatus";

    private static final ObservableList<ReservationView> reservations;

    static {
        reservations = FXCollections.observableArrayList();
        updateReservationViewFromDB();
    }

    public static ObservableList<ReservationView> getReservations() {
        updateReservationViewFromDB();
        return FXCollections.unmodifiableObservableList(reservations);
    }

    private static void updateReservationViewFromDB() {
        String query = "SELECT * FROM " + tableName + " WHERE " + tableName + ".RoomGuest='" + App.getUser() + "'";
        // String query = "SELECT * FROM " + tableName;

        try (Connection connection = Database.connect()) {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            reservations.clear();
            while (rs.next()) {
                reservations.add(new ReservationView(
                        rs.getInt(orderIdColumn),
                        rs.getString(roomColumn),
                        rs.getString(reservationStart),
                        rs.getString(reservationEnd),
                        rs.getInt(reservationStatus),
                        rs.getInt(roomOrderStatus)));
            }
        } catch (SQLException e) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ": Could not load reservations from database view");
            reservations.clear();
            return;
        }
    }

    public static Optional<ReservationView> getReservation(int id) {
        for (ReservationView reservation : reservations) {
            if (reservation.getOrderId() == id) return Optional.of(reservation);
        }
        return Optional.empty();
    }
}
