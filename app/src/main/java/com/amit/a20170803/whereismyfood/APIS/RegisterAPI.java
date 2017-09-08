package com.amit.a20170803.whereismyfood.APIS;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

/**
 * Created by to5y on 07/08/2017.
 */

public class RegisterAPI extends AsyncTask<String, Void, String> {

    private OnTaskComplete listener;
    private ProgressDialog loginProgress;

    public RegisterAPI(ProgressDialog loginProgress , OnTaskComplete listener) {
        this.listener = listener;
        this.loginProgress = loginProgress;
    }

    protected void onPreExecute()
    {
        loginProgress.setMessage("Loading...");

        loginProgress.setCancelable(false);

        loginProgress.show();
    };


    @Override
    protected String doInBackground(String... strings) {
        try
        {
            HttpClient client = new DefaultHttpClient();
            HttpPost request = new HttpPost("http://amit-learning.com/myPizza/Api_v2.php");

            JSONObject json = new JSONObject();
            json.put("function", strings[0]);
            json.put("name", strings[1]);
            json.put("email", strings[2]);
            json.put("password",strings[3]);
            json.put("gender",strings[3]);


            StringEntity se = new StringEntity( json.toString());

            se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            request.setEntity(se);

            String authorizationString = "Basic " + Base64.encodeToString(
                    ("tester" + ":" + "tm-sdktest").getBytes(),
                    Base64.NO_WRAP);
            request.setHeader("Authorization", authorizationString);
            HttpResponse response = client.execute(request);
            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity);

            int statusCode = response.getStatusLine().getStatusCode();
            if(statusCode==200 || statusCode==201 )
            {
                JSONObject jsonObject = new JSONObject(result);
                System.out.println("tttttttt "+jsonObject.toString());
                return jsonObject.get("result").toString();
            }

            return String.valueOf(statusCode);
        }
        catch(Exception e)
        {
            Log.d("LoginAPI", "Exception");
            Log.d("Message", e.getMessage());
        }
        return null;
    }


    @Override
    protected void onPostExecute(String result)
    {
        loginProgress.dismiss();
        listener.onComplete();
        super.onPostExecute(result);
    }
}
