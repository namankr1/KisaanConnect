package iitp.naman.kisaanconnect;

/**
 * Created by naman on 30-Nov-16.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
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

public class MyProfile extends AppCompatActivity{
    private String serverName;
    private String serverPhone;
    private String serverType;
    private String serverAddress;

    private TextView name1;
    private TextView phone1;
    private TextView address1;

    private SharedPreferences.Editor myprofe;
    private SharedPreferences myprofsf;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.myprofile);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            serverPhone=extras.getString("phoneno");
        }

        new ProcessUpdateFromStored().execute();

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(" ");
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent upanel = new Intent(getApplicationContext(), UserNotif.class);
                upanel.putExtra("phoneno", serverPhone);
                startActivity(upanel);
            }
        });

        name1 = (TextView) findViewById(R.id.name);
        phone1  = (TextView) findViewById(R.id.phone);
        address1 = (TextView) findViewById(R.id.address);


    }
    @Override
    protected void onResume() {
        super.onResume();
        //new NetCheck().execute();//only in case of profile update
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.myprofilebase, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                this.finish();
                return true;
            }
            case R.id.change_password: {
                Intent upanel = new Intent(getApplicationContext(), ChangePassword.class);
                upanel.putExtra("phoneno", serverPhone);
                upanel.putExtra("name",serverName);
                upanel.putExtra("address",serverAddress);
                upanel.putExtra("type",serverType);
                startActivity(upanel);
                return true;
            }
            case R.id.update_profile: {
                Intent upanel = new Intent(getApplicationContext(), UpdateProfile.class);
                upanel.putExtra("phoneno", serverPhone);
                upanel.putExtra("name",serverName);
                upanel.putExtra("address",serverAddress);
                upanel.putExtra("type",serverType);
                startActivity(upanel);
                return true;
            }
            case R.id.menuRefresh:
                new NetCheck().execute();
                return true;
            case R.id.get_quotes: {
                Intent upanel = new Intent(getApplicationContext(), GetAllQuotesUser.class);
                upanel.putExtra("phoneno", serverPhone);
                upanel.putExtra("name",serverName);
                upanel.putExtra("address",serverAddress);
                upanel.putExtra("type",serverType);
                startActivity(upanel);
                return true;
            }

            case R.id.get_orders: {
                Intent upanel = new Intent(getApplicationContext(), UserOrder.class);
                upanel.putExtra("phoneno", serverPhone);
                upanel.putExtra("name",serverName);
                upanel.putExtra("address",serverAddress);
                upanel.putExtra("type",serverType);
                startActivity(upanel);
                return true;
            }

            case R.id.get_negotiations: {
                Intent upanel = new Intent(getApplicationContext(), UserNegotiation.class);
                upanel.putExtra("phoneno", serverPhone);
                upanel.putExtra("name",serverName);
                upanel.putExtra("address",serverAddress);
                upanel.putExtra("type",serverType);
                startActivity(upanel);
                return true;
            }

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
            nDialog = MyCustomProgressDialog.ctor(MyProfile.this);
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
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.cantconnect), Toast.LENGTH_SHORT).show();
            }
            nDialog.dismiss();

        }
    }

    private class ProcessRegister extends AsyncTask<String,Void,JSONObject> {
        private ProgressDialog pDialog;
        private JSONObject resultserver=null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = MyCustomProgressDialog.ctor(MyProfile.this);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {

            JSONObject jsonIn = new JSONObject();
            myprofsf = getSharedPreferences("myprof"+serverPhone,MODE_PRIVATE);
            myprofe = myprofsf.edit();
            try {
                jsonIn.put("phone",serverPhone);
                RequestQueue que = Volley.newRequestQueue(getApplicationContext());
                String urlString = getResources().getString(R.string.network_url)+"getprofile/";
                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, urlString, jsonIn,
                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    String status = response.getString("status");
                                    if (status.compareTo("ok") == 0) {
                                        myprofe.putBoolean("alreadypresent",true);
                                        myprofe.putString("jsondata",response.toString());
                                        myprofe.commit();
                                        JSONObject tempdata =  response.getJSONObject("profile");
                                        serverAddress = tempdata.getString("address");
                                        serverName = tempdata.getString("name");
                                        serverType = tempdata.getString("type");
                                        name1.setText(serverName);
                                        address1.setText(serverAddress);
                                        phone1.setText(serverPhone);
                                        pDialog.dismiss();
                                    }else if(status.compareTo("err") == 0){
                                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.connectionfail), Toast.LENGTH_SHORT).show();
                                        pDialog.dismiss();
                                    }
                                    else{
                                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.connectionfail), Toast.LENGTH_SHORT).show();
                                        pDialog.dismiss();
                                    }
                                } catch (JSONException e) {
                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.connectionfail), Toast.LENGTH_SHORT).show();
                                    e.printStackTrace();
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
                pDialog.dismiss();
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.connectionfail), Toast.LENGTH_SHORT).show();
                return null;
            }
            return resultserver;

        }
        @Override
        protected void onPostExecute(JSONObject response) {

        }
    }

    private class ProcessUpdateFromStored extends AsyncTask<String,Void,Boolean> {
        private ProgressDialog pDialog;
        private Boolean resultserver=false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = MyCustomProgressDialog.ctor(MyProfile.this);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(String... args) {
            myprofsf = getSharedPreferences("myprof"+serverPhone,MODE_PRIVATE);
            Boolean alreadypresent1 = myprofsf.getBoolean("alreadypresent",false);
            JSONObject jsondata;
            if(alreadypresent1){
                String strjson = myprofsf.getString("jsondata",null);
                if(strjson!=null){
                    try{
                        jsondata= new JSONObject(strjson);

                        JSONObject tempdata =  jsondata.getJSONObject("profile");
                        serverAddress = tempdata.getString("address");
                        serverName = tempdata.getString("name");
                        serverType = tempdata.getString("type").equals("i")?"Individual":"Company";

                        resultserver=true;
                    }
                    catch(Exception e){
                        e.printStackTrace();

                    }
                }
            }

            return resultserver;
        }
        @Override
        protected void onPostExecute(Boolean response) {
            super.onPostExecute(response);
            if(response) {
                name1.setText(serverName);
                address1.setText(serverAddress);
                phone1.setText(serverPhone);
                pDialog.dismiss();
            }
            else{
                new NetCheck().execute();
                pDialog.dismiss();
            }

        }
    }
}
