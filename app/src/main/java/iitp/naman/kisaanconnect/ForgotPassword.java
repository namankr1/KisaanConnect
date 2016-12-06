package iitp.naman.kisaanconnect;

/**
 * Created by naman on 25-Nov-16.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class ForgotPassword extends AppCompatActivity {
    private EditText inputOtp;
    private EditText inputNewpassword;
    private Button btnVerify;
    private Button btnResend;
    private String inputPhone1;
    SharedPreferences.Editor e;
    SharedPreferences sf;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgotpassword);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(" ");
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        inputOtp = (EditText) findViewById(R.id.otp);
        inputNewpassword = (EditText) findViewById(R.id.oldpassword);
        btnVerify = (Button) findViewById(R.id.verify);
        btnResend = (Button) findViewById(R.id.resend);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            inputPhone1 = extras.getString("phoneno");
        }

        btnResend.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent upanel = new Intent(getApplicationContext(), ForgotPassword.class);
                upanel.putExtra("phoneno", inputPhone1);
                startActivity(upanel);
                finish();
            }
        });

        btnVerify.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (!inputOtp.getText().toString().equals("")) {
                    NetAsync(view);
                } else {
                    Toast.makeText(getApplicationContext(), "Otp cannot be empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    public void onBackPressed() {
        Intent upanel = new Intent(getApplicationContext(), PasswordReset.class);
        startActivity(upanel);
        finish();
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
                Intent upanel = new Intent(getApplicationContext(), PasswordReset.class);
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
            nDialog = MyCustomProgressDialog.ctor(ForgotPassword.this);
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

    private class ProcessRegister extends AsyncTask<String,Void,Boolean> {

        private ProgressDialog pDialog;
        private Boolean resultserver=false;
        private String inputOtp1;
        private String inputNewpassword1;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            inputOtp1 = inputOtp.getText().toString();
            inputNewpassword1 = inputNewpassword.getText().toString();
            pDialog = MyCustomProgressDialog.ctor(ForgotPassword.this);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(String... args) {

            JSONObject jsonIn = new JSONObject();
            try {
                jsonIn.put("phone",inputPhone1);
                jsonIn.put("otp",inputOtp1);
                jsonIn.put("newpassword",inputNewpassword1);
                RequestQueue que = Volley.newRequestQueue(getApplicationContext());
                String urlString = getResources().getString(R.string.network_url)+"forgotpassword/";
                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, urlString, jsonIn,
                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    String status = response.getString("status");
                                    if (status.compareTo("ok") == 0) {
                                        resultserver=true;
                                        sf = getSharedPreferences("yaad",MODE_PRIVATE);
                                        e = sf.edit();
                                        e.putBoolean("rm", true);
                                        e.putString("phonenum", inputPhone1);
                                        e.putString("passwordnum","");
                                        e.commit();
                                        Intent upanel = new Intent(getApplicationContext(), Home.class);
                                        upanel.putExtra("phoneno", inputPhone1);
                                        Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_SHORT).show();
                                        startActivity(upanel);
                                        pDialog.dismiss();
                                        finish();
                                    }
                                    else if(status.compareTo("err") == 0){
                                        Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_SHORT).show();
                                        pDialog.dismiss();
                                    }
                                    else{
                                        Toast.makeText(getApplicationContext(), "Connection fail", Toast.LENGTH_SHORT).show();
                                        pDialog.dismiss();
                                    }
                                } catch (JSONException e) {
                                    Toast.makeText(getApplicationContext(), "Connection fail", Toast.LENGTH_SHORT).show();
                                    pDialog.dismiss();
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Connection fail", Toast.LENGTH_SHORT).show();
                        pDialog.dismiss();
                    }
                });
                que.add(jsonObjReq);

            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Connection fail", Toast.LENGTH_SHORT).show();
                pDialog.dismiss();
                return null;
            }
            return resultserver;
        }
        @Override
        protected void onPostExecute(Boolean json) {


        }
    }
    public void NetAsync(View view){
        new NetCheck().execute();
    }
}