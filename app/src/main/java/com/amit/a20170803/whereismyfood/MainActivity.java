package com.amit.a20170803.whereismyfood;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amit.a20170803.whereismyfood.APIS.LoginAPI;
import com.amit.a20170803.whereismyfood.APIS.OnTaskComplete;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity implements OnTaskComplete , View.OnClickListener {

    public final static int MY_PERMISSIONS_REQUEST_INTERNET = 200;
    private static final int REQUEST_SIGNUP = 0;

    EditText email,password;
    Button loginBtn;
    String message = null;
    LoginAPI loginAPI;
    TextInputLayout signupInputLayoutEmail,signupInputLayoutPassword;
    Animation animShake;
    private Vibrator vib;
    TextView signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences myPrefrence = PreferenceManager.getDefaultSharedPreferences(this);
        String retValue = myPrefrence.getString("WhereIsPizzaUser","Mafesh");

        if(retValue.equals("Mafesh")) {////////sharedPref///////////

            email = (EditText) findViewById(R.id.input_email);
            //password = (EditText) findViewById(R.id.input_password);
            loginBtn = (Button) findViewById(R.id.btn_login);
            signupInputLayoutEmail = (TextInputLayout) findViewById(R.id.signup_input_layout_email);
            signupInputLayoutPassword = (TextInputLayout) findViewById(R.id.signup_input_layout_password);
            password = (EditText) findViewById(R.id.signup_input_password);
            signup = (TextView) findViewById(R.id.link_signup);
            String styledText = "<u><font color='white'>No account yet? Create one</font></u>.";
            signup.setText(Html.fromHtml(styledText), TextView.BufferType.SPANNABLE);

            if (ContextCompat.checkSelfPermission(getApplicationContext(),
                    Manifest.permission.INTERNET)
                    != PackageManager.PERMISSION_GRANTED) {


                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.INTERNET}, MY_PERMISSIONS_REQUEST_INTERNET);
            }

            animShake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
            vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

            loginBtn.setOnClickListener(this);
            signup.setOnClickListener(this);

            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        }else{

            Intent goTosecondPage = new Intent(this, RestaurantActivity.class);
            goTosecondPage.putExtra("UserId",retValue);
            startActivity(goTosecondPage);
            finish();
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

        }


    }







    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_INTERNET: {
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
    public void onComplete() {


        try {
            message = loginAPI.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


        if (message.equals("0")){
            Toast.makeText(this,"Invalid user name or password!",Toast.LENGTH_LONG ).show();
        }else{
            SharedPreferences myPrefrence = PreferenceManager.getDefaultSharedPreferences(this);
            myPrefrence.edit().putString("WhereIsPizzaUser",message).commit();
            System.out.println("sssssssssssssss "+message);
            Intent goTosecondPage = new Intent(this, RestaurantActivity.class);
            goTosecondPage.putExtra("UserId",message);
            startActivity(goTosecondPage);
            finish();
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        }



    }

    @Override
    public void onClick(View view) {
        if(view == loginBtn){
            if (!checkEmail()) {
                email.setAnimation(animShake);
                email.startAnimation(animShake);
                vib.vibrate(120);
                return;
            }
            if (!checkPassword()) {
                password.setAnimation(animShake);
                password.startAnimation(animShake);
                vib.vibrate(120);
                return;
            }

            loginAPI = new LoginAPI(new ProgressDialog(this),this);
            loginAPI.execute("login",email.getText().toString(),password.getText().toString());
            signupInputLayoutEmail.setErrorEnabled(false);
            signupInputLayoutPassword.setErrorEnabled(false);


        }else if(view == signup){

            Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
            startActivityForResult(intent, REQUEST_SIGNUP);
            finish();
            overridePendingTransition(R.anim.push_left_in,  R.anim.push_left_out);

        }
    }


    private boolean checkEmail() {
        String email = this.email.getText().toString().trim();
        if (email.isEmpty() || !isValidEmail(email)) {

            signupInputLayoutEmail.setErrorEnabled(true);
            signupInputLayoutEmail.setError("Please enter a Valid Email");
            this.email.setError("Valid Input Required");
            requestFocus(this.email);
            return false;
        }
        signupInputLayoutEmail.setErrorEnabled(false);
        return true;
    }

    private boolean checkPassword() {
        if (password.getText().toString().trim().isEmpty()) {
            signupInputLayoutPassword.setErrorEnabled(true);
            signupInputLayoutPassword.setError("Please enter a Password");
            password.setError("Password Required");
            requestFocus(password);
            return false;
        }
        signupInputLayoutPassword.setErrorEnabled(false);
        return true;
    }


    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

}
