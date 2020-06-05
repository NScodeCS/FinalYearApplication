package com.example.fyp5;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.ResultReceiver;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.function.ToLongBiFunction;

public class AppStartActivity extends AppCompatActivity {

    private Button uploadimage;
    private Button gallery;
    private EditText editText;
    private ImageView imageView;
    private Button camera;
private static final int REQUEST_CALL = 1;
private String dial = "112" ;
Button logout;
private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;
private TextView textLatLong, textAddress, textLong;
private ProgressBar progressBar;
private ResultReceiver resultReceiver;
TextView textuser;
TextView work;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_start);
        final SharedPreferences sharedPreferences = getSharedPreferences("UserInfo",MODE_PRIVATE);
        //intent to share user id
        work = findViewById(R.id.work);
        String name = getIntent().getStringExtra("User");
        work.setText(name);


                        //LOCATION FUNCTIONS
        resultReceiver = new AddressResultReceiver(new Handler());
        textLatLong = findViewById(R.id.textLatLong);
        textAddress = findViewById(R.id.textAddress);
        progressBar = findViewById(R.id.progressBar);
        textLong = findViewById(R.id.textLong);


        findViewById(R.id.btnLocation).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(
                        getApplicationContext(),Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){

                        ActivityCompat.requestPermissions(AppStartActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE_LOCATION_PERMISSION);

                }else {

                    getCurrentLocation();
                }

            }
        });

                        //UPLOAD LOCATION  TO MYSQL

        findViewById(R.id.btnUploadLocation).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String txtLong = textLong.getText().toString();
                String txtLatLong = textLatLong.getText().toString();
                String txtAddress = textAddress.getText().toString();
                String txtuser = work.getText().toString();


                if (TextUtils.isEmpty(txtLatLong) || TextUtils.isEmpty(txtAddress) ){
                    Toast.makeText(AppStartActivity.this, "You need to Get Your Current Location", Toast.LENGTH_SHORT).show();
                }else{


                    uploadlocation(txtAddress,txtLatLong,txtuser,txtLong);

                }

            }
        });


                                        //Image from Gallery Upload

        uploadimage = (Button) findViewById(R.id.uploadimage);
        gallery = (Button) findViewById(R.id.gallery);
        imageView = (ImageView) findViewById(R.id.imageView);



findViewById(R.id.gallery).setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
intent.setType("image/*");
startActivityForResult(Intent.createChooser(intent, "Pick an image"),1);

    }
});


                                      // IMAGE UPLOAD

findViewById(R.id.uploadimage).setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        String txtuser = work.getText().toString();

        if (imageView.getDrawable() == null){
            Toast.makeText(AppStartActivity.this, "Please select an image to upload", Toast.LENGTH_SHORT).show();
        }else{
                //Change my picture into BLOB file to upload to mysql
            BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
            Bitmap bmap = drawable.getBitmap();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bmap.compress(Bitmap.CompressFormat.PNG,100,bos);
            byte[] bb = bos.toByteArray();
            String image = Base64.encodeToString(bb,Base64.DEFAULT);
//Pass the Blop picture file as image, and the user to the fuction that stores them
            UploadImage(txtuser,image);


        }


    }
});

                             //TAKE A PICTURE FROM CAMERA

        camera = (Button)findViewById(R.id.camera);
     if(ContextCompat.checkSelfPermission(AppStartActivity.this,Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ){
         ActivityCompat.requestPermissions(AppStartActivity.this, new String[]{
                 Manifest.permission.CAMERA
         },100);
     }
findViewById(R.id.camera).setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(Intent.createChooser(intent, "Pick an image"),100);

    }
});


                                   //LOGOUT FUNCTION
        logout = findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(getResources().getString(R.string.prefLoginState),"Logged Out");
                editor.apply();
                startActivity(new Intent(AppStartActivity.this,MainActivity.class));
                finish();
            }
        });

    }



                                           //CREATE CLASSES AND CALLBACKS

