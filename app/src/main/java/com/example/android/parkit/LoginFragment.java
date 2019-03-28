package com.example.android.parkit;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {


    private EditText Email;
    private EditText Password;
    private Button loginBtn;
    private DataApi dataApi;


    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        Email = view.findViewById(R.id.login_email);
        Password = view.findViewById(R.id.login_password);
        loginBtn = view.findViewById(R.id.login);

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


        return view;
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
                    Toast.makeText(getContext(), "Code : " + response.code(),Toast.LENGTH_SHORT).show();
                    return;
                }

                Data dataResponse = response.body();

                switch (dataResponse.getResponse())
                {
                    case "ok":
                        Toast.makeText(getContext(), "Login Success",Toast.LENGTH_SHORT).show();
                        MainActivity.prefConfig.writeLoginStatus(true);
                        MainActivity.prefConfig.writeName(dataResponse.getName());
                        Intent intent = new Intent(getActivity(), MenuScreen.class);
                        startActivity(intent);
                        break;

                    case "failed":
                        Toast.makeText(getContext(), "Login Failed \n Wrong Email/Password",Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onFailure(Call<Data> call, Throwable t) {
                Toast.makeText(getContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

}
