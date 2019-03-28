package com.example.android.parkit;

public class ParkingLotData {

    private Double lat;

    private Double lng;

    private String name;

    private String address;

    private int available;

    private int total;

    public ParkingLotData(Double lat, Double lng, String name, String address, int available, int total) {
        this.lat = lat;
        this.lng = lng;
        this.name = name;
        this.address = address;
        this.available = available;
        this.total = total;
    }

    public Double getLat() {
        return lat;
    }

    public Double getLng() {
        return lng;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public int getAvailable() {
        return available;
    }

    public int getTotal() {
        return total;
    }
}
