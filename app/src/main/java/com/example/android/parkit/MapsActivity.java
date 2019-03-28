package com.example.android.parkit;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = "MapsActivity";
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(
            new LatLng(-40,-168), new LatLng(71,136));

    //widgets
    private ImageView mGps;

    //vars
    private Boolean mLocationPermissionGranted = false;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private int PLACE_AUTOCOMPLETE_REQUEST_CODE = 11;
    Double lat = 0.0;
    Double lng = 0.0;
    private String placeId = "";
    private String name = "";
    private String address = "";
    private String phoneNumber = "";
    private String websiteUri = "";
    private Float placeRating = null;
    private DataApi dataApi;
    private Marker mMarker;
    private BottomSheetBehavior bottomSheetBehavior;
    private TextView mName, mAddress, mSpace, mPrice;

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(this, "Map is Ready", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "Map is ready");
        mMap = googleMap;

        if (mLocationPermissionGranted) {

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);

            mMap.getUiSettings().setMyLocationButtonEnabled(false);

            //getParkingLotData();
            init();
        }
    }

    private void getData()
    {
        Call<List<ParkingLotData>> call = dataApi.getData();

        call.enqueue(new Callback<List<ParkingLotData>>() {
            @Override
            public void onResponse(Call<List<ParkingLotData>> call, Response<List<ParkingLotData>> response) {
                if(!response.isSuccessful())
                {
                    Toast.makeText(getApplicationContext(), "Code : " + response.code(),Toast.LENGTH_SHORT).show();
                    return;
                }

                final ArrayList<ParkingLots> parkingLotsArrayList = new ArrayList<ParkingLots>();
                List<ParkingLotData> parkingLotDataList =  response.body();

                for(final ParkingLotData parkingLotData: parkingLotDataList)
                {
                    parkingLotsArrayList.add(new ParkingLots(parkingLotData.getName(), parkingLotData.getAddress(), parkingLotData.getAvailable(), parkingLotData.getTotal()));
                    LatLng locationSelected = new LatLng(parkingLotData.getLat(), parkingLotData.getLng());
                    mMap.addMarker(new MarkerOptions().position(locationSelected).title(parkingLotData.getName())
                            .snippet("Space :" + parkingLotData.getAvailable() +"/" + parkingLotData.getTotal() ));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(locationSelected));
                    mMap.moveCamera((CameraUpdateFactory.zoomTo(DEFAULT_ZOOM)));
                }

                View bottomSheet = findViewById(R.id.bottom_sheet);
                bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);

                ListView listView =(ListView) bottomSheet.findViewById(R.id.list_view);

                ParkingLotAdapter parkingLotAdapter = new ParkingLotAdapter(MapsActivity.this, parkingLotsArrayList);

                listView.setAdapter(parkingLotAdapter);
                setListViewHeightBasedOnChildren(listView);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                        ParkingLots parkingLots = parkingLotsArrayList.get(i);
                        String parkingLotName = parkingLots.getParkingLotName();
                        Intent getData = getIntent();
                        Intent intent = new Intent(MapsActivity.this, ParkingLotMap.class);
                        intent.putExtra("parkingLotName", parkingLotName);
                        intent.putExtra("arrivalDate", getData.getStringExtra("arrivalDate"));
                        intent.putExtra("leaveDate", getData.getStringExtra("leaveDate"));
                        intent.putExtra("arrivalTime", getData.getStringExtra("arrivalTime"));
                        intent.putExtra("leaveTime", getData.getStringExtra("leaveTime"));
                        startActivity(intent);
                    }
                });

            }

            @Override
            public void onFailure(Call<List<ParkingLotData>> call, Throwable t) {
                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        mGps = (ImageView)findViewById(R.id.ic_gps);

        getLocationPermission();

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.

                mMap.clear();
                LatLng location = place.getLatLng();
                lat = location.latitude;
                lng = location.longitude;
                String title = (String) place.getName();
                moveCamera(new LatLng(lat , lng), DEFAULT_ZOOM, title);


                Log.i(TAG, "Place: " + place.getName());
                Log.d(TAG, "lat" + location.latitude + "lng" +location.longitude);
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://parkit1.000webhostapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        dataApi = retrofit.create(DataApi.class);

        getData();
    }

    private void callPlaceAutocompleteActivityIntent() {
        try {
            Intent intent =
                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                            .build(this);
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);

            //PLACE_AUTOCOMPLETE_REQUEST_CODE is integer for request code
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            // TODO: Handle the error.
        }

    }

    private void init()
    {
        Log.d(TAG, "init: initializing");

        mGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG,"Clicked gps icon");
                getDeviceLocation();
            }
        });
        hideSoftKeyboard();
    }

    private void getDeviceLocation(){
        Log.d(TAG,"getDeviceLocation: getting the devices current location");
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try{
            if(mLocationPermissionGranted){
                Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful())
                        {
                            Log.d(TAG, "OnCompleteListener: found location");
                            Location currentLocation = (Location) task.getResult();
                            moveCamera(new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude()), DEFAULT_ZOOM, "My Location");
                        }
                        else
                        {
                            Log.d(TAG, "onComplete : Current location is null");
                            Toast.makeText(MapsActivity.this, "Unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }catch (SecurityException e){
            Log.e(TAG,"getDeviceLocation : SecurityException : " + e.getMessage());
        }

    }

    private void moveCamera(LatLng latlng, float zoom, String title){
        Log.d(TAG, "moveCamera: moving camera to: lat: " + latlng.latitude + ", lng : "  + latlng.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, zoom));

        if(!title.equals("MyLocation")) {
            MarkerOptions options = new MarkerOptions().position(latlng).title(title);
            mMap.addMarker(options);
        }

        hideSoftKeyboard();
    }

    private void initMap(){
        Log.d(TAG, "Initialising Map");
        SupportMapFragment mapFrqagment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFrqagment.getMapAsync(MapsActivity.this);
    }

    private void getLocationPermission()
    {
        Log.d(TAG, "getting location permissions");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(),COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                mLocationPermissionGranted = true;
                initMap();
            }
            else
            {
                ActivityCompat.requestPermissions(this, permissions,LOCATION_PERMISSION_REQUEST_CODE);
            }
        }
        else {
            ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult : called");
        mLocationPermissionGranted = false;

        switch(requestCode)
        {
            case LOCATION_PERMISSION_REQUEST_CODE:{
                if(grantResults.length > 0)
                {
                    for(int i = 0; i < grantResults.length; i++){
                        if(grantResults[i] != PackageManager.PERMISSION_GRANTED)
                        {
                            mLocationPermissionGranted = false;
                            Log.d(TAG, "onRequestPermissionsResult : permission failed");
                            return;
                        }
                    }
                    Log.d(TAG, "onRequestPermissionsResult : permission granted");
                    mLocationPermissionGranted = true;
                    //initialise our map
                    initMap();
                }
            }
        }
    }

    private void hideSoftKeyboard()
    {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

}
