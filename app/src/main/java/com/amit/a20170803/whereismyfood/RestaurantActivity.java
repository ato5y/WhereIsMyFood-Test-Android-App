package com.amit.a20170803.whereismyfood;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.amit.a20170803.whereismyfood.APIS.OnTaskComplete;
import com.amit.a20170803.whereismyfood.APIS.RestaurantAPI;
import com.amit.a20170803.whereismyfood.WSClasses.Menus;
import com.amit.a20170803.whereismyfood.WSClasses.Restaurant;
import com.amit.a20170803.whereismyfood.adapter.RecyclerViewCustomAdapter;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class RestaurantActivity extends AppCompatActivity implements OnTaskComplete {
    RecyclerView listRest;
    //ListView listRest;
    RestaurantAPI restaurantAPI;
    ArrayList<Restaurant> restaurants;
    RecyclerView recyclerView;
    ArrayList<Menus> myCart;
    String loggedUserId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);


        //listRest = (RecyclerView) findViewById(R.id.recyclerview1);
        //listRest = (ListView) findViewById(R.id.listview_test);
        loggedUserId = (String) getIntent().getSerializableExtra("UserId");
        myCart = (ArrayList<Menus>) getIntent().getSerializableExtra("MyCart");
        recyclerView = (RecyclerView) findViewById(R.id.rvAllUsers);

        restaurantAPI = new RestaurantAPI(new ProgressDialog(this),this);
        restaurantAPI.execute();


    }

    @Override
    public void onComplete() {

        try {
            try {

                restaurants = restaurantAPI.get();
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                RecyclerViewCustomAdapter recyclerViewCustomAdapter = new RecyclerViewCustomAdapter(restaurants, this,myCart,loggedUserId);
                recyclerView.setAdapter(recyclerViewCustomAdapter);


            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
