package models;

import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class ReservationView {
    private final int orderId;
    private final ReadOnlyStringProperty room;
    private final ReadOnlyStringProperty reservationStart;
    private final ReadOnlyStringProperty reservationEnd;
    private final ReadOnlyIntegerProperty reservationStatus;
    private final ReadOnlyIntegerProperty roomOrderStatus;
    private String reservationStatusString;
    private String roomOrderStatusString;


    public ReservationView(int orderId, String room, String reservationStart, 
        String reservationEnd, Integer reservationStatus, Integer roomOrderStatus) {
        this.orderId = orderId;
        this.room = new SimpleStringProperty(room);
        this.reservationStart = new SimpleStringProperty(reservationStart);
        this.reservationEnd = new SimpleStringProperty(reservationEnd);
        this.reservationStatus = new SimpleIntegerProperty(reservationStatus);
        this.roomOrderStatus = new SimpleIntegerProperty(roomOrderStatus);

        setReservationStatusString();
        setRoomOrderStatusString();
    }

    public int getOrderId() {
        return orderId;
    }

    public String getRoom() {
        return room.get();
    }

    public ReadOnlyStringProperty roomProperty() {
        return room;
    }

    public String getReservationStart() {
        return reservationStart.get();
    }

    public ReadOnlyStringProperty reservationStartProperty() {
        return reservationStart;
    }

    public String getReservationEnd() {
        return reservationEnd.get();
    }

    public ReadOnlyStringProperty reservationEndProperty() {
        return reservationEnd;
    }

    public Integer getReservationStatus() {
        return reservationStatus.get();
    }

    public ReadOnlyIntegerProperty reservationStatusProperty() {
        return reservationStatus;
    }

    public Integer getRoomOrderStatus() {
        return roomOrderStatus.get();
    }

    public ReadOnlyIntegerProperty roomOrderStatusProperty() {
        return roomOrderStatus;
    }

    private void setReservationStatusString() {
        switch(this.reservationStatus.get()) {
            case 0:
                reservationStatusString = "Ready For Booking";
                break;
            case 1:
                reservationStatusString = "Confirmed";
                break;
            case 2:
                reservationStatusString = "Pending";
                break;
            case 3:
                reservationStatusString = "Finished";
                break;
        }
    }

    private void setRoomOrderStatusString() {
        switch(this.roomOrderStatus.get()) {
            case 0:
                roomOrderStatusString = "Request Denied";
                break;
            case 1:
                roomOrderStatusString = "Pending";
                break;
            case 2:
                roomOrderStatusString = "Not Right Day";
                break;
            case 3:
                roomOrderStatusString = "Can Check In";
                break;
            case 4:
                roomOrderStatusString = "Checked In";
                break;
            case 5:
                roomOrderStatusString = "Checked Out";
                break;
        }
    }

    public String getReservationStatusString() {
        return reservationStatusString;
    }

    public String getRoomOrderStatusString() {
        return roomOrderStatusString;
    }
}
