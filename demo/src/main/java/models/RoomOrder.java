package models;

import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class RoomOrder {
    private final int id;
    private final int roomId;
    private final ReadOnlyStringProperty roomGuest;
    private final ReadOnlyIntegerProperty numAdult;
    private final ReadOnlyIntegerProperty numKid;
    private final ReadOnlyIntegerProperty numPet;
    private final ReadOnlyDoubleProperty invoiceTotal;
    private final ReadOnlyStringProperty checkInDate;
    private final ReadOnlyStringProperty checkOutDate;
    private final ReadOnlyIntegerProperty roomOrderStatus;

    public RoomOrder(int id, int roomId, String roomGuest, Integer numAdult,
            Integer numKid, Integer numPet, Double invoiceTotal,
            String checkInDate, String checkOutDate,
            Integer roomOrderStatus) {
        this.id = id;
        this.roomId = roomId;
        this.roomGuest = new SimpleStringProperty(roomGuest);
        this.numAdult = new SimpleIntegerProperty(numAdult);
        this.numKid = new SimpleIntegerProperty(numKid);
        this.numPet = new SimpleIntegerProperty(numPet);
        this.invoiceTotal = new SimpleDoubleProperty(invoiceTotal);
        this.checkInDate = new SimpleStringProperty(checkInDate);
        this.checkOutDate = new SimpleStringProperty(checkOutDate);
        this.roomOrderStatus = new SimpleIntegerProperty(roomOrderStatus);
    }

    public int getId() {
        return id;
    }

    public int getRoomId() {
        return roomId;
    }

    public String getRoomGuest() {
        return roomGuest.get();
    }

    public ReadOnlyStringProperty roomGuestProperty() {
        return roomGuest;
    }

    public int getNumAdult() {
        return numAdult.get();
    }

    public ReadOnlyIntegerProperty numAdultProperty() {
        return numAdult;
    }

    public int getNumKid() {
        return numKid.get();
    }

    public ReadOnlyIntegerProperty numKidProperty() {
        return numKid;
    }

    public int getNumPet() {
        return numPet.get();
    }

    public ReadOnlyIntegerProperty numPetProperty() {
        return numPet;
    }

    public double getInvoiceTotal() {
        return invoiceTotal.get();
    }

    public ReadOnlyDoubleProperty invoiceTotalProperty() {
        return invoiceTotal;
    }

    public String getCheckInDate() {
        return checkInDate.get();
    }

    public ReadOnlyStringProperty checkInDateProperty() {
        return checkInDate;
    }

    public String getCheckOutDate() {
        return checkOutDate.get();
    }

    public ReadOnlyStringProperty checkOutDateProperty() {
        return checkOutDate;
    }

    public int getRoomOrderStatus() {
        return roomOrderStatus.get();
    }

    public ReadOnlyIntegerProperty roomOrderStatusProperty() {
        return roomOrderStatus;
    }

    @Override
    public String toString() {
        //Replace with String
        return super.toString();
    }
}
