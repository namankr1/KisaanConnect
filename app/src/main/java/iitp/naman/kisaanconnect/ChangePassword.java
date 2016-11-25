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

import android.util.Log;
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
import java.util.HashMap;

public class ChangePassword extends Activity {

    private static String KEY_SUCCESS = "success";
    private static String KEY_ERROR = "error";

    EditText newpass;
    EditText oldpass;
    TextView alert;
    Button changepass;
    Button cancel;
    String phonen;
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.changepassword);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
           phonen = extras.getString("phoneno");
        cancel = (Button) findViewById(R.id.btcancel);
        cancel.setOnClickListener(new View.OnClickListener(){
            public void onClick(View arg0){

                Intent login = new Intent(getApplicationContext(), Home.class);

                startActivity(login);
                finish();
            }

        });

        newpass = (EditText) findViewById(R.id.newpass);
            oldpass = (EditText) findViewById(R.id.oldpass) ;
        alert = (TextView) findViewById(R.id.alertpass);
        changepass = (Button) findViewById(R.id.btchangepass);

        changepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NetAsync(view);
            }
        });}}

    private class NetCheck extends AsyncTask<String, Void, Boolean>
    {
        private ProgressDialog nDialog;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            nDialog = new ProgressDialog(ChangePassword.this);
            nDialog.setMessage("Loading..");
            nDialog.setTitle("Checking Network");
            nDialog.setIndeterminate(false);
            nDialog.setCancelable(true);
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
                nDialog.dismiss();
                alert.setText("Error in Network Connection");
            }
        }
    }

    private class ProcessRegister extends AsyncTask<String,Void,JSONObject> {

        private ProgressDialog pDialog;
        private static final String LOG_TAG = "MyActivity";
        String newpas;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            newpas = newpass.getText().toString();

            pDialog = new ProgressDialog(ChangePassword.this);
            pDialog.setTitle("Contacting Servers");
            pDialog.setMessage("Getting Data ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {

            JSONObject jsonIn = new JSONObject();
            try {
                jsonIn.put("phone", phonen);
                jsonIn.put("oldpassword",oldpass);
                jsonIn.put("newpassword", newpas);
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(),"login Failed", Toast.LENGTH_LONG).show();
                return null;
            }

            return jsonIn;

        }

        @Override
        protected void onPostExecute(JSONObject json) {
            if (json != null) {

                RequestQueue que = Volley.newRequestQueue(getApplicationContext());
                Log.i(LOG_TAG, "getting profile jsonIn :" + json.toString());

                String urlString = getResources().getString(R.string.network_url)+"changepassword/";
                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, urlString, json,
                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    String status = response.getString("status");
                                    if (status.compareTo("ok") == 0) {

                                        pDialog.dismiss();
                                        alert.setText("Your Password is successfully changed.");
                                        Intent upanel = new Intent(getApplicationContext(), Login.class);
                                        upanel.putExtra("phoneno", phonen);
                                        startActivity(upanel);
                                    } else if (status.compareTo("err") == 0) {
                                        String error = response.getString("err");
                                        alert.setText(error);
                                        Toast.makeText(getApplicationContext(),
                                                getString(R.string.login_failed),
                                                Toast.LENGTH_LONG).show();
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

        }}
    public void NetAsync(View view){
        new NetCheck().execute();
    }}