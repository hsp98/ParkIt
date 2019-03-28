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
public class RegistrationFragment extends Fragment {

    private EditText Name,EmailId,PhoneNumber,Password;
    private Button RegisterBtn;
    private DataApi dataApi;

    public RegistrationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_registration, container, false);

        Name = view.findViewById(R.id.name);
        EmailId = view.findViewById(R.id.email);
        PhoneNumber = view.findViewById(R.id.phone_number);
        Password = view.findViewById(R.id.password);
        RegisterBtn = view.findViewById(R.id.register);

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


        return view;
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
                    Toast.makeText(getContext(), "Code : " + response.code(),Toast.LENGTH_SHORT).show();
                    return;
                }

                Data dataResponse = response.body();

                switch(dataResponse.getResponse())
                {
                    case "donee" :
                        Toast.makeText(getContext(),"Registration Success",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getActivity(), MenuScreen.class);
                        startActivity(intent);
                        break;

                    case "exist":
                        Toast.makeText(getContext(),"User Exist Please Login",Toast.LENGTH_SHORT).show();
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new LoginFragment()).commit();
                        break;

                    case "error":
                        Toast.makeText(getContext(), "Try again", Toast.LENGTH_SHORT).show();
                        break;

                    default:
                        Toast.makeText(getContext(), "Restart", Toast.LENGTH_SHORT).show();
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
