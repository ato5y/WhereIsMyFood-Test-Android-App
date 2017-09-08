package com.amit.a20170803.whereismyfood;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.amit.a20170803.whereismyfood.WSClasses.Menus;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PickUpMapActivity extends FragmentActivity implements OnMapReadyCallback ,View.OnClickListener {
    ArrayList<Menus> myCart;
    private GoogleMap mMap;
    private Marker marker;
    private Button pickUpAddress;
    private String pinAddress;
    private String loggedUserId;
    private String myCurrentAddress;
    private LatLng pinAddressLatLng;
    private LatLng myCurrentLatLng;
    public final static int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_up_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        loggedUserId = (String) getIntent().getSerializableExtra("UserId");
        myCart = (ArrayList<Menus>) getIntent().getSerializableExtra("MyCart");

        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CONTACTS)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }

        pickUpAddress = (Button) findViewById(R.id.pick_up_btn);
        pickUpAddress.setOnClickListener(this);


    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        /*LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/
        //mMap.animateCamera(CameraUpdateFactory.zoomIn());
        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationChangeListener(myLocationChangeListener);
        // Setting a click event handler for the map
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng latLng) {

                // Creating a marker
                MarkerOptions markerOptions = new MarkerOptions();

                // Setting the position for the marker
                markerOptions.position(latLng);

                // Setting the title for the marker.
                // This will be displayed on taping the marker
                //markerOptions.title(latLng.latitude + " : " + latLng.longitude);
                markerOptions.title(getAddress(latLng));
                pinAddress=getAddress(latLng);
                pinAddressLatLng = latLng;
                // Clears the previously touched position
                mMap.clear();

                // Animating to the touched position
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

                // Placing a marker on the touched position
                marker = mMap.addMarker(markerOptions);
            }
        });
    }

    /////////////////////Get Current Location///////////////////////
    private GoogleMap.OnMyLocationChangeListener myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
        @Override
        public void onMyLocationChange(Location location) {
            LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
            myCurrentAddress = getAddress(loc);
            myCurrentLatLng = loc;
        }
    };

    /////////////////////Get Address///////////////////////
    public String getAddress(LatLng loc){
        //LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
        //mMarker = mMap.addMarker(new MarkerOptions().position(loc));
        //mMap.addMarker(new MarkerOptions().position(loc));
        if(mMap != null){
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 16.0f));
        }
        Geocoder gCoder = new Geocoder(this, Locale.getDefault());//new Geocoder(getApplicationContext()); //new Geocoder(this, getResources().getConfiguration().locale);
        //ArrayList<Address> addresses = gCoder.getFromLocation(123456789, 123456789, 1);
        List<Address> addresses = null;
        try {
            addresses =  gCoder.getFromLocation((loc.latitude), ( loc.longitude), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        /*if (addresses != null && addresses.size() > 0) {
            //Toast.makeText(getApplicationContext(), "country: " + addresses.get(0).getSubLocality(), Toast.LENGTH_LONG).show();
            //System.out.println("eeeeeeeee AdminArea() "+addresses.get(0).getCountryName());
            //System.out.println("eeeeeeeee AdminArea() "+addresses.get(0).getAdminArea());
            //System.out.println("eeeeeeeee FeatureName() "+addresses.get(0).getFeatureName());
            //System.out.println("eeeeeeeee SubAdminArea() "+addresses.get(0).getSubAdminArea());
        }*/

        return addresses.get(0).getFeatureName()+" - "+addresses.get(0).getSubAdminArea()+" - "+addresses.get(0).getAdminArea()+" - "+addresses.get(0).getCountryName();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                }
                return;
            }
        }
    }

    @Override
    public void onClick(View view) {
        if (view == pickUpAddress){
            if (pinAddress == null){
                //Toast.makeText(getApplicationContext(), "in if ", Toast.LENGTH_LONG).show();
                Intent goTosecondPage = new Intent(this, MyCartActivity.class);
                goTosecondPage.putExtra("MyCart",myCart);
                goTosecondPage.putExtra("UserId",loggedUserId);
                goTosecondPage.putExtra("Address",myCurrentAddress);
                goTosecondPage.putExtra("Lat",myCurrentLatLng.latitude);
                goTosecondPage.putExtra("Lng",myCurrentLatLng.longitude);
                startActivity(goTosecondPage);
            }else{
                //Toast.makeText(getApplicationContext(), "in else ", Toast.LENGTH_LONG).show();
                Intent goTosecondPage = new Intent(this, MyCartActivity.class);
                goTosecondPage.putExtra("MyCart",myCart);
                goTosecondPage.putExtra("UserId",loggedUserId);
                goTosecondPage.putExtra("Address",pinAddress);
                goTosecondPage.putExtra("Lat",pinAddressLatLng.latitude);
                goTosecondPage.putExtra("Lng",pinAddressLatLng.longitude);
                startActivity(goTosecondPage);
            }
        }
    }

    @Override
    public void onBackPressed()
    {
    }

}
