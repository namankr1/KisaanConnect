package iitp.naman.kisaanconnect;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import android.app.Activity;
import android.content.Intent;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;


import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    String inputPhone1;
    String serverName;
    String serverPhone;
    String serverType;
    String serverAddress;

    String[] notificationid = new String[] {""};
    String[] notificationquoteid = new String[] {"No new Notifications"};
    String[] notificationprice = new String[] {""};
    String[] notificationquantity = new String[] {""};
    String[] notificationsenderphone= new String[] {""};
    String[] notificationsendername= new String[] {""};
    String[] notificationsenderaddress= new String[] {""};
    String[] notificationstatus= new String[] {""};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.home);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            inputPhone1 = extras.getString("phoneno");
        }


        new NetCheck().execute();
        new ProcessNotification().execute();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int len=notificationid.length;
                String notifall="";
                for(int i=0;i<len;i++){
                    notifall+=notificationid[i]+" "+notificationquoteid[i]+" "+notificationprice[i]+"\n";
                }

                Snackbar snackbar = Snackbar.make(view, notifall, Snackbar.LENGTH_LONG)
                        .setAction("Action", null);
                View snackbarView = snackbar.getView();
                TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setMaxLines(len);
                snackbar.show();

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
            Intent upanel = new Intent(getApplicationContext(), Home.class);

            upanel.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            upanel.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            upanel.putExtra("phoneno", inputPhone1);
            startActivity(upanel);
        } else if (id == R.id.nav_my_profile) {
            Intent upanel = new Intent(getApplicationContext(), MyProfile.class);
            upanel.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            upanel.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            upanel.putExtra("phoneno", inputPhone1);
            upanel.putExtra("name",serverName);
            upanel.putExtra("address",serverAddress);
            upanel.putExtra("type",serverType);
            startActivity(upanel);

        } else if (id == R.id.nav_gov_not) {
            Intent upanel = new Intent(getApplicationContext(), GovtNotification.class);
            upanel.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            upanel.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            upanel.putExtra("phoneno", inputPhone1);
            startActivity(upanel);

        } else if (id == R.id.nav_buy) {
           Intent upanel = new Intent(getApplicationContext(), Buy.class);
            upanel.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            upanel.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            upanel.putExtra("phoneno", inputPhone1);
            startActivity(upanel);

        } else if (id == R.id.nav_sell) {
            Intent upanel = new Intent(getApplicationContext(), Sell.class);
            upanel.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            upanel.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            upanel.putExtra("phoneno", inputPhone1);
            startActivity(upanel);

        } else if (id == R.id.nav_logout) {
            Intent upanel = new Intent(getApplicationContext(), Login.class);
            //upanel.putExtra("phoneno", inputPhone1);
            startActivity(upanel);

        }
        else if (id == R.id.nav_exit) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private class NetCheck extends AsyncTask<String, Void, Boolean>
    {
        private ProgressDialog nDialog;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            nDialog = new ProgressDialog(Home.this);
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

    private class ProcessRegister extends AsyncTask<String,Void,JSONObject> {

        /**
         * Defining Process dialog
         **/
        private ProgressDialog pDialog;

        String inputOtp1;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Home.this);
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
                jsonIn.put("phone",inputPhone1);


            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Connection fail", Toast.LENGTH_LONG).show();
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
            String urlString = getResources().getString(R.string.network_url)+"getprofile/";
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, urlString, json,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                String status = response.getString("status");
                                Log.i("Response :","Status : "+status);
                                if (status.compareTo("ok") == 0) {
                                    Log.i("Status Ok :","Loading User Space ");
                                    pDialog.setMessage("Loading User Space");
                                    pDialog.setTitle("Getting Data");
                                    Log.i("Response1 :","Status : "+status);
                                    JSONObject tempdata =  response.getJSONObject("profile");
                                    Log.i("Response2 :","Status : "+status);
                                    serverAddress = tempdata.getString("address");
                                    serverName = tempdata.getString("name");
                                    Log.i("Response3 :","Status : "+status);
                                    serverType = tempdata.getString("type").equals("i")?"Individual":"Company";
                                    serverPhone = tempdata.getString("phone");
                                    Log.i("Response4 :","Status : "+serverPhone+serverType+serverName+serverAddress);
                                    pDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), "Welcome " + serverName,
                                            Toast.LENGTH_LONG).show();

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

    private class ProcessNotification extends AsyncTask<String,Void,JSONObject> {

        /**
         * Defining Process dialog
         **/
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Home.this);
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

                jsonIn.put("phone",inputPhone1);

            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Connection fail", Toast.LENGTH_LONG).show();
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
            String urlString = getResources().getString(R.string.network_notification)+"getnotifications/";
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, urlString, json,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                String status = response.getString("status");
                                Log.i("Response :","Status :1 "+status);
                                if (status.compareTo("ok") == 0) {
                                    Log.i("Status Ok :2","Loading User Space ");
                                    pDialog.setMessage("Loading User Space");
                                    pDialog.setTitle("Getting Data");

                                    /*
                                    Intent upanel = new Intent(getApplicationContext(), Buy.class);
                                    upanel.putExtra("phoneno", inputPhone1);

                                    pDialog.dismiss();

                                    startActivity(upanel);
                                    */

                                    Log.i("Response1 :3","Status : "+status);
                                    JSONArray tempdata =  response.getJSONArray("notifications");
                                    int len=tempdata.length();
                                    if(len!=0) {
                                        notificationid = new String[len];
                                        notificationprice = new String[len];
                                        notificationquantity = new String[len];
                                        notificationquoteid = new String[len];
                                        notificationsenderphone = new String[len];
                                        notificationsendername  = new String[len];
                                        notificationsenderaddress  = new String[len];
                                        notificationstatus = new String[len];

                                        for (int i = 0; i < len; i++) {
                                            notificationid[i] = tempdata.getJSONObject(i).getString("id");
                                            JSONObject temp = tempdata.getJSONObject(i).getJSONObject("sender");
                                            notificationsenderphone[i] = temp.getString("phone");
                                            notificationsendername[i] = temp.getString("name");
                                            notificationsenderaddress[i] = temp.getString("address");
                                            notificationquantity[i] = tempdata.getJSONObject(i).getString("quantity");
                                            notificationprice[i] = tempdata.getJSONObject(i).getString("price");
                                            notificationquoteid[i] = tempdata.getJSONObject(i).getString("quoteid");
                                            notificationstatus[i] = tempdata.getJSONObject(i).getString("status");
                                            Log.i("Response4 :4", "Status : got notification");

                                        }

                                        //insert category details here

                                    }
                                    pDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), "Notification caught up ",
                                            Toast.LENGTH_LONG).show();

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
    }
}
