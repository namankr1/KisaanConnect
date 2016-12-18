package iitp.naman.kisaanconnect;

/**
 * Created by naman on 15-Dec-16.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
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
import java.net.HttpURLConnection;
import java.net.URL;

public class UserNegotiation extends AppCompatActivity {

    private GridView gridView;
    private String serverPhone;

    private String[] notificationid = new String[]{""};
    private String[] notificationquoteid = new String[]{"No new Notifications"};
    private String[] notificationprice = new String[]{""};
    private String[] notificationquantity = new String[]{""};
    private String[] notificationsenderphone = new String[]{""};
    private String[] notificationsendername = new String[]{""};
    private String[] notificationsenderaddress = new String[]{""};
    private String[] notificationstatus = new String[]{""};
    private String[] notificationtype = new String[]{""};
    private Activity myactivity;

    private SharedPreferences.Editor userquotee;
    private SharedPreferences userquotesf;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.negotiations);
        myactivity=this;

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            serverPhone=extras.getString("phoneno");
        }

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("My Negotiations");
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        gridView = (GridView) findViewById(R.id.gridView12);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent upanel = new Intent(getApplicationContext(), UserNotif.class);
                upanel.putExtra("phoneno", serverPhone);
                startActivity(upanel);
            }
        });
    }
    @Override
    protected void onResume(){
        super.onResume();
        new NetCheck1().execute();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.refreshnotification, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.menuRefresh:
                NetAsync(this.findViewById(android.R.id.content));
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
            nDialog = MyCustomProgressDialog.ctor(UserNegotiation.this);
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
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return false;
        }
        @Override
        protected void onPostExecute(Boolean th){

            if(th){
                new ProcessRegister().execute();
            }
            else{
                Toast.makeText(getApplicationContext(),  getResources().getString(R.string.connectionfail), Toast.LENGTH_SHORT).show();
            }
            nDialog.dismiss();
        }
    }

    private class ProcessRegister extends AsyncTask<String,Void,Boolean> {
        private ProgressDialog pDialog;
        private Boolean resultserver=false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = MyCustomProgressDialog.ctor(UserNegotiation.this);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(String... args) {
            JSONObject jsonIn = new JSONObject();
            userquotesf = getSharedPreferences("usernegotiation"+serverPhone,MODE_PRIVATE);
            userquotee = userquotesf.edit();
            try {
                jsonIn.put("phone",serverPhone);
                RequestQueue que = Volley.newRequestQueue(getApplicationContext());
                String urlString = getResources().getString(R.string.network_notification)+"getnegotiationsofuser/";
                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, urlString, jsonIn,
                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    String status = response.getString("status");
                                    if (status.compareTo("ok") == 0) {
                                        userquotee.putBoolean("alreadypresent",true);
                                        userquotee.putString("jsondata",response.toString());
                                        userquotee.commit();
                                        JSONArray tempdata =  response.getJSONArray("negotiations");
                                        int len = tempdata.length();

                                        notificationid = new String[len];
                                        notificationprice = new String[len];
                                        notificationquantity = new String[len];
                                        notificationquoteid = new String[len];
                                        notificationsenderphone = new String[len];
                                        notificationsendername = new String[len];
                                        notificationsenderaddress = new String[len];
                                        notificationstatus = new String[len];
                                        notificationtype = new String[len];

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
                                            notificationtype[i] = tempdata.getJSONObject(i).getString("type");

                                        }
                                        resultserver=true;
                                        gridView.setAdapter(new UserNegotiationAdapter(myactivity, getApplicationContext(), notificationid, notificationsendername,  notificationquantity, notificationprice, notificationstatus, notificationtype));

                                        if(len>0){
                                            pDialog.dismiss();
                                        }
                                        else{
                                            pDialog.dismiss();
                                            AlertDialog.Builder builder = new AlertDialog.Builder(UserNegotiation.this);
                                            builder.setMessage(getResources().getString(R.string.javausernegotiation_1))
                                                    .setCancelable(false)
                                                    .setPositiveButton(getResources().getString(R.string.javausernegotiation_2), new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            dialog.cancel();
                                                        }
                                                    });
                                            AlertDialog alert = builder.create();
                                            alert.show();
                                        }

                                    }else if(status.compareTo("err") == 0){
                                        Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_SHORT).show();
                                        pDialog.dismiss();
                                    }
                                    else{
                                        Toast.makeText(getApplicationContext(),  getResources().getString(R.string.connectionfail), Toast.LENGTH_SHORT).show();
                                        pDialog.dismiss();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(getApplicationContext(),  getResources().getString(R.string.connectionfail), Toast.LENGTH_SHORT).show();
                                    pDialog.dismiss();
                                }
                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),  getResources().getString(R.string.connectionfail), Toast.LENGTH_SHORT).show();
                        pDialog.dismiss();
                    }
                });
                que.add(jsonObjReq);

            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(),  getResources().getString(R.string.connectionfail), Toast.LENGTH_SHORT).show();
                pDialog.dismiss();
                return false;
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


    private class ProcessUpdateFromStored extends AsyncTask<String,Void,String> {
        private ProgressDialog pDialog;
        private String resultserver="0";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = MyCustomProgressDialog.ctor(UserNegotiation.this);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            userquotesf = getSharedPreferences("usernegotiation"+serverPhone,MODE_PRIVATE);
            Boolean alreadypresent1 = userquotesf.getBoolean("alreadypresent",false);
            JSONObject jsondata;
            if(alreadypresent1){
                String strjson = userquotesf.getString("jsondata",null);
                if(strjson!=null){
                    try{
                        jsondata= new JSONObject(strjson);


                        JSONArray tempdata =  jsondata.getJSONArray("negotiations");
                        int len = tempdata.length();

                        notificationid = new String[len];
                        notificationprice = new String[len];
                        notificationquantity = new String[len];
                        notificationquoteid = new String[len];
                        notificationsenderphone = new String[len];
                        notificationsendername = new String[len];
                        notificationsenderaddress = new String[len];
                        notificationstatus = new String[len];
                        notificationtype = new String[len];

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
                            notificationtype[i] = tempdata.getJSONObject(i).getString("type");

                        }

                        if(len>0){
                            resultserver="1";
                        }
                        else{
                            resultserver="2";
                        }
                    }
                    catch(Exception e){
                        e.printStackTrace();

                    }
                }
            }

            return resultserver;
        }
        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);

            try {
                switch (Integer.parseInt(response)) {
                    case 1:
                        gridView.setAdapter(new UserNegotiationAdapter(myactivity, getApplicationContext(), notificationid, notificationsendername,  notificationquantity, notificationprice, notificationstatus, notificationtype));
                        pDialog.dismiss();
                        break;
                    case 2:
                        gridView.setAdapter(new UserNegotiationAdapter(myactivity, getApplicationContext(), notificationid, notificationsendername,  notificationquantity, notificationprice, notificationstatus, notificationtype));
                        pDialog.dismiss();
                        AlertDialog.Builder builder = new AlertDialog.Builder(UserNegotiation.this);
                        builder.setMessage(getResources().getString(R.string.javausernegotiation_1))
                                .setCancelable(false)
                                .setPositiveButton(getResources().getString(R.string.javausernegotiation_2), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                        break;
                    default:
                        new NetCheck().execute();
                        pDialog.dismiss();
                }
            }
            catch (Exception e){
                e.printStackTrace();
                new NetCheck().execute();
                pDialog.dismiss();
            }

        }
    }

    private class NetCheck1 extends AsyncTask<String, Void, Boolean>
    {
        private ProgressDialog nDialog;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            nDialog = MyCustomProgressDialog.ctor(UserNegotiation.this);
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
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return false;

        }
        @Override
        protected void onPostExecute(Boolean th){

            if(th){
                new ProcessRegister().execute();
            }
            else{
                new ProcessUpdateFromStored().execute();
            }

            nDialog.dismiss();
        }
    }
}
