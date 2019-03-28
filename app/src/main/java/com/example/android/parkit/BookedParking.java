package com.example.android.parkit;

public class BookedParking {

    private String parkingLotName;

    private String duration;

    private String arrivalDate;

    private String leaveDate;

    private String feesPaid;

    private String bookingID;

    public BookedParking(String parkingLotName, String duration, String arrivalDate, String leaveDate, String feesPaid, String bookingID) {
        this.parkingLotName = parkingLotName;
        this.duration = duration;
        this.arrivalDate = arrivalDate;
        this.leaveDate = leaveDate;
        this.feesPaid = feesPaid;
        this.bookingID = bookingID;
    }

    public String getParkingLotName() {
        return parkingLotName;
    }

    public String getDuration() {
        return duration;
    }

    public String getArrivalDate() {
        return arrivalDate;
    }

    public String getLeaveDate() {
        return leaveDate;
    }

    public String getFeesPaid() {
        return feesPaid;
    }

    public String getBookingID() {
        return bookingID;
    }
}
