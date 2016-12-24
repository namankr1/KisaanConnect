package iitp.naman.kisaanconnect;

/**
 * Created by naman on 01-Dec-16.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
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
import java.net.HttpURLConnection;
import java.net.URL;

public class EndNegotiation extends AppCompatActivity {
    private String notificationsenderphone;
    private String notificationsendername;
    private String notificationsenderaddress;
    private String notificationquantity;
    private String notificationprice;
    private String notificationquoteid;
    private String inputPhone1;

    private int yourstatus=0;

    private TextView quotedprice;
    private TextView quotedquantity;
    private TextView sendername;
    private TextView senderphone;
    private TextView senderaddress;
    private Button btnaccept;
    private Button btnreject;
    private int poschooselan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.endnegotiation);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(" ");
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        SharedPreferences sfchoosenlan = getSharedPreferences("languagechoosen",MODE_PRIVATE);
        poschooselan = sfchoosenlan.getInt("position",0);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            inputPhone1 = extras.getString("inputPhone1");
            notificationprice = extras.getString("notificationprice");
            notificationquantity = extras.getString("notificationquantity");
            notificationquoteid = extras.getString("notificationquoteid");
            notificationsenderaddress = extras.getString("notificationsenderaddress");
            notificationsendername = extras.getString("notificationsendername");
            notificationsenderphone = extras.getString("notificationsenderphone");
        }

        quotedprice=(TextView) findViewById(R.id.price);
        quotedquantity=(TextView) findViewById(R.id.quantity);
        sendername=(TextView) findViewById(R.id.sendername);
        senderphone=(TextView) findViewById(R.id.senderphone);
        senderaddress=(TextView) findViewById(R.id.senderaddress);
        quotedprice.setText(notificationprice);
        quotedquantity.setText(notificationquantity);
        sendername.setText(notificationsendername);
        senderaddress.setText(notificationsenderaddress);
        senderphone.setText(notificationsenderphone);
        btnaccept=(Button) findViewById(R.id.accept);
        btnreject=(Button) findViewById(R.id.reject);

        btnaccept.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                yourstatus=1;
                new NetCheck().execute();
            }
        });
        btnreject.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                yourstatus=-1;
                new NetCheck().execute();
            }
        });
    }

    @Override
    public void onBackPressed() {
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
            nDialog = MyCustomProgressDialog.ctor(EndNegotiation.this);
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
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean th){
            if(th){
                new ProcessNotification().execute();
            }
            else{
                Toast.makeText(getApplicationContext(),getResources().getString(R.string.cantconnect), Toast.LENGTH_SHORT).show();
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
            pDialog = MyCustomProgressDialog.ctor(EndNegotiation.this);
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
                jsonIn.put("status", yourstatus);
                RequestQueue que = Volley.newRequestQueue(getApplicationContext());
                String urlString = getResources().getString(R.string.network_notification) + "endnegotiation/";
                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, urlString, jsonIn,
                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    String status = response.getString("status");
                                    if (status.compareTo("ok") == 0) {
                                        Toast.makeText(getApplicationContext(), response.getString("message").split(";")[poschooselan], Toast.LENGTH_SHORT).show();
                                        pDialog.dismiss();
                                        resultserver=true;
                                        finish();
                                    } else if (status.compareTo("err") == 0) {
                                        Toast.makeText(getApplicationContext(), response.getString("message").split(";")[poschooselan], Toast.LENGTH_SHORT).show();
                                        pDialog.dismiss();
                                    }
                                    else{
                                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.connectionfail), Toast.LENGTH_SHORT).show();
                                        pDialog.dismiss();
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.connectionfail), Toast.LENGTH_SHORT).show();
                                    pDialog.dismiss();
                                }
                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.connectionfail), Toast.LENGTH_SHORT).show();
                        pDialog.dismiss();
                    }
                });
                que.add(jsonObjReq);

            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(),getResources().getString(R.string.connectionfail), Toast.LENGTH_SHORT).show();
                pDialog.dismiss();
                return null;
            }
            return resultserver;
        }

        @Override
        protected void onPostExecute(Boolean json) {

        }
    }
}