////////////////////////////////////////////////





    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1) {
            ImageView imageView = findViewById(R.id.imageView);
            try {
                InputStream inputStream = getContentResolver().openInputStream(data.getData());
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imageView.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
       else if(resultCode == RESULT_OK && requestCode == 100){
           Bitmap captureImage = (Bitmap) data.getExtras().get("data");
           imageView.setImageBitmap(captureImage);

        }
    }





    private void UploadImage(  final String txtuser,final String image){
        final ProgressDialog progressDialog = new ProgressDialog( AppStartActivity.this );
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(false);
        progressDialog.setTitle("Uploading Your Image To Emergency Services");
        progressDialog.show();
        String uRL = "http:/10.0.2.2/loginregister/uploadimage.php";
        StringRequest request = new StringRequest(Request.Method.POST, uRL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("Succesfully Uploaded your image")){
                    progressDialog.dismiss();
                    Toast.makeText(AppStartActivity.this, response, Toast.LENGTH_SHORT).show();
                    //i can set the activity to go to login after they register or go into the app

                    //startActivity(new Intent(AppStartActivity.this,MainActivity.class));
                    onResume();

                }
                else {
                    progressDialog.dismiss();
                    Toast.makeText(AppStartActivity.this, response, Toast.LENGTH_SHORT).show();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(AppStartActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                Toast.makeText(AppStartActivity.this, "Please your Mobile Data or Wifi", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String > param = new HashMap<>();
                param.put("textuser",txtuser);
                param.put("image",image);
                return param;

            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(30000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(AppStartActivity.this).addToRequestQueue(request);

    }


    //Location Upload to Mysql
    private void uploadlocation(final String textAddress, final String textLatLong, final String textuser,final String textlong){
        final ProgressDialog progressDialog = new ProgressDialog( AppStartActivity.this );
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(false);
        progressDialog.setTitle("Uploading Your Location To Emergency Units");
        progressDialog.show();
        String uRL = "http:/10.0.2.2/loginregister/location.php";
        StringRequest request = new StringRequest(Request.Method.POST, uRL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("Succesfully Uploaded Your Location")){
                    progressDialog.dismiss();
                    Toast.makeText(AppStartActivity.this, response, Toast.LENGTH_SHORT).show();
                    //i can set the activity to go to login after they register or go into the app

                  //startActivity(new Intent(AppStartActivity.this,MainActivity.class));
                    onResume();

                }
                else {
                    progressDialog.dismiss();
                    Toast.makeText(AppStartActivity.this, response, Toast.LENGTH_SHORT).show();
                    Toast.makeText(AppStartActivity.this, "Please activaye your wifi", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
               Toast.makeText(AppStartActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
               //Wee need to give the option to the user to activate his data/wifi
                Toast.makeText(AppStartActivity.this, "Please activaye your wifi", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String > param = new HashMap<>();
                param.put("txtaddress", textAddress);
                param.put("txtLatLong",textLatLong);
                param.put("textuser",textuser);
                param.put("txtlong",textlong);
                return param;

            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(30000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(AppStartActivity.this).addToRequestQueue(request);

    }




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int [] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions,grantResults);
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults.length > 0) {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getCurrentLocation();
            }else{
                Toast.makeText(this, "Permission Denied!", Toast.LENGTH_SHORT).show();
            }

        }

    }


    public void getCurrentLocation() {


        progressBar.setVisibility(View.VISIBLE);

        final LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationServices.getFusedLocationProviderClient(AppStartActivity.this)
                .requestLocationUpdates(locationRequest, new LocationCallback(){

                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        LocationServices.getFusedLocationProviderClient(AppStartActivity.this)
                                .removeLocationUpdates(this);
                        if(locationResult != null && locationResult.getLocations().size() > 0){
                            int latestLocationIndex = locationResult.getLocations().size() - 1;
                            double latitude = locationResult.getLocations().get(latestLocationIndex).getLatitude();
                            double longitude = locationResult.getLocations().get(latestLocationIndex).getLongitude();
                            textLatLong.setText(String.format("Latitude: %s" ,latitude ));
                            textLong.setText(String.format("Longitude: %s",longitude));

                            //);
                            Location location = new Location("providerNA");
                            location.setLatitude(latitude);
                            location.setLongitude(longitude);
                            fetchAddressFromLatLong(location);

                        } else {
                            progressBar.setVisibility(View.GONE);
                        }

                    }
                }, Looper.getMainLooper());


    }
    private void fetchAddressFromLatLong(Location location){
        Intent intent = new Intent(this, FetchAddressIntentService.class);
        intent.putExtra(Constants.RECEIVER, resultReceiver);
        intent.putExtra(Constants.LOCATION_DATA_EXTRA,location);
        startService(intent);
    }

    private class AddressResultReceiver extends ResultReceiver{
         AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            super.onReceiveResult(resultCode, resultData);
            if(resultCode ==  Constants.SUCCESS_RESULT ){
                textAddress.setText(resultData.getString(Constants.RESULT_DATA_KEY));
            }else{
                Toast.makeText(AppStartActivity.this, resultData.getString(Constants.RESULT_DATA_KEY), Toast.LENGTH_SHORT).show();
            }
            progressBar.setVisibility(View.GONE);
        }
    }

private void upload(){
        final ProgressDialog progressDialog = new ProgressDialog(AppStartActivity.this);

}

}
