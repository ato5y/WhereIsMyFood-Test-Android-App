package com.amit.a20170803.whereismyfood;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amit.a20170803.whereismyfood.WSClasses.Branches;
import com.amit.a20170803.whereismyfood.WSClasses.Menus;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener{

    private GoogleMap mMap;
    ArrayList<Branches> branches;
    Marker marker;
    ArrayList<Menus> myCart;
    String loggedUserId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        loggedUserId = (String) getIntent().getSerializableExtra("UserId");
        myCart = (ArrayList<Menus>) getIntent().getSerializableExtra("MyCart");
        branches = (ArrayList<Branches>) getIntent().getSerializableExtra("AllBranches");
//mMap.setOnMarkerClickListener(this);

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

        for (int i = 0; i < branches.size(); i++) {
            LatLng location = new LatLng(Double.parseDouble(branches.get(i).getLat()), Double.parseDouble(branches.get(i).getLng()));
            marker =  mMap.addMarker(new MarkerOptions().position(location).title(branches.get(i).getName()));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location,10));
         //   marker =  mMap.addMarker(new MarkerOptions().position(location));
            branches.get(i).setMarkerId(marker.getId());

            mMap.setOnMarkerClickListener(this);



            // Add a marker in Sydney and move the camera
            //LatLng sydney = new LatLng(-34, 151);
            //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
            //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        }
     //mMap.animateCamera(CameraUpdateFactory.zoomBy(3));
    }

    @Override
    public boolean onMarkerClick(Marker marker) {



            for(Branches branche : branches) {
                if(marker.getId().equalsIgnoreCase(branche.getMarkerId())) {
                    MarkerClicked(branche.getName(),branche.getAddress(),branche.getId());
                    break;
                }


        }
        return false;
    }

    public void MarkerClicked(String name, String address, final String id) {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.layout_custom_dialog, null);
        final TextView branchName = (TextView) alertLayout.findViewById(R.id.branch_name);
        final TextView branchAddress = (TextView) alertLayout.findViewById(R.id.branch_address);
        branchName.setText(name);
        branchAddress.setText(address);

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Details");
        // this is set the view from XML inside AlertDialog
        alert.setView(alertLayout);
        // disallow cancel of AlertDialog on click of back button and outside touch
        alert.setCancelable(false);
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        alert.setPositiveButton("Go Menu", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Toast.makeText(getBaseContext(), "Username:  Password: ", Toast.LENGTH_SHORT).show();
                Intent goTosecondPage = new Intent(MapsActivity.this, MenuActivity.class);
                goTosecondPage.putExtra("BranchId",id);
                goTosecondPage.putExtra("MyCart",myCart);
                goTosecondPage.putExtra("UserId",loggedUserId);
                startActivity(goTosecondPage);
            }
        });
        AlertDialog dialog = alert.create();
        dialog.show();
    }

}
