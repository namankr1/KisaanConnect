package iitp.naman.kisaanconnect;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
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

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    String inputPhone1;
    String serverName;
    String serverPhone;
    String serverType;
    String serverAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.i("Hi","Ther3");
        super.onCreate(savedInstanceState);

        Log.i("Hi","Ther4");
        setContentView(R.layout.home);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            inputPhone1 = extras.getString("phoneno");
        }

        new NetCheck().execute();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent upanel = new Intent(getApplicationContext(), UserNotif.class);
                upanel.putExtra("phoneno", inputPhone1);
                startActivity(upanel);
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
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent upanel = new Intent(getApplicationContext(), Home.class);
            upanel.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            upanel.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            upanel.putExtra("phoneno", inputPhone1);
            startActivity(upanel);
        } else if (id == R.id.nav_my_profile) {
            Intent upanel = new Intent(getApplicationContext(), MyProfile.class);
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
                nDialog.dismiss();
                new ProcessRegister().execute();
            }
            else{
                Toast.makeText(getApplicationContext(), "Cannot Connect to Network", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class ProcessRegister extends AsyncTask<String,Void,JSONObject> {
        private ProgressDialog pDialog;
        JSONObject resultserver=null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Home.this);
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {

            JSONObject jsonIn = new JSONObject();
            try {
                jsonIn.put("phone",inputPhone1);
                RequestQueue que = Volley.newRequestQueue(getApplicationContext());
                String urlString = getResources().getString(R.string.network_url)+"getprofile/";
                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, urlString, jsonIn,
                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    String status = response.getString("status");
                                    if (status.compareTo("ok") == 0) {
                                        JSONObject tempdata =  response.getJSONObject("profile");
                                        serverAddress = tempdata.getString("address");
                                        serverName = tempdata.getString("name");
                                        serverType = tempdata.getString("type").equals("i")?"Individual":"Company";
                                        serverPhone = tempdata.getString("phone");
                                        pDialog.dismiss();
                                    }else if(status.compareTo("err") == 0){
                                        Toast.makeText(getApplicationContext(), "Connection fail", Toast.LENGTH_SHORT).show();
                                        pDialog.dismiss();
                                    }
                                    else{
                                        Toast.makeText(getApplicationContext(), "Connection fail", Toast.LENGTH_SHORT).show();
                                        pDialog.dismiss();
                                    }
                                } catch (JSONException e) {
                                    Toast.makeText(getApplicationContext(), "Connection fail", Toast.LENGTH_SHORT).show();
                                    e.printStackTrace();
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
                return null;
            }
            return resultserver;

        }
        @Override
        protected void onPostExecute(JSONObject response) {

        }
    }
}
