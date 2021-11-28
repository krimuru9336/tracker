package com.example.tracker2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Objects;

public class MyDbAdapter {
    MyDbHelper myhelper;

    public MyDbAdapter(Context context) {
        myhelper = new MyDbHelper(context);
    }

    public long insertData(String locationName, double lat, double longi, String time) {
        SQLiteDatabase dbb = myhelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MyDbHelper.LOCATION_NAME, locationName);
        contentValues.put(MyDbHelper.LATITUDE, lat);
        contentValues.put(MyDbHelper.LONGITUDE, longi);
        contentValues.put(MyDbHelper.TIME, time);
        long id = dbb.insert(MyDbHelper.TABLE_NAME, null, contentValues);
        return id;
    }

    public void showMap(String locationName, LatLng latLng, GoogleMap mMap) {

        MarkerOptions markerOptions = new MarkerOptions().position(latLng);
        markerOptions.title(locationName);
        Objects.requireNonNull(mMap.addMarker(markerOptions)).showInfoWindow();
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 9.0F));
//        Marker.showInfoWindow();
    }

    public ArrayList<ArrayList<Object>> getData() {
//        insertHistoricData();
        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] columns = {MyDbHelper.UID, MyDbHelper.LOCATION_NAME, MyDbHelper.LATITUDE, MyDbHelper.LONGITUDE, MyDbHelper.TIME};
        Cursor cursor = db.query(MyDbHelper.TABLE_NAME, columns, null, null, null, null, null);

        ArrayList<ArrayList<Object>> locationArray = new ArrayList<>();

        while (cursor.moveToNext()) {
            try {
                ArrayList<Object> tempList = new ArrayList<Object>();
//                int cid = 1;
                tempList.add(cursor.getString(cursor.getColumnIndexOrThrow(MyDbHelper.LOCATION_NAME)));
                tempList.add(cursor.getDouble(cursor.getColumnIndexOrThrow(MyDbHelper.LATITUDE)));
                tempList.add(cursor.getDouble(cursor.getColumnIndexOrThrow(MyDbHelper.LONGITUDE)));
                tempList.add(cursor.getString(cursor.getColumnIndexOrThrow(MyDbHelper.TIME)));
                locationArray.add(tempList);

            } catch (Exception e) {
                Toast.makeText(myhelper.context, "Error in fetching values", Toast.LENGTH_SHORT).show();
            }
        }
        return locationArray;
    }

    static class MyDbHelper extends SQLiteOpenHelper {
        private static final String DATABASE_NAME = "mapDB";    // Database Name
        private static final String TABLE_NAME = "history";   // Table Name
        private static final int DATABASE_Version = 1;    // Database Version
        private static final String UID = "_id";     // Column 1 (Primary Key)
        private static final String LOCATION_NAME = "LocationName"; // Column 2
        private static final String LATITUDE = "Latitude";    //Column 3
        private static final String LONGITUDE = "Longitude";    // Column 4
        private static final String TIME = "Time";    // Column 5
        private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME +
                " (" + UID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + LOCATION_NAME + " VARCHAR(300)," +
                LATITUDE + " DECIMAL(3,2) ," + LONGITUDE + " DECIMAL(3,2), " + TIME + " VARCHAR(300)" + ");";
        private static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
        private Context context;

        public MyDbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_Version);
            this.context = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(CREATE_TABLE);
                Toast.makeText(context, "Table Created", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            try {
                Toast.makeText(context, "On Upgrade", Toast.LENGTH_SHORT).show();
                db.execSQL(DROP_TABLE);
//                onCreate(db);
            } catch (Exception e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }


    //    public int delete(String uname) {
//        SQLiteDatabase db = myhelper.getWritableDatabase();
//        String[] whereArgs = {uname};
//
//        int count = db.delete(MyDbHelper.TABLE_NAME, MyDbHelper.NAME + " = ?", whereArgs);
//        return count;
//    }
//
//    public int updateName(String oldName, String newName) {
//        SQLiteDatabase db = myhelper.getWritableDatabase();
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(MyDbHelper.NAME, newName);
//        String[] whereArgs = {oldName};
//        int count = db.update(MyDbHelper.TABLE_NAME, contentValues, MyDbHelper.NAME + " = ?", whereArgs);
//        return count;
//    }
    public void insertHistoricData() {
        SQLiteDatabase dbb = myhelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        for (int i = 0; i < 3; i++) {
            contentValues.put(MyDbHelper.LOCATION_NAME, "LocationName" + 50.55 + i);
            contentValues.put(MyDbHelper.LATITUDE, 50.55 + i);
            contentValues.put(MyDbHelper.LONGITUDE, 9.68 + i);
            contentValues.put(MyDbHelper.TIME, "Time" + 9.68 + i);
            long id = dbb.insert(MyDbHelper.TABLE_NAME, null, contentValues);
        }
    }

}
