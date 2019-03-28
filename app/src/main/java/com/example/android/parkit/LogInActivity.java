package com.example.android.parkit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LogInActivity extends AppCompatActivity {

    private EditText Email;
    private EditText Password;
    private Button loginBtn;
    private DataApi dataApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        Email = findViewById(R.id.login_email);
        Password = findViewById(R.id.login_password);
        loginBtn = findViewById(R.id.login);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://parkit1.000webhostapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        dataApi = retrofit.create(DataApi.class);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performLogin();
            }
        });

    }



    private void performLogin()
    {
        String email = Email.getText().toString();
        String password = Password.getText().toString();

        Call<Data> call = dataApi.checkLogin(email,password);

        call.enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                if(!response.isSuccessful())
                {
                    Toast.makeText(getApplicationContext(), "Code : " + response.code(),Toast.LENGTH_SHORT).show();
                    return;
                }

                Data dataResponse = response.body();

                switch (dataResponse.getResponse())
                {
                    case "ok":
                        Toast.makeText(getApplicationContext(), "Login Success",Toast.LENGTH_SHORT).show();
                        MainActivity.prefConfig.writeLoginStatus(true);
                        MainActivity.prefConfig.writeName(dataResponse.getName());
                        Intent intent = new Intent(LogInActivity.this, MenuScreen.class);
                        startActivity(intent);
                        break;

                    case "failed":
                        Toast.makeText(getApplicationContext(), "Login Failed \n Wrong Email/Password",Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onFailure(Call<Data> call, Throwable t) {
                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}
