package iitp.naman.kisaanconnect;

/**
 * Created by naman on 30-Sep-16.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

    private EditText inputName;
    private EditText inputPhone;
    private EditText inputPassword;
    private EditText inputCPassword;
    private EditText inputAddress;
    private EditText inputCity;
    private EditText inputZip;

    Button btnRegister;

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

        inputName = (EditText) findViewById(R.id.name);
        inputPhone = (EditText) findViewById(R.id.phone);
        inputPassword = (EditText) findViewById(R.id.password);
        inputCPassword = (EditText) findViewById(R.id.cpassword);
        inputAddress = (EditText) findViewById(R.id.address);
        inputCity = (EditText) findViewById(R.id.city);
        inputZip = (EditText) findViewById(R.id.zip);
        btnRegister = (Button) findViewById(R.id.register);


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
                        Toast.makeText(getApplicationContext(), "Phone Number Should be 10 digits", Toast.LENGTH_SHORT).show();
                    }
                }
                else if(!inputPassword.equals(inputCPassword))
                {
                    Toast.makeText(getApplicationContext(), "Password and Confirm Password Do Not Match", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "One or more fields are empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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

    private class NetCheck extends AsyncTask<String, Void, Boolean>
    {
        private ProgressDialog nDialog;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            nDialog = new ProgressDialog(Register.this);
            nDialog.setCancelable(false);
            nDialog.show();
        }

        @Override
        protected Boolean doInBackground(String... args){
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
                    e1.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return false;

        }
        @Override
        protected void onPostExecute(Boolean th){

            if(th == true){
                new ProcessRegister().execute();
            }
            else{
                Toast.makeText(getApplicationContext(), "Cannot Connect to Network", Toast.LENGTH_SHORT).show();
            }
            nDialog.dismiss();

        }
    }

    class ProcessRegister extends AsyncTask<String,Void,JSONObject> {

        private ProgressDialog pDialog;

        private String inputName1,inputPhone1,inputPassword1,inputAddress1,inputCity1,inputZip1;

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(Register.this);
            pDialog.setCancelable(false);
            pDialog.show();
            super.onPreExecute();
            inputName1 = inputName.getText().toString();
            inputPhone1 = inputPhone.getText().toString();
            inputPassword1 = inputPassword.getText().toString();
            inputAddress1 = inputAddress.getText().toString();
            inputCity1 = inputCity.getText().toString();
            inputZip1 = inputZip.getText().toString();

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
                RequestQueue que = Volley.newRequestQueue(getApplicationContext());
                String urlString = getResources().getString(R.string.network_url)+"signup/";
                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, urlString, jsonIn,
                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    String status = response.getString("status");
                                    if (status.compareTo("ok") == 0) {
                                        Intent upanel = new Intent(getApplicationContext(), Otp.class);
                                        upanel.putExtra("phoneno", inputPhone1);
                                        startActivity(upanel);
                                        Toast.makeText(getApplicationContext(), response.getString("message") , Toast.LENGTH_SHORT).show();
                                        finish();
                                    }else if(status.compareTo("err") == 0){
                                        Toast.makeText(getApplicationContext(), response.getString("message") , Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        Toast.makeText(getApplicationContext(), "Connection fail", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(getApplicationContext(), "Connection fail", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Connection fail", Toast.LENGTH_SHORT).show();
                    }
                });
                que.add(jsonObjReq);


            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Connection fail", Toast.LENGTH_SHORT).show();
                return null;
            }
            return jsonIn;

        }
        @Override
        protected void onPostExecute(JSONObject json) {
            pDialog.dismiss();

        }
    }
    public void NetAsync(View view){
        new NetCheck().execute();
    }
}