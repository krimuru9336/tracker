package com.example.tracker2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class CustomListAdapter extends ArrayAdapter<Card> {

    private Context context;
    private int resource;
    private int lastPosition = -1;

    private static class ViewHolder {
        TextView locationName;
        TextView time;
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
        return convertView;
    }

}
