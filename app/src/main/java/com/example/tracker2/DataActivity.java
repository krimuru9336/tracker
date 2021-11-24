package com.example.tracker2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //helper.showMap(new LatLng(lat,longi));
                    Intent intent = new Intent(v.getContext(), MapsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("Latitude", lat+"");
                    bundle.putString("Longitude", longi+"");
//                    intent.putExtra("isReturn","true");
//                    intent.putExtra("Latitude",lat);
//                    intent.putExtra("Longitude",longi);
                    intent.putExtras(bundle);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            });
            tbrow.addView(textView);
            tableLayout.addView(tbrow);
        }
    }
}