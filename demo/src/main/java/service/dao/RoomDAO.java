package service.dao;

import models.Room;
import service.App;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RoomDAO {
    private static final String tableName = "Rooms";
    private static final String idColumn = "id";
    private static final String roomNameColumn = "roomName";
    private static final String roomDescriptionColumn = "roomDescription";
    private static final String pricePerNightColumn = "pricePerNight";
    private static final String extraFeeColumn = "extraFee";
    private static final String numBedColumn = "numBed";
    private static final String numBathColumn = "numBath";
    private static final String startDateColumn = "startDate";
    private static final String endDateColumn = "endDate";
    private static final String addressColumn = "address";
    private static final String address2Column = "address2";
    private static final String stateColumn = "state";
    private static final String cityColumn = "city";
    private static final String zipColumn = "zip";
    private static final String roomStatusColumn = "roomStatus";
    private static final String roomGuestColumn = "roomGuest";
    private static final String roomOwnerColumn = "roomOwner";

    private static final ObservableList<Room> rooms;

    static {
        rooms = FXCollections.observableArrayList();
        updateRoomsFromDB();
    }

    public static ObservableList<Room> getRooms() {
        updateRoomsFromDB();
        return FXCollections.unmodifiableObservableList(rooms);
    }

    private static void updateRoomsFromDB() {
        String query = "SELECT * FROM " + tableName;

        if (App.getPage().equalsIgnoreCase("MyRoom")) {
            query = query + " WHERE " + tableName + ".RoomOwner='" + App.getUser() + "'";
        }
        else if (App.getPage().equalsIgnoreCase("FindRoom")) {
            query = query + " WHERE NOT " + tableName + ".RoomOwner='" + App.getUser() + "'";
        }

        try (Connection connection = Database.connect()) {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            rooms.clear();
            while (rs.next()) {
                rooms.add(new Room(
                    rs.getInt(idColumn),
                    rs.getString(roomNameColumn),
                    rs.getString(roomDescriptionColumn),
                    rs.getDouble(pricePerNightColumn),
                    rs.getDouble(extraFeeColumn),
                    rs.getInt(numBedColumn),
                    rs.getInt(numBathColumn),
                    rs.getString(startDateColumn),
                    rs.getString(endDateColumn),
                    rs.getString(addressColumn),
                    rs.getString(address2Column),
                    rs.getString(stateColumn),
                    rs.getString(cityColumn),
                    rs.getString(zipColumn),
                    rs.getInt(roomStatusColumn),
                    rs.getString(roomGuestColumn),
                    rs.getString(roomOwnerColumn)));
            }
        } catch (SQLException e) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ": Could not load Rooms from database ");
            rooms.clear();
            return;
        }
    }

    public static void update(Room newRoom) {
        //update database
        long rows = CRUDHelper.update(
            tableName,
            new String[] {
                roomNameColumn,
                roomDescriptionColumn,
                pricePerNightColumn,
                extraFeeColumn,
                numBedColumn,
                numBathColumn,
                startDateColumn,
                endDateColumn,
                addressColumn,
                address2Column,
                stateColumn,
                cityColumn,
                zipColumn,
                roomStatusColumn,
                roomGuestColumn,
                roomOwnerColumn
            },
            new Object[] {
                newRoom.getRoomName(),
                newRoom.getRoomDescription(),
                newRoom.getPricePerNight(),
                newRoom.getExtraFee(),
                newRoom.getNumBed(),
                newRoom.getNumBath(),
                newRoom.getStartDate(),
                newRoom.getEndDate(),
                newRoom.getAddress(),
                newRoom.getAddress2(),
                newRoom.getState(),
                newRoom.getCity(),
                newRoom.getZip(),
                newRoom.getRoomStatus(),
                newRoom.getRoomGuest(),
                newRoom.getRoomOwner()
            },
            new int[] {
                Types.VARCHAR,
                Types.VARCHAR,
                Types.DOUBLE,
                Types.DOUBLE,
                Types.INTEGER,
                Types.INTEGER,
                Types.VARCHAR,
                Types.VARCHAR,
                Types.VARCHAR,
                Types.VARCHAR,
                Types.VARCHAR,
                Types.VARCHAR,
                Types.VARCHAR,
                Types.INTEGER,
                Types.VARCHAR,
                Types.VARCHAR,
            },
            idColumn,
            Types.INTEGER,
            newRoom.getId()
        );

        if (rows == 0)
            throw new IllegalStateException("Room to be updated with id " 
            + newRoom.getId() + " didn't exist in database");

        //update cache
        Optional<Room> optionalRoom = getRoom(newRoom.getId());
        optionalRoom.ifPresentOrElse((oldRoom) -> {
            rooms.remove(oldRoom);
            rooms.add(newRoom);
        }, () -> {
            throw new IllegalStateException("Room to be updated with id " 
                + newRoom.getId() + " didn't exist in database");
        });
        return;
    }

    //CHANGE TO ONLY COLUMNS THAT WILL BE DISPLAYED ON PARENT VIEW
    public static void insertRoom (String roomName, 
        String roomDescription, Double pricePerNight,
        Double extraFee, Integer numBed, Integer numBath, 
        String startDate, String endDate, String address, 
        String address2, String state, String city, String zip, 
        Integer roomStatus, String roomGuest, String roomOwner) {
        //update database
        int id = (int) CRUDHelper.create(
            tableName,
            new String[] {
                roomNameColumn,
                roomDescriptionColumn,
                pricePerNightColumn,
                extraFeeColumn,
                numBedColumn,
                numBathColumn,
                startDateColumn,
                endDateColumn,
                addressColumn,
                address2Column,
                stateColumn,
                cityColumn,
                zipColumn,
                roomStatusColumn,
                roomGuestColumn,
                roomOwnerColumn
            },
            new Object[] {
                roomName,
                roomDescription,
                pricePerNight,
                extraFee,
                numBed,
                numBath,
                startDate,
                endDate,
                address,
                address2,
                state,
                city,
                zip,
                roomStatus,
                roomGuest,
                roomOwner
            },
            new int[] {
                Types.VARCHAR,
                Types.VARCHAR,
                Types.DOUBLE,
                Types.DOUBLE,
                Types.INTEGER,
                Types.INTEGER,
                Types.VARCHAR,
                Types.VARCHAR,
                Types.VARCHAR,
                Types.VARCHAR,
                Types.VARCHAR,
                Types.VARCHAR,
                Types.VARCHAR,
                Types.INTEGER,
                Types.VARCHAR,
                Types.VARCHAR});

        //update cache
        rooms.add(new Room(
            id,
            roomName,
            roomDescription,
            pricePerNight,
            extraFee,
            numBed,
            numBath,
            startDate,
            endDate,
            address,
            address2,
            state,
            city,
            zip,
            roomStatus,
            roomGuest,
            roomOwner
        ));
    }

    public static void delete(int id) {
        //update database
        CRUDHelper.delete(tableName, id);

        //update cache
        Optional<Room> room = getRoom(id);
        room.ifPresent((rooms::remove));
    }

    public static Optional<Room> getRoom(int id) {
        for (Room room : rooms) {
            if (room.getId() == id) return Optional.of(room);
        }
        return Optional.empty();
    }
}
