package com.example.android.parkit;

public class ParkingLots {

    private String parkingLotName;
    private String parkingLotAddress;
    private int spaceAvailable;
    private int total;

    public ParkingLots(String parkingLotName, String parkingLotAddress, int spaceAvailable, int total) {
        this.parkingLotName = parkingLotName;
        this.parkingLotAddress = parkingLotAddress;
        this.spaceAvailable = spaceAvailable;
        this.total = total;
    }

    public String getParkingLotName() {
        return parkingLotName;
    }

    public String getParkingLotAddress() {
        return parkingLotAddress;
    }

    public int getSpaceAvailable() {
        return spaceAvailable;
    }

    public int getTotal() {
        return total;
    }

    public void setParkingLotName(String parkingLotName) {
        this.parkingLotName = parkingLotName;
    }

    public void setParkingLotAddress(String parkingLotAddress) {
        this.parkingLotAddress = parkingLotAddress;
    }

    public void setSpaceAvailable(int spaceAvailable) {
        this.spaceAvailable = spaceAvailable;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
