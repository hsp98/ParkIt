package com.example.android.parkit;

public class ParkingBookingData {

    private String email, parkingLotName, arrivalDate, arrivalTime, leaveDate, leaveTime, duration, fees, bookingID;

    public ParkingBookingData(String email, String parkingLotName, String arrivalDate, String arrivalTime, String leaveDate, String leaveTime, String duration, String fees) {
        this.email = email;
        this.parkingLotName = parkingLotName;
        this.arrivalDate = arrivalDate;
        this.arrivalTime = arrivalTime;
        this.leaveDate = leaveDate;
        this.leaveTime = leaveTime;
        this.duration = duration;
        this.fees = fees;
    }

    public String getEmail() {
        return email;
    }

    public String getParkingLotName() {
        return parkingLotName;
    }

    public String getArrivalDate() {
        return arrivalDate;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public String getLeaveDate() {
        return leaveDate;
    }

    public String getLeaveTime() {
        return leaveTime;
    }

    public String getDuration() {
        return duration;
    }

    public String getFees() {
        return fees;
    }

    public String getBookingID(){
        return bookingID;
    }
}
