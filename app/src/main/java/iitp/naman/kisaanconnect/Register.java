package iitp.naman.kisaanconnect;

/**
 * Created by naman on 30-Sep-16.
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Register extends AppCompatActivity {


    /**
     * Defining layout items.
     **/

    EditText inputName;
    EditText inputPhone;
    EditText inputPassword;
    EditText inputCPassword;
    EditText inputAddress;
    EditText inputCity;
    EditText inputZip;

    Button btnRegister;
    Button btnCancel;
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Password reset");
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        /**
         * Defining all layout items
         **/
        inputName = (EditText) findViewById(R.id.name);
        inputPhone = (EditText) findViewById(R.id.phone);
        inputPassword = (EditText) findViewById(R.id.password);
        inputCPassword = (EditText) findViewById(R.id.cpassword);
        inputAddress = (EditText) findViewById(R.id.address);
        inputCity = (EditText) findViewById(R.id.city);
        inputZip = (EditText) findViewById(R.id.zip);

        btnRegister = (Button) findViewById(R.id.register);
        btnCancel = (Button) findViewById(R.id.cancel);

/**
 * Button which Switches back to the login screen on clicked
 **/

        btnCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), Login.class);
                startActivityForResult(myIntent, 0);
                finish();
            }

        });

        /**
         * Register Button click event.
         * A Toast is set to alert when the fields are empty.
         * Another toast is set to alert Username must be 5 characters.
         **/

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (  ( !inputName.getText().toString().equals("")) && ( !inputPhone.getText().toString().equals("")) && ( !inputPassword.getText().toString().equals("")) && ( !inputCPassword.getText().toString().equals("")) && ( !inputAddress.getText().toString().equals("")) && ( !inputCity.getText().toString().equals(""))&& ( !inputZip.getText().toString().equals("")))
                {
                    if ( inputPhone.getText().toString().length() == 10 ){
                        NetAsync(view);

                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),
                                "Phone Number Should be 10 digits", Toast.LENGTH_SHORT).show();
                    }
                }
                else if(!inputPassword.equals(inputCPassword))
                {
                    Toast.makeText(getApplicationContext(),
                            "Password and Confirm Password Do Not Match", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),
                            "One or more fields are empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.govtnotification, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent upanel = new Intent(getApplicationContext(), Login.class);

                startActivity(upanel);
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Async Task to check whether internet connection is working
     **/

    private class NetCheck extends AsyncTask<String, Void, Boolean>
    {
        private ProgressDialog nDialog;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            nDialog = new ProgressDialog(Register.this);
            nDialog.setMessage("Loading..");
            nDialog.setTitle("Checking Network");
            nDialog.setIndeterminate(false);
            nDialog.setCancelable(true);
            nDialog.show();
        }

        @Override
        protected Boolean doInBackground(String... args){

/**
 * Gets current device state and checks for working internet connection by trying Google.
 **/
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()) {
                try {
                    URL url = new URL(getResources().getString(R.string.network_check));
                    HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                    urlc.setConnectTimeout(3000);
                    urlc.connect();
                    if (urlc.getResponseCode() == 200) {
                        return true;
                    }
                } catch (MalformedURLException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            return false;

        }
        @Override
        protected void onPostExecute(Boolean th){

            if(th == true){
                nDialog.dismiss();
                new ProcessRegister().execute();
            }
            else{
                Toast.makeText(getApplicationContext(), "Cannot Connect to Network",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    class ProcessRegister extends AsyncTask<String,Void,JSONObject> {

        /**
         * Defining Process dialog
         **/
        private ProgressDialog pDialog;

        String inputName1,inputPhone1,inputPassword1,inputAddress1,inputCity1,inputZip1;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            inputName1 = inputName.getText().toString();
            inputPhone1 = inputPhone.getText().toString();
            inputPassword1 = inputPassword.getText().toString();
            inputAddress1 = inputAddress.getText().toString();
            inputCity1 = inputCity.getText().toString();
            inputZip1 = inputZip.getText().toString();

            pDialog = new ProgressDialog(Register.this);
            pDialog.setTitle("Contacting Servers");
            pDialog.setMessage("Registering ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {

            JSONObject jsonIn = new JSONObject();
            try {
                jsonIn.put("firstName",inputName1);
                jsonIn.put("lastName"," ");
                jsonIn.put("phone",inputPhone1);
                jsonIn.put("address",inputAddress1+", "+inputCity1+", "+inputZip1);
                jsonIn.put("password",inputPassword1);


            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "login Failed", Toast.LENGTH_LONG).show();
                return null;
            }
            return jsonIn;

        }
        @Override
        protected void onPostExecute(JSONObject json) {
            /**
             * Checks for success message.
             **/
            RequestQueue que = Volley.newRequestQueue(getApplicationContext());
                String urlString = getResources().getString(R.string.network_url)+"signup/";
                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, urlString, json,
                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    String status = response.getString("status");
                                    Log.i("Response :","Status : "+status);
                                    if (status.compareTo("ok") == 0) {
                                        Log.i("Status Ok :","Sending Otp ");
                                        pDialog.setMessage("Sending Otp");
                                        pDialog.setTitle("Otp");
                                        Log.i("Status Before upanel :","Sending Otp ");
                                        Intent upanel = new Intent(getApplicationContext(), Otp.class);
                                        upanel.putExtra("phoneno", inputPhone1);
                                        pDialog.dismiss();
                                        startActivity(upanel);
                                    }else if(status.compareTo("err") == 0){
                                        Toast.makeText(getApplicationContext(),
                                                response.getString("message") ,
                                                Toast.LENGTH_LONG).show();
                                        pDialog.dismiss();
                                    }
                                    else{
                                        pDialog.setMessage("Server Connection Denied");
                                        pDialog.setTitle("Exit");
                                        pDialog.dismiss();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(getApplicationContext(),
                                getString(R.string.login_failed),
                                Toast.LENGTH_LONG).show();
                    }
                });
            que.add(jsonObjReq);
        }
    }
    public void NetAsync(View view){
        new NetCheck().execute();
    }}