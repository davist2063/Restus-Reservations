package service.dao;

import models.RoomOrder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RoomOrderDAO {
    private static final String tableName = "RoomOrders";
    private static final String idColumn = "id";
    private static final String roomIdColumn = "roomId";
    private static final String roomGuestColumn = "roomGuest";
    private static final String numAdultColumn = "numAdult";
    private static final String numKidColumn = "numKid";
    private static final String numPetColumn = "numPet";
    private static final String invoiceTotalColumn = "invoiceTotal";
    private static final String checkInDateColumn = "checkInDate";
    private static final String checkOutDateColumn = "checkOutDate";
    private static final String roomOrderStatusColumn = "roomOrderStatus";

    private static final ObservableList<RoomOrder> roomOrders;

    static {
        roomOrders = FXCollections.observableArrayList();
        updateRoomOrdersFromDB();
    }

    public static ObservableList<RoomOrder> getRoomOrders() {
        return FXCollections.unmodifiableObservableList(roomOrders);
    }

    private static void updateRoomOrdersFromDB() {
        String query = "SELECT * FROM " + tableName;

        try (Connection connection = Database.connect()) {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            roomOrders.clear();
            while (rs.next()) {
                roomOrders.add(new RoomOrder(
                        rs.getInt(idColumn),
                        rs.getInt(roomIdColumn),
                        rs.getString(roomGuestColumn),
                        rs.getInt(numAdultColumn),
                        rs.getInt(numKidColumn),
                        rs.getInt(numPetColumn),
                        rs.getDouble(invoiceTotalColumn),
                        rs.getString(checkInDateColumn),
                        rs.getString(checkOutDateColumn),
                        rs.getInt(roomOrderStatusColumn)
                ));
            }
        } catch (SQLException e) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ": Could not load room orders from database ");
            roomOrders.clear();
            return;
        }
    }

    public static void update(RoomOrder newRoomOrder) {
        //update database
        long rows = CRUDHelper.update(
            tableName, 
            new String[] {
                idColumn,
                roomIdColumn,
                roomGuestColumn,
                numAdultColumn,
                numKidColumn,
                numPetColumn,
                invoiceTotalColumn,
                checkInDateColumn,
                checkOutDateColumn,
                roomOrderStatusColumn
            }, 
            new Object[] {
                newRoomOrder.getId(),
                newRoomOrder.getRoomId(),
                newRoomOrder.getRoomGuest(),
                newRoomOrder.getNumAdult(),
                newRoomOrder.getNumKid(),
                newRoomOrder.getNumPet(),
                newRoomOrder.getInvoiceTotal(),
                newRoomOrder.getCheckInDate(),
                newRoomOrder.getCheckOutDate(),
                newRoomOrder.getRoomOrderStatus(),
            }, 
            new int[] {
                Types.INTEGER,
                Types.INTEGER,
                Types.VARCHAR,
                Types.INTEGER,
                Types.INTEGER,
                Types.INTEGER,
                Types.DOUBLE,
                Types.VARCHAR,
                Types.VARCHAR,
                Types.INTEGER,
            }, 
            idColumn, 
            Types.INTEGER,
            newRoomOrder.getId()
        );
        
        if (rows == 0)
            throw new IllegalStateException("Room Order to be updated with id " 
                + newRoomOrder.getId() + " didn't exist in database");

        //update cache
        Optional<RoomOrder> optionalRoomOrder = getRoomOrder(newRoomOrder.getId());
        optionalRoomOrder.ifPresentOrElse((oldRoomOrder) -> {
            roomOrders.remove(oldRoomOrder);
            roomOrders.add(newRoomOrder);
        }, () -> {
            throw new IllegalStateException("Room order to be updated with id " 
                + newRoomOrder.getId() + " didn't exist in database");
        });
        return;
    }

    public static int insertRoomOrder(int roomId, String roomGuest, 
        Integer numAdult, Integer numKid, Integer numPet, Double invoiceTotal,
        String checkInDate, String checkOutDate, Integer roomOrderStatus) {
        //update database
        int id = (int) CRUDHelper.create(
            tableName,
            new String[] {
                roomIdColumn,
                roomGuestColumn,
                numAdultColumn,
                numKidColumn,
                numPetColumn,
                invoiceTotalColumn,
                checkInDateColumn,
                checkOutDateColumn,
                roomOrderStatusColumn
            }, 
            new Object[] {
                roomId, 
                roomGuest, 
                numAdult, 
                numKid, 
                numPet, 
                invoiceTotal, 
                checkInDate, 
                checkOutDate, 
                roomOrderStatus
            }, 
            new int[] {
                Types.INTEGER,
                Types.VARCHAR,
                Types.INTEGER,
                Types.INTEGER,
                Types.INTEGER,
                Types.DOUBLE,
                Types.VARCHAR,
                Types.VARCHAR,
                Types.INTEGER,
            } 
        );

        //update cache
        roomOrders.add(new RoomOrder(
            id, 
            roomId, 
            roomGuest, 
            numAdult, 
            numKid, 
            numPet, 
            invoiceTotal, 
            checkInDate, 
            checkOutDate, 
            roomOrderStatus
        ));

        return id;
    }

    public static void delete(int id) {
        //update database
        CRUDHelper.delete(tableName, id);

        //update cache
        Optional<RoomOrder> roomOrder = getRoomOrder(id);
        roomOrder.ifPresent(roomOrders::remove);
    }

    public static Optional<RoomOrder> getRoomOrder(int id) {
        for (RoomOrder roomOrder : roomOrders) {
            if (roomOrder.getId() == id) return Optional.of(roomOrder);
        }
        return Optional.empty();
    }
}
