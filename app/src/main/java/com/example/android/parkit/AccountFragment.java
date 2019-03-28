package com.example.android.parkit;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class AccountFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_account, container, false);

        TextView signOutTextView = (TextView) rootView.findViewById(R.id.sign_out);
        signOutTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.prefConfig.writeLoginStatus(false);
                MainActivity.prefConfig.writeName("User");
                Intent intent = new Intent(getActivity(), HomePage.class);
                startActivity(intent);
            }
        });

        TextView nameTextView = (TextView) rootView.findViewById(R.id.name);

        TextView mobileNoTextView = (TextView) rootView.findViewById(R.id.mobile_number);

        TextView emailIdTextView = (TextView) rootView.findViewById(R.id.email_id);

        return rootView;
    }
}
