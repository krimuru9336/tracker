package com.example.tracker2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class DataActivity extends AppCompatActivity {
    MyDbAdapter helper;

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view_layout);

        helper = new MyDbAdapter(this);
        listView = (ListView) findViewById(R.id.listView);

        ArrayList<Card> list = new ArrayList<>();

        CustomListAdapter adapter = new CustomListAdapter(this, R.layout.activity_data, list);

        ArrayList<ArrayList<Object>> data = helper.getData();
        for (ArrayList<Object> object : data) {

            String locationName = object.get(0).toString();
            double lat = (double) object.get(1);
            double longi = (double) object.get(2);
            String time = object.get(3).toString();
            list.add(new Card(locationName,time));

//            textView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    //helper.showMap(new LatLng(lat,longi));
//                    Intent intent = new Intent(v.getContext(), MapsActivity.class);
//                    Bundle bundle = new Bundle();
//                    bundle.putString("Latitude", lat+"");
//                    bundle.putString("Longitude", longi+"");
//                    intent.putExtras(bundle);
//                    setResult(RESULT_OK, intent);
//                    finish();
//                }
//            });

        }
        listView.setAdapter(adapter);
    }
}