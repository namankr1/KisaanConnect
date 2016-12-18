package iitp.naman.kisaanconnect;

/**
 * Created by naman on 30-Nov-16.
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
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


public class Sell extends AppCompatActivity {

    private GridView gridView;
    private String inputPhone1;
    private String[] categoryname = new String[] {};
    private String[] categorydescription = new String[] {};
    private String[] categoryid = new String[] {};
    private String[] categorypicture = new String[] {};
    private SharedPreferences.Editor buye;
    private SharedPreferences buysf;
    private Activity myactivity;



    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.buy);
        myactivity = this;


        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle( getResources().getString(R.string.javasell_1));
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            inputPhone1 = extras.getString("phoneno");
        }

        new ProcessUpdateFromStored().execute();

        gridView = (GridView) findViewById(R.id.gridView1);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                String categoryid = ((TextView) v.findViewById(R.id.grid_item_id)).getText()+"";
                String categoryname =((TextView) v.findViewById(R.id.grid_item_label)).getText()+"";
                Intent upanel = new Intent(getApplicationContext(), sellSubcategory.class);
                upanel.putExtra("phoneno", inputPhone1);
                upanel.putExtra("category",categoryid);
                upanel.putExtra("categoryname",categoryname);
                startActivity(upanel);
            }
        });

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent upanel = new Intent(getApplicationContext(), UserNotif.class);
                upanel.putExtra("phoneno", inputPhone1);
                startActivity(upanel);

            }
        });
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
            nDialog = MyCustomProgressDialog.ctor(Sell.this);
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
                Toast.makeText(getApplicationContext(),  getResources().getString(R.string.cantconnect), Toast.LENGTH_SHORT).show();
            }
            nDialog.dismiss();
        }
    }

    private class ProcessRegister extends AsyncTask<String,Void,JSONObject> {

        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = MyCustomProgressDialog.ctor(Sell.this);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {

            JSONObject jsonIn = new JSONObject();
            buysf = getSharedPreferences("categoriestore",MODE_PRIVATE);
            buye = buysf.edit();
            try {
                jsonIn.put("phone",inputPhone1);
                RequestQueue que = Volley.newRequestQueue(getApplicationContext());
                String urlString = getResources().getString(R.string.network_url_category)+"getcategories/";
                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, urlString, jsonIn,
                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    String status = response.getString("status");
                                    if (status.compareTo("ok") == 0) {
                                        buye.putBoolean("alreadypresent",true);
                                        buye.putString("jsondata",response.toString());
                                        buye.commit();
                                        JSONArray tempdata =  response.getJSONArray("categories");
                                        int len=tempdata.length();
                                        categoryname =new String[len];
                                        categoryid=new String[len];
                                        categorypicture=new String[len];
                                        categorydescription=new String[len];

                                        for(int i=0;i<len;i++){
                                            categoryname[i]=tempdata.getJSONObject(i).getString("name");
                                            categoryid[i]=tempdata.getJSONObject(i).getString("id");
                                            categorydescription[i]=tempdata.getJSONObject(i).getString("description");
                                            categorypicture[i]=tempdata.getJSONObject(i).getString("picture");
                                        }

                                        gridView.setAdapter(new ImageAdapter(myactivity,getApplicationContext(), categoryname,categoryid,categorypicture));
                                        pDialog.dismiss();
                                    } else if (status.compareTo("err") == 0) {
                                        Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_SHORT).show();
                                        pDialog.dismiss();
                                    }
                                    else {
                                        Toast.makeText(getApplicationContext(),  getResources().getString(R.string.connectionfail), Toast.LENGTH_SHORT).show();
                                        pDialog.dismiss();
                                    }
                                }
                                catch (JSONException e) {
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
            }
            catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(),  getResources().getString(R.string.connectionfail), Toast.LENGTH_SHORT).show();
                pDialog.dismiss();
                return null;
            }
            return jsonIn;
        }
        @Override
        protected void onPostExecute(JSONObject response) {

        }
    }

    public void NetAsync(View view){
        new NetCheck().execute();
    }

    private class ProcessUpdateFromStored extends AsyncTask<String,Void,Boolean> {
        private ProgressDialog pDialog;
        private Boolean resultserver=false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = MyCustomProgressDialog.ctor(Sell.this);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(String... args) {
            buysf = getSharedPreferences("categoriestore",MODE_PRIVATE);
            Boolean alreadypresent1 = buysf.getBoolean("alreadypresent",false);
            JSONObject jsondata;
            if(alreadypresent1){
                String strjson = buysf.getString("jsondata",null);
                if(strjson!=null){
                    try{
                        jsondata= new JSONObject(strjson);
                        JSONArray tempdata = jsondata.getJSONArray("categories");
                        int len = tempdata.length();
                        categoryname = new String[len];
                        categoryid = new String[len];
                        categorypicture = new String[len];
                        categorydescription = new String[len];
                        for (int i = 0; i < len; i++) {
                            categoryname[i] = tempdata.getJSONObject(i).getString("name");
                            categoryid[i] = tempdata.getJSONObject(i).getString("id");
                            categorydescription[i] = tempdata.getJSONObject(i).getString("description");
                            categorypicture[i] =tempdata.getJSONObject(i).getString("picture");
                        }
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
                gridView.setAdapter(new ImageAdapter(myactivity,getApplicationContext(), categoryname, categoryid, categorypicture));
                pDialog.dismiss();
            }
            else{
                new NetCheck().execute();
                pDialog.dismiss();
            }

        }
    }
}
