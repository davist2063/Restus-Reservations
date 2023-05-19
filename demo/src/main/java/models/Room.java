package models;

import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Room {
    private final int id;
    private final ReadOnlyStringProperty roomName;
    private final ReadOnlyStringProperty roomDescription;
    private final ReadOnlyDoubleProperty pricePerNight;
    private final ReadOnlyDoubleProperty extraFee;
    private final ReadOnlyIntegerProperty numBed;
    private final ReadOnlyIntegerProperty numBath;
    private final ReadOnlyStringProperty startDate;
    private final ReadOnlyStringProperty endDate;
    private final ReadOnlyStringProperty address;
    private final ReadOnlyStringProperty address2;
    private final ReadOnlyStringProperty state;
    private final ReadOnlyStringProperty city;
    private final ReadOnlyStringProperty zip;
    private final ReadOnlyIntegerProperty roomStatus;
    private final ReadOnlyStringProperty roomGuest;
    private final ReadOnlyStringProperty roomOwner;
    private String findRoomStatusString;
    private String myRoomStatusString;
    


    public Room(int id, String roomName, String roomDescription,
            Double pricePerNight, Double extraFee, Integer numBed,
            Integer numBath, String startDate, String endDate,
            String address, String address2, String state,
            String city, String zip, Integer roomStatus,
            String roomGuest, String roomOwner) {
        this.id = id;
        this.roomName = new SimpleStringProperty(roomName);
        this.roomDescription = new SimpleStringProperty(roomDescription);
        this.pricePerNight = new SimpleDoubleProperty(pricePerNight);
        this.extraFee = new SimpleDoubleProperty(extraFee);
        this.numBed = new SimpleIntegerProperty(numBed);
        this.numBath = new SimpleIntegerProperty(numBath);
        this.startDate = new SimpleStringProperty(startDate);
        this.endDate = new SimpleStringProperty(endDate);
        this.address = new SimpleStringProperty(address);
        this.address2 = new SimpleStringProperty(address2);
        this.state = new SimpleStringProperty(state);
        this.city = new SimpleStringProperty(city);
        this.zip = new SimpleStringProperty(zip);
        this.roomStatus = new SimpleIntegerProperty(roomStatus);
        this.roomGuest = new SimpleStringProperty(roomGuest);
        this.roomOwner = new SimpleStringProperty(roomOwner);
        setStatusString();
    }

    public int getId() {
        return id;
    }

    public String getRoomName() {
        return roomName.get();
    }

    public ReadOnlyStringProperty roomNameProperty() {
        return roomName;
    }

    public String getRoomDescription() {
        return roomDescription.get();
    }

    public ReadOnlyStringProperty roomDescriptionProperty() {
        return roomDescription;
    }

    public double getPricePerNight() {
        return pricePerNight.get();
    }

    public ReadOnlyDoubleProperty pricePerNightProperty() {
        return pricePerNight;
    }

    public double getExtraFee() {
        return extraFee.get();
    }

    public ReadOnlyDoubleProperty extraFeeProperty() {
        return extraFee;
    }

    public int getNumBed() {
        return numBed.get();
    }

    public ReadOnlyIntegerProperty numBedProperty() {
        return numBed;
    }

    public int getNumBath() {
        return numBath.get();
    }

    public ReadOnlyIntegerProperty numBathProperty() {
        return numBath;
    }

    public String getStartDate() {
        return startDate.get();
    }

    public ReadOnlyStringProperty startDateProperty() {
        return startDate;
    }

    public String getEndDate() {
        return endDate.get();
    }

    public ReadOnlyStringProperty endDateProperty() {
        return endDate;
    }

    public String getAddress() {
        return address.get();
    }

    public ReadOnlyStringProperty addressProperty() {
        return address;
    }

    public String getAddress2() {
        return address2.get();
    }

    public ReadOnlyStringProperty address2Property() {
        return address2;
    }

    public String getState() {
        return state.get();
    }

    public ReadOnlyStringProperty stateProperty() {
        return state;
    }

    public String getCity() {
        return city.get();
    }

    public ReadOnlyStringProperty cityProperty() {
        return city;
    }

    public String getZip() {
        return zip.get();
    }

    public ReadOnlyStringProperty zipProperty() {
        return zip;
    }

    public int getRoomStatus() {
        return roomStatus.get();
    }

    public ReadOnlyIntegerProperty roomStatusProperty() {
        return roomStatus;
    }

    public String getRoomGuest() {
        return roomGuest.get();
    }

    public ReadOnlyStringProperty roomGuestProperty() {
        return roomGuest;
    }

    public String getRoomOwner() {
        return roomOwner.get();
    }

    public ReadOnlyStringProperty roomOwnerProperty() {
        return roomOwner;
    }

    private void setStatusString() {
        switch(this.roomStatus.get()) {
            case 0:
                findRoomStatusString = "Open";
                myRoomStatusString = "Ready For Booking";
                break;
            case 1:
                findRoomStatusString = "Closed";
                myRoomStatusString = "Confirmed";
                break;
            case 2:
                findRoomStatusString = "Closed";
                myRoomStatusString = "Pending";
                break;
            case 3:
                findRoomStatusString = "Closed";
                myRoomStatusString = "Modify for Next Booking";
                break;
        }
    }

    public String getFindRoomStatusString() {
        return findRoomStatusString;
    }

    public String getMyRoomStatusString() {
        return myRoomStatusString;
    }

    @Override
    public String toString() {
        //Replace with String
        return super.toString();
    }
}
