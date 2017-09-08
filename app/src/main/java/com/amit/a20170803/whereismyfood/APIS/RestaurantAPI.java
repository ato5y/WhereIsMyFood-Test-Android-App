package com.amit.a20170803.whereismyfood.APIS;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import com.amit.a20170803.whereismyfood.WSClasses.Restaurant;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by to5y on 08/08/2017.
 */

public class RestaurantAPI extends AsyncTask<String, Void, ArrayList<Restaurant>> {


    private OnTaskComplete onTaskComplete;
    private ProgressDialog progressDialog;


    public RestaurantAPI(ProgressDialog progressDialog, OnTaskComplete onTaskComplete) {
        this.onTaskComplete = onTaskComplete;
        this.progressDialog = progressDialog;
    }


    @Override
    protected void onPreExecute() {
        //super.onPreExecute();
        progressDialog.setMessage("Loading...");

        progressDialog.setCancelable(false);

        progressDialog.show();
    }

    @Override
    protected ArrayList<Restaurant> doInBackground(String... strings) {
        ArrayList<Restaurant> restaurants = new ArrayList<Restaurant>();

        try
        {

            HttpClient client = new DefaultHttpClient();

            HttpGet request = new HttpGet("http://amit-learning.com/myPizza/Api_v2.php?function=getRestaurantData");


            String authorizationString = "Basic " + Base64.encodeToString(   //"Basic" fixed
                    ("tester" + ":" + "tm-sdktest").getBytes(),  //for info if it has username & password
                    Base64.NO_WRAP);
            request.setHeader("Authorization", authorizationString); //Authorization fixed

            HttpResponse response = client.execute(request);


            int statusCode = response.getStatusLine().getStatusCode();
            //System.out.println("ttttttttttttttttt bs"+statusCode);
            if(statusCode == 200)
            {

                HttpEntity entity = response.getEntity();
                String result = EntityUtils.toString(entity);
                //System.out.println("ttttttttttttttttt "+result);
                JSONObject jsonObjectDate = new JSONObject(result);
                //System.out.println("ttttttttttttttttt data "+jsonObjectDate.getJSONArray("data"));
                JSONArray jsonArray = new JSONArray(jsonObjectDate.getJSONArray("data").toString());
                //System.out.println("ttttttttttttttttt "+jsonArray.length());
                for(int counter=0 ; counter<jsonArray.length() ; counter++)
                {

                    JSONObject jsonObject = jsonArray.getJSONObject(counter);

                    //System.out.println("ttttttttttttttttt "+jsonObject.toString());

                    String id = jsonObject.getString("id");
                    String name = jsonObject.getString("name");
                    String imgUrl = jsonObject.getString("img_url");

                    Restaurant retUser = new Restaurant(id, name, imgUrl);
                    restaurants.add(retUser);
                }
            }
        }
        catch(Exception e)
        {
            Log.d("UsersAPI", "Exception");
            Log.d("Message", e.getMessage());
        }
        return restaurants;
    }


    @Override
    protected void onPostExecute(ArrayList<Restaurant> restaurants) {
        progressDialog.dismiss();
        onTaskComplete.onComplete();
        super.onPostExecute(restaurants);
    }

}
