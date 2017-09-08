package com.amit.a20170803.whereismyfood;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputLayout;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.amit.a20170803.whereismyfood.APIS.LoginAPI;
import com.amit.a20170803.whereismyfood.APIS.OnTaskComplete;
import com.amit.a20170803.whereismyfood.APIS.RegisterAPI;

import java.util.concurrent.ExecutionException;

public class RegisterActivity extends AppCompatActivity implements OnTaskComplete, View.OnClickListener {

    String message = null;
    String selectedRadioButtonText;
    private Vibrator vib;
    Animation animShake;
    private EditText signupInputName, signupInputEmail, signupInputPassword;
    private TextInputLayout signupInputLayoutName, signupInputLayoutEmail, signupInputLayoutPassword;
    private Button btnSignUp;
    TextView loginLink;
    RegisterAPI registerAPI;
    RadioGroup genderRadioGroup;
    RadioButton selectedRadioButton;
    LoginAPI loginAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        signupInputLayoutName = (TextInputLayout) findViewById(R.id.signup_input_layout_name);
        signupInputLayoutEmail = (TextInputLayout) findViewById(R.id.signup_input_layout_email);
        signupInputLayoutPassword = (TextInputLayout) findViewById(R.id.signup_input_layout_password);

        signupInputName = (EditText) findViewById(R.id.signup_input_name);
        signupInputEmail = (EditText) findViewById(R.id.signup_input_email);
        signupInputPassword = (EditText) findViewById(R.id.signup_input_password);
        btnSignUp = (Button) findViewById(R.id.btn_signup);
        loginLink = (TextView) findViewById(R.id.link_login);
        genderRadioGroup = (RadioGroup) findViewById(R.id.gender_radio);

        String styledText = "<u><font color='white'>Already a member? Login</font></u>.";
        loginLink.setText(Html.fromHtml(styledText), TextView.BufferType.SPANNABLE);

        animShake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
        vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        btnSignUp.setOnClickListener(this);
        loginLink.setOnClickListener(this);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

    }


    @Override
    public void onClick(View view) {
        if(view == btnSignUp) {
            if (!checkName()) {
                signupInputName.setAnimation(animShake);
                signupInputName.startAnimation(animShake);
                vib.vibrate(120);
                return;
            }
            if (!checkEmail()) {
                signupInputEmail.setAnimation(animShake);
                signupInputEmail.startAnimation(animShake);
                vib.vibrate(120);
                return;
            }
            if (!checkPassword()) {
                signupInputPassword.setAnimation(animShake);
                signupInputPassword.startAnimation(animShake);
                vib.vibrate(120);
                return;
            }


            int selectedRadioButtonID = genderRadioGroup.getCheckedRadioButtonId();
            if (selectedRadioButtonID != -1) {

                selectedRadioButton = (RadioButton) findViewById(selectedRadioButtonID);
                selectedRadioButtonText = selectedRadioButton.getText().toString();
            }
            registerAPI = new RegisterAPI(new ProgressDialog(this),this);
            registerAPI.execute("register",signupInputName.getText().toString(),signupInputEmail.getText().toString(),signupInputPassword.getText().toString(),selectedRadioButtonText);
            signupInputLayoutName.setErrorEnabled(false);
            signupInputLayoutEmail.setErrorEnabled(false);
            signupInputLayoutPassword.setErrorEnabled(false);
        }if(view == loginLink){

            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

        }
    }

    private boolean checkName() {
        if (signupInputName.getText().toString().trim().isEmpty()) {

            signupInputLayoutName.setErrorEnabled(true);
            signupInputLayoutName.setError("Please enter a Name");
            signupInputName.setError("Valid Input Required");
            return false;
        }
        signupInputLayoutName.setErrorEnabled(false);
        return true;
    }

    private boolean checkEmail() {
        String email = signupInputEmail.getText().toString().trim();
        if (email.isEmpty() || !isValidEmail(email)) {

            signupInputLayoutEmail.setErrorEnabled(true);
            signupInputLayoutEmail.setError("Please enter a Valid Email");
            signupInputEmail.setError("Valid Input Required");
            requestFocus(signupInputEmail);
            return false;
        }
        signupInputLayoutEmail.setErrorEnabled(false);
        return true;
    }

    private boolean checkPassword() {
        if (signupInputPassword.getText().toString().trim().isEmpty()) {
            signupInputLayoutPassword.setErrorEnabled(true);
            signupInputLayoutPassword.setError("Please Enter a Password");
            signupInputPassword.setError("Password Required");
            requestFocus(signupInputPassword);
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

    @Override
    public void onComplete() {

        try {
            message = registerAPI.get();
//            Toast.makeText(this,message,Toast.LENGTH_LONG ).show();
            if(message.equals("1")){
                loginAPI = new LoginAPI(new ProgressDialog(this),this);
                loginAPI.execute("login",signupInputEmail.getText().toString(),signupInputPassword.getText().toString());
                String x = loginAPI.get();
                SharedPreferences myPrefrence = PreferenceManager.getDefaultSharedPreferences(this);
                myPrefrence.edit().putString("WhereIsPizzaUser",x).commit();

                Intent goTosecondPage = new Intent(this, RestaurantActivity.class);
                goTosecondPage.putExtra("UserId",x);
                startActivity(goTosecondPage);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);


            }else{
            Toast.makeText(this,"This mail is already exist!",Toast.LENGTH_LONG ).show();
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
