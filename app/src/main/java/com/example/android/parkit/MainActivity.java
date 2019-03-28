package com.example.android.parkit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    public static PrefConfig prefConfig;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefConfig = new PrefConfig(this);

        int timeout = 2000; // make the activity visible for 2 seconds

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                finish();

                if(prefConfig.readLoginStatus())
                {
                    Intent intent = new Intent(MainActivity.this, MenuScreen.class);
                    startActivity(intent);
                }
                else
                {
                    Intent intent = new Intent(MainActivity.this, HomePage.class);
                    startActivity(intent);
                }

            }
        }, timeout);
    }
}
