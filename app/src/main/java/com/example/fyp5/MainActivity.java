package com.example.fyp5;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

//declaring Variables
    MaterialEditText number, confirmnumber;
    Button login, register;
    CheckBox loginstate;
    SharedPreferences sharedPreferences;


    @Override

    //connect my vairables with buttons

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        confirmnumber = findViewById(R.id.confirmnumber);
        sharedPreferences = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        number = findViewById(R.id.number);
        register = findViewById(R.id.register);
        login = findViewById(R.id.login);
        loginstate = findViewById(R.id.checkbox);




        //register user and we open RegisterActivity for the user to register
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,RegisterActivity.class));
                finish();

            }
        });
 //set my button listener and calling my fuction with name login
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String txtNumber =  Objects.requireNonNull(number.getText()).toString();

                if (TextUtils.isEmpty(txtNumber)){
                    Toast.makeText(MainActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                }else{

                    login(txtNumber);
                    user();
                }
            }
        }  );


        String loginStatus = sharedPreferences.getString(getResources().getString(R.string.prefLoginState),"");

        if (loginStatus.equals("Logged in")){

            Intent intent = new Intent(this, AppStartActivity.class);
            startActivity(new Intent(getApplicationContext(),AppStartActivity.class));

            //Intent intent = new Intent(this, Verification.class);
          //startActivity(new Intent(getApplicationContext(),Verification.class));
        }


}


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



    private void user() {
        String name = number.getText().toString();
        Intent i = new Intent(getApplicationContext(),AppStartActivity.class);
        i.putExtra("User",name);
        startActivity(i);

    }

    //login fuction declare. Get login info and if user is register we move on to APPSTART
    private void login(final String number){
        final ProgressDialog progressDialog = new ProgressDialog( MainActivity.this );
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(false);
        progressDialog.setTitle("Log in");
        progressDialog.show();
        String uRL = "http:/10.0.2.2/loginregister/login.php";
        StringRequest request = new StringRequest(Request.Method.POST, uRL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response.equals("Login Success")) {

                    progressDialog.dismiss();
                    Toast.makeText(MainActivity.this, response, Toast.LENGTH_SHORT).show();
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    if (loginstate.isChecked()) {

                        editor.putString(getResources().getString(R.string.prefLoginState), "Logged in");
                    } else {
                        editor.putString(getResources().getString(R.string.prefLoginState), "Logged Out");
                    }

                    editor.apply();
                   // startActivity(new Intent(MainActivity.this, AppStartActivity.class));
                    // startActivity(new Intent(MainActivity.this,Verification.class));
                }else if(response.equals("Wrong Number Or you Need to Register")){
                    progressDialog.dismiss();
                    Toast.makeText(MainActivity.this, response, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this,MainActivity.class));

                } else{
                    progressDialog.dismiss();
                    Toast.makeText(MainActivity.this, response, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this,MainActivity.class));
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();

            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String > param = new HashMap<>();
                param.put("number", number);
                return param;

            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(30000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(MainActivity.this).addToRequestQueue(request);

    }

}
