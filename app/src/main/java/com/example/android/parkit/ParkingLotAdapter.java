package com.example.android.parkit;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ParkingLotAdapter extends ArrayAdapter<ParkingLots> {

    public ParkingLotAdapter(@NonNull Context context, @NonNull ArrayList<ParkingLots> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.info_dialog, parent, false);
        }

        ParkingLots currentParkingLots = getItem(position);

        TextView nameTextView =(TextView) listItemView.findViewById(R.id.parking_lot_name);

        nameTextView.setText(currentParkingLots.getParkingLotName());

        TextView addressTextView = (TextView) listItemView.findViewById(R.id.parking_lot_address);

        addressTextView.setText(currentParkingLots.getParkingLotAddress());

        TextView spaceTextView = (TextView) listItemView.findViewById(R.id.parking_lot_space);

        String s = currentParkingLots.getSpaceAvailable() + "/" + currentParkingLots.getTotal();

        spaceTextView.setText(s);

        return listItemView;
    }
}
