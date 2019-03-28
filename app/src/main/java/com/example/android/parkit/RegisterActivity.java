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

public class RegisterActivity extends AppCompatActivity {

    private EditText Name,EmailId,PhoneNumber,Password;
    private Button RegisterBtn;
    private DataApi dataApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Name = findViewById(R.id.name);
        EmailId = findViewById(R.id.email);
        PhoneNumber = findViewById(R.id.phone_number);
        Password = findViewById(R.id.password);
        RegisterBtn = findViewById(R.id.register);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://parkit1.000webhostapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        dataApi = retrofit.create(DataApi.class);

        RegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performRegistration();
            }
        });

    }

    private void performRegistration()
    {
        String name = Name.getText().toString();
        String email = EmailId.getText().toString();
        String phoneNumber = PhoneNumber.getText().toString();
        String password = Password.getText().toString();

        Call<Data> call = dataApi.createPost(name,email,phoneNumber,password);

        call.enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                if(!response.isSuccessful())
                {
                    Toast.makeText(getApplicationContext(), "Code : " + response.code(),Toast.LENGTH_SHORT).show();
                    return;
                }

                Data dataResponse = response.body();

                switch(dataResponse.getResponse())
                {
                    case "donee" :
                        Toast.makeText(getApplicationContext(),"Registration Success",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegisterActivity.this, MenuScreen.class);
                        startActivity(intent);
                        break;

                    case "exist":
                        Toast.makeText(getApplicationContext(),"User Exist Please Login",Toast.LENGTH_SHORT).show();
                        Intent intent2 = new Intent(RegisterActivity.this, LogInActivity.class);
                        startActivity(intent2);
                        break;

                    case "error":
                        Toast.makeText(getApplicationContext(), "Try again", Toast.LENGTH_SHORT).show();
                        break;

                    default:
                        Toast.makeText(getApplicationContext(), "Restart", Toast.LENGTH_SHORT).show();
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
