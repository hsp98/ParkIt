package com.example.android.parkit;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class QRCodeFragment extends Fragment
{
    private DataApi dataApi;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_qrcode, null);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://parkit1.000webhostapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        dataApi = retrofit.create(DataApi.class);

        //getBookingData();

        final ArrayList<BookedParking> bookedParkingDataArrayList = new ArrayList<BookedParking>();

        bookedParkingDataArrayList.add(new BookedParking("A", "2 hours","28/1/2019",
                "28/1/2019", "20","ABCD123"));

        ListView listView =(ListView) rootView.findViewById(R.id.bookings_list_view);

        BookedParkingAdapter bookedParkingAdapter = new BookedParkingAdapter(getContext(), bookedParkingDataArrayList);

        listView.setAdapter(bookedParkingAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                BookedParking bookedParking = bookedParkingDataArrayList.get(i);
                String bookingId = bookedParking.getBookingID();

                QRCodeBottomSheetFragment qrCodeBottomSheetFragment = new QRCodeBottomSheetFragment();

                Bundle args = new Bundle();
                args.putString("bookingId", bookingId);
                qrCodeBottomSheetFragment.setArguments(args);
//                getFragmentManager().beginTransaction().add(R.id.fragment_container,qrCodeBottomSheetFragment).commit();

                qrCodeBottomSheetFragment.show(getFragmentManager(),"example bottomsheet");
            }
        });


        return rootView;
    }

    private void getBookingData()
    {
        String email = MainActivity.prefConfig.readName();

        Call<List<BookedParkingData>> call = dataApi.getBookings(email);

        call.enqueue(new Callback<List<BookedParkingData>>() {
            @Override
            public void onResponse(Call<List<BookedParkingData>> call, Response<List<BookedParkingData>> response) {
                if(!response.isSuccessful())
                {
                    Toast.makeText(getContext(), "Code : " + response.code(),Toast.LENGTH_SHORT).show();
                    return;
                }

                final ArrayList<BookedParking> bookedParkingDataArrayList = new ArrayList<BookedParking>();

                List<BookedParkingData> bookedParkingDataList = response.body();

                for(final BookedParkingData bookedParkingData : bookedParkingDataList )
                {
                    bookedParkingDataArrayList.add(new BookedParking(bookedParkingData.getParkingLotName(), bookedParkingData.getDuration(),
                            bookedParkingData.getArrivalDate(), bookedParkingData.getLeaveDate(), bookedParkingData.getFeesPaid(),bookedParkingData.getBookingID()));
                }

                bookedParkingDataArrayList.add(new BookedParking("A", "2 hours","28/1/2019",
                        "28/1/2019", "20","ABCD123"));

                ListView listView =(ListView) getActivity().findViewById(R.id.bookings_list_view);

                BookedParkingAdapter bookedParkingAdapter = new BookedParkingAdapter(getContext(), bookedParkingDataArrayList);

                listView.setAdapter(bookedParkingAdapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                        BookedParking bookedParking = bookedParkingDataArrayList.get(i);
                        String bookingId = bookedParking.getBookingID();

                        QRCodeBottomSheetFragment qrCodeBottomSheetFragment = new QRCodeBottomSheetFragment();
                        qrCodeBottomSheetFragment.show(getFragmentManager(),"example bottomsheet");

                    }
                });

            }

            @Override
            public void onFailure(Call<List<BookedParkingData>> call, Throwable t) {
                Toast.makeText(getContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }

}
