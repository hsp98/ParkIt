package com.example.android.parkit;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class BookedParkingAdapter extends ArrayAdapter<BookedParking> {

    public BookedParkingAdapter(@NonNull Context context, @NonNull List<BookedParking> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_view_bookings, parent, false);
        }

        BookedParking bookedParking = getItem(position);

        TextView parkingLotNameTextView = (TextView) listItemView.findViewById(R.id.booked_parking_lot_name);
        parkingLotNameTextView.setText(bookedParking.getParkingLotName());

        TextView feesPaidTextView = (TextView) listItemView.findViewById(R.id.fees_paid);
        feesPaidTextView.setText(bookedParking.getFeesPaid());

        TextView durationTextView = (TextView) listItemView.findViewById(R.id.duration);
        durationTextView.setText(bookedParking.getDuration());

        TextView toAndFromTextView = (TextView) listItemView.findViewById(R.id.to_from_date);
        toAndFromTextView.setText(bookedParking.getArrivalDate() + " TO " + bookedParking.getLeaveDate());

        TextView bookingIDTextView = (TextView) listItemView.findViewById(R.id.booking_id);
        bookingIDTextView.setText(bookedParking.getBookingID());

        return listItemView;
    }
}
