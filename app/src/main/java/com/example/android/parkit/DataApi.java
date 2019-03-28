package com.example.android.parkit;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface DataApi {

    @POST("register.php")
    Call<Data> createPost(@Query("name") String name, @Query("email") String emailId, @Query("phone_num") String mobileNumber, @Query("password") String password);

    @POST("login.php")
    Call<Data> checkLogin(@Query("email") String emailId, @Query("password") String password);

    @POST("latlong.php")
    Call<List<ParkingLotData>> getParkingData(@Query("lat") Double lat, @Query("lng") Double lng);

    @POST("getCredentials.php")
    Call<List<ParkingLotData>> getData();

    @POST("booking.php")
    Call<List<BookedParkingData>> getBookings(@Query("email") String email);

    @POST("booking.php")
    Call<List<ParkingBookingData>> bookParking(@Query("email") String email, @Query("parkingLotName") String parkingLotName, @Query("arrivalDate") String arrivalDate,
                             @Query("leaveDate") String leaveDate, @Query("arrivalTime") String arrivalTime, @Query("leaveTime") String leaveTime,
                             @Query("duration") String duration, @Query("fees") String fees);

}
