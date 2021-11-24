package com.example.tracker2;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.text.Editable;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.tracker2.databinding.ActivityMapsBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    MyDbAdapter helper;
    private FusedLocationProviderClient fusedLocationClient;
    View popupInputDialogView;
    String latitude, isReturn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_Tracker2);
        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        SharedPreferences sharedPreferences = getSharedPreferences("preferences", MODE_PRIVATE);

        if (sharedPreferences.getBoolean("firstStart", true)) {
            showStartDialog();
        }
        LayoutInflater layoutInflater = LayoutInflater.from(MapsActivity.this);
        popupInputDialogView = layoutInflater.inflate(R.layout.get_title_popup, null);

        int locationRQ = 101;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            checkPermissions(Manifest.permission.ACCESS_FINE_LOCATION, "Location", locationRQ);
            checkPermissions(Manifest.permission.ACCESS_COARSE_LOCATION, "Location", locationRQ);
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        helper = new MyDbAdapter(this);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void showStartDialog() {

        AlertDialog.Builder alert = new AlertDialog.Builder(MapsActivity.this);
        final EditText edittext = new EditText(this);

        alert.setTitle("Wo bist du?");

        alert.setView(edittext);

        alert.show();

        SharedPreferences sharedPreferences = getSharedPreferences("preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("firstStart", false);
        editor.apply();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                String strEditText = data.getStringExtra("isReturn");
                Toast.makeText(getApplicationContext(), strEditText, Toast.LENGTH_SHORT).show();
            }
        }
    }

    //    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 1) {
//            if(resultCode == RESULT_OK) {
//                String strEditText = data.getStringExtra("isReturn");
//                Toast.makeText(getApplicationContext(), strEditText, Toast.LENGTH_SHORT).show();
//            }
//        }
//    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        ArrayList<ArrayList<Object>> data = helper.getData();
        for (ArrayList<Object> object : data) {
            double lat = (double) object.get(1);
            double longi = (double) object.get(2);
            helper.showMap(new LatLng(lat, longi),mMap);
        }
    }

    public void viewData(View view) {
        Intent intent = new Intent(this, DataActivity.class);
        startActivity(intent);
    }

    public void show(LatLng latLng) {
        Toast.makeText(getApplicationContext(), "Hi", Toast.LENGTH_LONG).show();

    }



    public void addMarker(View view) {

        AlertDialog.Builder alert = new AlertDialog.Builder(MapsActivity.this);
        final EditText edittext = new EditText(this);

        alert.setTitle("Wo bist du?");

        alert.setView(edittext);

        alert.setPositiveButton("Add!", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                String title = edittext.getText().toString();
                getCurrentLocation(title);
            }
        });

        alert.show();
    }
     private void getCurrentLocation(String locationName) {

        LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getApplicationContext(), "Permission not granted", Toast.LENGTH_SHORT).show();
            return;
        }

        if(lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                // Logic to handle location object
                                double lat = location.getLatitude();
                                double longi = location.getLongitude();
                                helper.showMap(new LatLng(lat, longi),mMap);
                                insertDataIntoDB(locationName,lat,longi);
                            }
                        }
                    });
        }
        else {
            Toast.makeText(getApplicationContext(), "Please turn on your location", Toast.LENGTH_SHORT).show();
        }
    }

    private void insertDataIntoDB(String locationName, double lat, double longi) {
        helper.insertData(locationName, lat, longi, Calendar.getInstance().getTime().toString());
        Toast.makeText(getApplicationContext(), "Added!", Toast.LENGTH_SHORT).show();
    }

    private void checkPermissions(String permission, String name, int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), permission) == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(), name + " Permission Granted", Toast.LENGTH_SHORT).show();
                shouldShowRequestPermissionRationale(permission);
                showDialog(permission, name, requestCode);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length == 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getApplicationContext(), "Permissions refused " + requestCode, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "Permissions granted " + requestCode, Toast.LENGTH_SHORT).show();
        }
    }

    private void showDialog(String permission, String name, int requestCode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        Toast.makeText(getApplicationContext(), "IN SHOW DIALOG", Toast.LENGTH_SHORT).show();
        new AlertDialog.Builder(this)
                .setTitle("Grant Location Permission")
                .setMessage("This will let you save your location using your GPS")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Continue with delete operation
                        ActivityCompat.requestPermissions(MapsActivity.this, new String[]{permission}, requestCode);
                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }



//    public void checkLocation() {
//
//        boolean gps_enabled;
//        try {
//            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
//        }catch (Exception ex){}
//        boolean network_enabled;
//        try{
//            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
//        }catch (Exception ex){}
//        if(!gps_enabled && !network_enabled){
//            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
//            dialog.setMessage(getResources().getString(R.string.gps_network_not_enabled));
//            dialog.setPositiveButton(getResources().getString(R.string.open_location_settings), new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
//                    Intent myIntent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                    Startup.this.startActivity(myIntent);
//                }
//            });
//            dialog.setNegativeButton(getString(R.string.Cancel), new DialogInterface.OnClickListener() {
//
//                @Override
//                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
//                    // TODO Auto-generated method stub
//
//                }
//            });
//            dialog.show();
//        }
//    }
}