package com.example.android.parkit;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class ParkingFragment extends Fragment {

    private String TAG = "MenuScreen";
    private int PLACE_AUTOCOMPLETE_REQUEST_CODE = 11;
    String amPm;
    private static final int ERROR_DIALOG_REQUEST = 9001;
    private Double lat = 0.0;
    private  Double lng = 0.0;
    private String placeId = "1";
    private String name = "a";
    private String address = "b";
    private String phoneNumber = "3";
    private String websiteUri = "c";
    private Float placeRating = null;
    private int arrivalDay, arrivalMonth, arrivalYear, arrivalHour, arrivalMinute, leaveDay, leaveMonth, leaveYear, leaveHour, leaveMinute;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_parking, container, false);

        final TextView textView = (TextView) rootView.findViewById(R.id.text);

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getActivity().getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.

                LatLng location = place.getLatLng();
                lat = location.latitude;
                lng = location.longitude;
                try{
                    placeId = place.getId();
                    name = place.getName().toString();
                    address = place.getAddress().toString();
                    phoneNumber = place.getPhoneNumber().toString();
                    websiteUri = place.getWebsiteUri().toString();
                    placeRating = place.getRating();
                }catch(NullPointerException e)
                {
                    Log.d(TAG,"NullPointerException :" + e.getMessage());
                }

                Log.i(TAG, "Place: " + place.getName());
                Log.d(TAG, "lat" + location.latitude + "lng" +location.longitude);
                textView.setText("" + lat + " " + lng);
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });


        final Calendar myCalendar = Calendar.getInstance();
        final EditText edittext= rootView.findViewById(R.id.arrivalDate);
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {

                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
                arrivalDay = dayOfMonth;
                arrivalMonth = monthOfYear;
                arrivalYear = year;
            }

            private void updateLabel() {
                String myFormat = "MM/dd/yy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                edittext.setText(sdf.format(myCalendar.getTime()));
            }

        };

        edittext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        final Calendar myCalendar2 = Calendar.getInstance();

        final EditText edittext2= rootView.findViewById(R.id.leaveDate);
        final DatePickerDialog.OnDateSetListener date2 = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar2.set(Calendar.YEAR, year);
                myCalendar2.set(Calendar.MONTH, monthOfYear);
                myCalendar2.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

            private void updateLabel() {
                String myFormat2 = "MM/dd/yy"; //In which you need put here
                SimpleDateFormat sdf2 = new SimpleDateFormat(myFormat2, Locale.US);

                edittext2.setText(sdf2.format(myCalendar2.getTime()));
            }

        };

        edittext2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getActivity(), date2, myCalendar2
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar2.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        final EditText chooseTime;
        final TimePickerDialog[] timePickerDialog = new TimePickerDialog[1];
        final Calendar timeCalendar = Calendar.getInstance();
        final int currentHour = timeCalendar.get(Calendar.HOUR);
        final int currentMinute  = timeCalendar.get(Calendar.MINUTE);


        chooseTime = rootView.findViewById(R.id.arrivalTime);
        chooseTime.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {


                timePickerDialog[0] = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        if(hourOfDay >= 12)
                        {
                            amPm = " PM";
                        }
                        else
                        {
                            amPm = " AM";
                        }
                        chooseTime.setText(String.format("%02d/%02d", hourOfDay, minutes) + amPm);
                    }
                },currentHour,currentMinute,false);
                timePickerDialog[0].show();
            }
        });

        final EditText chooseTime2;
        final TimePickerDialog[] timePickerDialog2 = new TimePickerDialog[1];
        final Calendar timeCalendar2 = Calendar.getInstance();
        final int currentHour2 = timeCalendar.get(Calendar.HOUR);
        final int currentMinute2  = timeCalendar.get(Calendar.MINUTE);


        chooseTime2 = rootView.findViewById(R.id.leaveTime);
        chooseTime2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                timePickerDialog2[0] = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        if(hourOfDay >= 12)
                        {
                            amPm = " PM";
                        }
                        else
                        {
                            amPm = " AM";
                        }
                        chooseTime2.setText(String.format("%02d/%02d", hourOfDay, minutes) + amPm);
                    }
                },currentHour2,currentMinute2,false);
                timePickerDialog2[0].show();
            }
        });

        Button findParking = rootView.findViewById(R.id.find_parking);

        findParking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(),edittext.getText() +  " " + edittext2.getText() + " " + chooseTime.getText() + " " + chooseTime2.getText(),Toast.LENGTH_SHORT).show();
                if(isServicesOK())
                {
                    Intent sendData = new Intent(getActivity(), MapsActivity.class);
                    sendData.putExtra("lng", lng);
                    sendData.putExtra("lat", lat);
                    sendData.putExtra("name", name);
                    sendData.putExtra("address", address);
                    sendData.putExtra("placeId", placeId);
                    sendData.putExtra("phoneNumber", phoneNumber);
                    sendData.putExtra("placeRating", placeRating);
                    sendData.putExtra("websiteUri", websiteUri);
                    sendData.putExtra("arrivalDate", edittext.getText());
                    sendData.putExtra("leaveDate", edittext2.getText());
                    sendData.putExtra("arrivalTime", chooseTime.getText());
                    sendData.putExtra("leaveTime",chooseTime2.getText());
                    startActivity(sendData);
                                    }
            }
        });

        return rootView;
    }

    private void callPlaceAutocompleteActivityIntent() {
        try {
            Intent intent =
                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                            .build(getActivity());
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
            //PLACE_AUTOCOMPLETE_REQUEST_CODE is integer for request code
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            // TODO: Handle the error.
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //autocompleteFragment.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(getActivity(), data);
                Log.i(TAG, "Place:" + place.toString());
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(getActivity(), data);
                Log.i(TAG, status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {

            }
        }
    }


    public boolean isServicesOK()
    {
        Log.d(TAG, "isServicesOK: checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(getActivity());

        if(available == ConnectionResult.SUCCESS)
        {
            //everything is fine and user can make map requests
            Log.d(TAG, "isServicesOK : Google Play Services is working");
            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available))
        {
            //an error has occured but we can resolve it
            Log.d(TAG, "isServicesOK : an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(getActivity(),available, ERROR_DIALOG_REQUEST);
            dialog.show();
        }
        else
        {
            Toast.makeText(getActivity(), "You cant make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

}
