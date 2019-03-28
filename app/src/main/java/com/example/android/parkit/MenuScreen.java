package com.example.android.parkit;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class MenuScreen extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_screen);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);

        loadFragment(new ParkingFragment());
    }

    private boolean loadFragment(Fragment fragment)
    {
        if(fragment != null)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).commit();
            return true;
        }
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment = new ParkingFragment();

        switch (menuItem.getItemId())
        {
            case R.id.navigation_parking:

                fragment = new ParkingFragment();
                break;

            case R.id.navigation_scanner:
                fragment = new QRCodeFragment();
                break;

            case R.id.navigation_account:
                fragment = new AccountFragment();
                break;

            case R.id.navigation_more:
                fragment = new MoreFragment();
                break;
        }

        return loadFragment(fragment);    }
}
