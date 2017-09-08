package com.amit.a20170803.whereismyfood.APIS;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import com.amit.a20170803.whereismyfood.WSClasses.Branches;

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
 * Created by to5y on 10/08/2017.
 */

public class BranchesAPI  extends AsyncTask<String, Void, ArrayList<Branches>> {


    private OnTaskComplete onTaskComplete;
    private ProgressDialog progressDialog;
    String resId;

    public BranchesAPI(ProgressDialog progressDialog, OnTaskComplete onTaskComplete, String resId) {
        this.onTaskComplete = onTaskComplete;
        this.progressDialog = progressDialog;
        this.resId = resId;
    }


    @Override
    protected void onPreExecute() {
        //super.onPreExecute();
        progressDialog.setMessage("Loading...");

        progressDialog.setCancelable(false);

        progressDialog.show();
    }


    @Override
    protected ArrayList<Branches> doInBackground(String... strings) {
        ArrayList<Branches> branches = new ArrayList<Branches>();

        try
        {

            HttpClient client = new DefaultHttpClient();

            HttpGet request = new HttpGet("http://amit-learning.com/myPizza/Api_v2.php?function=getBranchesData&resID="+resId);


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

                    String id = jsonObject.getString("id");
                    String name = jsonObject.getString("name");
                    String lat = jsonObject.getString("latitude");
                    String lng = jsonObject.getString("longitude");
                    String address = jsonObject.getString("address");

                    Branches retUser = new Branches(id, name, lat ,lng ,address ,resId);
                    branches.add(retUser);
                }
            }
        }
        catch(Exception e)
        {
            Log.d("UsersAPI", "Exception");
            Log.d("Message", e.getMessage());
        }
        return branches;
    }



    @Override
    protected void onPostExecute(ArrayList<Branches> branches) {
        progressDialog.dismiss();
        onTaskComplete.onComplete();
        super.onPostExecute(branches);
    }

}
