package com.example.android.parkit;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class HomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, new HomePageFragment()).commit();
//        if(findViewById(R.id.fragment_container) != null)
//        {
//            if(savedInstanceState != null)
//            {
//                return;
//            }
//        }
    }
}
