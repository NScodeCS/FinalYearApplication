package com.example.fyp5;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

public class RegisterActivity extends AppCompatActivity {

    MaterialEditText number,confirmnumber;
    Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        number = findViewById(R.id.number);
        confirmnumber = findViewById(R.id.confirmnumber);
        register = findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }

    private void registerNewAccount(final String number, final String confirmnumber ){
        final ProgressDialog progressDialog = new ProgressDialog( RegisterActivity.this );
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(false);
        progressDialog.setTitle("Register New User");
        progressDialog.show();
        String uRL = "http:/10.0.2.2/loginregister/register.php";
        StringRequest request = new StringRequest(Request.Method.POST, uRL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("Succesfully Registered")){
                    progressDialog.dismiss();
                    Toast.makeText(RegisterActivity.this, response, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this,MainActivity.class));
                    finish();

                }
                else {
                    progressDialog.dismiss();
                    Toast.makeText(RegisterActivity.this, response, Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(RegisterActivity.this, error.toString(), Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String > param = new HashMap<>();
                param.put("number", number);
                param.put("confirmnumber",confirmnumber);
                return param;

            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(30000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(RegisterActivity.this).addToRequestQueue(request);


                //MySingleton.getInstance(RegisterActivity.this).addToRequestQueue(request);
                 //maybe an error here


    }
}
