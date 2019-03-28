package com.example.android.parkit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ParkingLotMap extends AppCompatActivity {

    private DataApi dataApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_lot_map);

        Button btn = (Button) findViewById(R.id.proceedToParking);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               bookParking();
            }
        });
    }

    private void bookParking()
    {

        try{
            String email = MainActivity.prefConfig.readName();
            Intent intent = getIntent();
            String parkingLotName = intent.getStringExtra("parkingLotName");
            String arrivalDate = intent.getStringExtra("arrivalDate");
            String leaveDate = intent.getStringExtra("leaveDate");
            String arrivalTime = intent.getStringExtra("arrivalTime");
            String leaveTime = intent.getStringExtra("leaveTime");

            SimpleDateFormat format = new SimpleDateFormat("hh:mm aa");
            Date at = format.parse(arrivalTime);
            Date lt = format.parse(leaveTime);
            long mills = lt.getTime() - at.getTime();
            int hours = (int) (mills/(1000 * 60 * 60));
            int mins = (int) (mills % (1000*60*60));
            String duration = hours + ":" + mins;
            int charge = hours * 10 + (mins*10)/60;
            String fees = charge+"";


            Call<List<ParkingBookingData>> call = dataApi.bookParking(email,parkingLotName,arrivalDate,leaveDate, arrivalTime, leaveTime,duration, fees);

            call.enqueue(new Callback<List<ParkingBookingData>>() {
                @Override
                public void onResponse(Call<List<ParkingBookingData>> call, Response<List<ParkingBookingData>> response) {
                    if(!response.isSuccessful())
                    {
                        Toast.makeText(getApplicationContext(), "Code : " + response.code(),Toast.LENGTH_SHORT).show();
                        return;
                    }
                    List<ParkingBookingData> parkingBookingDataList = response.body();
                    for(final ParkingBookingData parkingBookingData : parkingBookingDataList){
                        String bookingID = parkingBookingData.getBookingID();

                        Toast.makeText(getApplicationContext(),"Your Parking is Booked with booking ID : " + bookingID,Toast.LENGTH_LONG).show();
                        break;
                    }
                    Intent intent = new Intent(ParkingLotMap.this, MenuScreen.class);
                    startActivity(intent);
                }

                @Override
                public void onFailure(Call<List<ParkingBookingData>> call, Throwable t) {
                    Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }
}
