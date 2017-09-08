package com.amit.a20170803.whereismyfood.APIS;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import com.amit.a20170803.whereismyfood.WSClasses.Menus;

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
 * Created by to5y on 12/08/2017.
 */

public class MenuAPI extends AsyncTask<String, Void, ArrayList<Menus>> {


    private OnTaskComplete onTaskComplete;
    private ProgressDialog progressDialog;
    String branchId;

    public MenuAPI(ProgressDialog progressDialog, OnTaskComplete onTaskComplete, String branchId) {
        this.onTaskComplete = onTaskComplete;
        this.progressDialog = progressDialog;
        this.branchId = branchId;
    }

    @Override
    protected void onPreExecute() {
        //super.onPreExecute();
        progressDialog.setMessage("Loading...");

        progressDialog.setCancelable(false);

        progressDialog.show();
    }





    @Override
    protected ArrayList<Menus> doInBackground(String... strings) {
        ArrayList<Menus> menus = new ArrayList<Menus>();

        try
        {

            HttpClient client = new DefaultHttpClient();

            HttpGet request = new HttpGet("http://amit-learning.com/myPizza/Api_v2.php?function=getMenuData&branchID="+branchId);


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
                    String price = jsonObject.getString("price");
                    String type = jsonObject.getString("type");
                    String desc = jsonObject.getString("description");

                    Menus retUser = new Menus(id, name, price ,type ,desc,branchId );
                    menus.add(retUser);
                }
            }
        }
        catch(Exception e)
        {
            Log.d("UsersAPI", "Exception");
            Log.d("Message", e.getMessage());
        }
        return menus;
    }




    @Override
    protected void onPostExecute(ArrayList<Menus> menus) {
        progressDialog.dismiss();
        onTaskComplete.onComplete();
        super.onPostExecute(menus);
    }

}
