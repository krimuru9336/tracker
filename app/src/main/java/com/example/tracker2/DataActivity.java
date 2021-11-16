package com.example.tracker2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class DataActivity extends AppCompatActivity {
    MyDbAdapter helper;
    TableLayout tableLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);

        helper = new MyDbAdapter(this);
        tableLayout = (TableLayout) findViewById(R.id.tableLayout);

        ArrayList<ArrayList<Object>> data = helper.getData();
        for (ArrayList<Object> object : data) {
            TableRow tbrow = new TableRow(this);
            TextView textView = new TextView(this);
            String title = object.get(0).toString();
            double lat = (double) object.get(1);
            double longi = (double) object.get(2);
            String time = object.get(3).toString();
            textView.setText(title+ " " + lat  + " " + longi  + " " + time);
            tbrow.addView(textView);
            tableLayout.addView(tbrow);
        }
    }
}