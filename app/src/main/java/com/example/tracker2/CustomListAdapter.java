package com.example.tracker2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import java.util.ArrayList;

public class CustomListAdapter extends ArrayAdapter<Card> {

    private Context context;
    private int resource;

    private static class ViewHolder {
        TextView locationName;
        TextView time;
        TextView coordinates;
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
        double latitude = getItem(position).getLatitude();
        double longitude = getItem(position).getLongitude();
        String timeText = getItem(position).getTime();

        ViewHolder viewHolder;

        if(convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(resource, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.locationName = (TextView) convertView.findViewById(R.id.locationTextView);
            viewHolder.time = (TextView) convertView.findViewById(R.id.timeTextView);
            viewHolder.coordinates = (TextView) convertView.findViewById(R.id.coordinates);
            viewHolder.cardView = (CardView)  convertView.findViewById(R.id.cardView);

            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.locationName.setText(locationNameText);
        viewHolder.time.setText(timeText);
        viewHolder.coordinates.setText(latitude + ","+longitude);

        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCoordinatesToMapsActivity(v, locationNameText, latitude, longitude);
            }

        });


        return convertView;
    }

    public void sendCoordinatesToMapsActivity(View v, String locationName, double latitude, double longitude) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("locationName",locationName);
        editor.putString("latitude", latitude+"");
        editor.putString("longitude", longitude+"");
        editor.commit();
        Intent sd=new Intent(context,MapsActivity.class);
        context.startActivity(sd);
    }
}
