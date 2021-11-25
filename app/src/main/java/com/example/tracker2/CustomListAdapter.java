package com.example.tracker2;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import java.util.ArrayList;

public class CustomListAdapter extends ArrayAdapter<Card> {

    private Context context;
    private int resource;

    private static class ViewHolder {
        TextView locationName;
        TextView time;
        CardView cardView;
    }

    public CustomListAdapter(Context context, int resource, ArrayList<Card> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String locationNameText = getItem(position).getLocationName();
        String timeText = getItem(position).getTime();
        double latitude = getItem(position).getLat();
        double longitude = getItem(position).getLongi();

        ViewHolder viewHolder;

        if(convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(resource, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.locationName = (TextView) convertView.findViewById(R.id.locationTextView);
            viewHolder.time = (TextView) convertView.findViewById(R.id.timeTextView);

            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.locationName.setText(locationNameText);
        viewHolder.time.setText(timeText);
        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCoordinatesToMapsActivity(v, latitude, longitude);
            }

        });


        return convertView;
    }

    public void sendCoordinatesToMapsActivity(View v, double latitude, double longitude) {
       //Write code to send lat and longi
    }
}
