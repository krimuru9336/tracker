package com.example.tracker2;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
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
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    MyDbAdapter helper;
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        ArrayList<ArrayList<Object>> data = helper.getData();
        for (ArrayList<Object> object : data) {
            double lat = (double) object.get(1);
            double longi = (double) object.get(2);
            LatLng latLng = new LatLng(lat, longi);
            mMap.addMarker(new MarkerOptions().position(latLng));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 9.0F));
        }
    }

    public void viewData(View view) {
        Intent intent = new Intent(this, DataActivity.class);
        startActivity(intent);
    }

    public void addMarker(View view) {
        Date currentTime = Calendar.getInstance().getTime();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getApplicationContext(), "Permission not granted", Toast.LENGTH_SHORT).show();
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            double lat = location.getLatitude();
                            double longi = location.getLongitude();
                            LatLng latLng = new LatLng(lat, longi);
                            mMap.addMarker(new MarkerOptions().position(latLng));
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 9.0F));
                            helper.insertData("kri", lat, longi, currentTime.toString());
                            Toast.makeText(getApplicationContext(), "Added!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
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
}