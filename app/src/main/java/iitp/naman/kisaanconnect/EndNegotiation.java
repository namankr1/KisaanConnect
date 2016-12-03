package iitp.naman.kisaanconnect;

/**
 * Created by naman on 01-Dec-16.
 */

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.app.ProgressDialog;
import android.os.AsyncTask;
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

public class EndNegotiation extends AppCompatActivity {
    private String notificationid;
    private String notificationsenderphone;
    private String notificationsendername;
    private String notificationsenderaddress;
    private String notificationquantity;
    private String notificationprice;
    private String notificationquoteid;
    private String notificationstatus;
    private String inputPhone1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.endnegotiation);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("End Negotiation");
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            inputPhone1 = extras.getString("inputPhone1");
            notificationid = extras.getString("notificationid");
            notificationprice = extras.getString("notificationprice");
            notificationquantity = extras.getString("notificationquantity");
            notificationquoteid = extras.getString("notificationquoteid");
            notificationsenderaddress = extras.getString("notificationsenderaddress");
            notificationsendername = extras.getString("notificationsendername");
            notificationsenderphone = extras.getString("notificationsenderphone");
            notificationstatus = extras.getString("notificationstatus");
        }
        new NetCheck().execute();
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
                Intent upanel = new Intent(getApplicationContext(), UserNotif.class);
                upanel.putExtra("phoneno", inputPhone1);
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
            nDialog = new ProgressDialog(EndNegotiation.this);
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
                }
                catch (MalformedURLException e1) {
                    e1.printStackTrace();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean th){
            if(th == true){
                new ProcessNotification().execute();
            }
            else{
                Toast.makeText(getApplicationContext(), "Cannot Connect to Network", Toast.LENGTH_SHORT).show();
            }
            nDialog.dismiss();
        }
    }

    private class ProcessNotification extends AsyncTask<String, Void, Boolean> {
        private ProgressDialog pDialog;
        private Boolean resultserver=false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(EndNegotiation.this);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(String... args) {
            JSONObject jsonIn = new JSONObject();

            try {
                jsonIn.put("senderphone", inputPhone1);
                jsonIn.put("recieverphone", notificationsenderphone);
                jsonIn.put("quoteid", notificationquoteid);
                jsonIn.put("status", notificationstatus);
                RequestQueue que = Volley.newRequestQueue(getApplicationContext());
                String urlString = getResources().getString(R.string.network_notification) + "endnegotiation/";
                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, urlString, jsonIn,
                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    String status = response.getString("status");
                                    if (status.compareTo("ok") == 0) {
                                        Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_SHORT).show();
                                        Intent upanel = new Intent(getApplicationContext(), UserNotif.class);
                                        upanel.putExtra("phoneno", inputPhone1);
                                        startActivity(upanel);
                                        finish();
                                        resultserver=true;
                                    } else if (status.compareTo("err") == 0) {
                                        Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_SHORT).show();
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
            return resultserver;
        }

        @Override
        protected void onPostExecute(Boolean json) {
            pDialog.dismiss();
        }
    }
}

