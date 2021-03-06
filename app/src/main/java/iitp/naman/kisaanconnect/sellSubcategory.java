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

public class sellSubcategory extends AppCompatActivity {

    private GridView gridView;
    private String inputPhone1;
    private String category1;
    private String categoryname1;
    private String[] subcategoryname = new String[] {};
    private String[] subcategorydescription = new String[] {};
    private String[] subcategoryid = new String[] {};
    private String[] subcategorypicture = new String[] {};
    private Activity myactivity;


    private SharedPreferences.Editor buysube;
    private SharedPreferences buysubsf;

    private int poschooselan;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.buysubcategory);
        myactivity = this;


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            inputPhone1 = extras.getString("phoneno");
            category1=extras.getString("category");
            categoryname1=extras.getString("categoryname");
        }

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(categoryname1);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        SharedPreferences sfchoosenlan = getSharedPreferences("languagechoosen",MODE_PRIVATE);
        poschooselan = sfchoosenlan.getInt("position",0);

        new ProcessUpdateFromStored().execute();

        gridView = (GridView) findViewById(R.id.gridView1);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                String subcategoryid = ((TextView) v.findViewById(R.id.grid_item_id)).getText()+"";
                String subcategoryname1 = ((TextView) v.findViewById(R.id.grid_item_label)).getText()+"";
                Intent upanel = new Intent(getApplicationContext(), AddQuotesofSubcategory.class);
                upanel.putExtra("phoneno", inputPhone1);
                upanel.putExtra("category",category1);
                upanel.putExtra("categoryname",categoryname1);
                upanel.putExtra("subcategory",subcategoryid);
                upanel.putExtra("subcategoryname",subcategoryname1);
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

    @Override
    public void onBackPressed() {
        finish();
    }

    private class NetCheck extends AsyncTask<String, Void, Boolean>
    {
        private ProgressDialog nDialog;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            nDialog = MyCustomProgressDialog.ctor(sellSubcategory.this);
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
            if(th ){
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
            pDialog = MyCustomProgressDialog.ctor(sellSubcategory.this);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {

            JSONObject jsonIn = new JSONObject();
            buysubsf = getSharedPreferences("subcategoriestore"+category1,MODE_PRIVATE);
            buysube = buysubsf.edit();
            try {
                jsonIn.put("phone",inputPhone1);
                jsonIn.put("categoryid",category1);
                RequestQueue que = Volley.newRequestQueue(getApplicationContext());
                String urlString = getResources().getString(R.string.network_url_category)+"getsubcategories/";
                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, urlString, jsonIn,
                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    String status = response.getString("status");
                                    if (status.compareTo("ok") == 0) {
                                        buysube.putBoolean("alreadypresent",true);
                                        buysube.putString("jsondata",response.toString());
                                        buysube.commit();
                                        JSONArray tempdata =  response.getJSONArray("subcategories");
                                        int len=tempdata.length();
                                        subcategoryname =new String[len];
                                        subcategoryid=new String[len];
                                        subcategorypicture=new String[len];
                                        subcategorydescription=new String[len];

                                        for(int i=0;i<len;i++){
                                            subcategoryname[i]=tempdata.getJSONObject(i).getString("name");
                                            subcategoryid[i]=tempdata.getJSONObject(i).getString("id");
                                            subcategorydescription[i]=tempdata.getJSONObject(i).getString("description");
                                            subcategorypicture[i]=tempdata.getJSONObject(i).getString("picture");
                                        }

                                        gridView.setAdapter(new ImageAdapter(myactivity,getApplicationContext(), subcategoryname,subcategoryid,subcategorypicture));
                                        pDialog.dismiss();
                                    }else if(status.compareTo("err") == 0){
                                        Toast.makeText(getApplicationContext(), response.getString("message").split(";")[poschooselan], Toast.LENGTH_SHORT).show();
                                        pDialog.dismiss();
                                    }
                                    else{
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
        protected void onPostExecute(JSONObject json) {


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
            pDialog = MyCustomProgressDialog.ctor(sellSubcategory.this);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(String... args) {
            buysubsf = getSharedPreferences("subcategoriestore"+category1,MODE_PRIVATE);
            Boolean alreadypresent1 = buysubsf.getBoolean("alreadypresent",false);
            JSONObject jsondata;
            if(alreadypresent1){
                String strjson = buysubsf.getString("jsondata",null);
                if(strjson!=null){
                    try{
                        jsondata= new JSONObject(strjson);
                        JSONArray tempdata =  jsondata.getJSONArray("subcategories");
                        int len=tempdata.length();
                        subcategoryname =new String[len];
                        subcategoryid=new String[len];
                        subcategorypicture=new String[len];
                        subcategorydescription=new String[len];

                        for(int i=0;i<len;i++){
                            subcategoryname[i]=tempdata.getJSONObject(i).getString("name");
                            subcategoryid[i]=tempdata.getJSONObject(i).getString("id");
                            subcategorydescription[i]=tempdata.getJSONObject(i).getString("description");
                            subcategorypicture[i]=tempdata.getJSONObject(i).getString("picture");
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
                gridView.setAdapter(new ImageAdapter(myactivity,getApplicationContext(), subcategoryname,subcategoryid,subcategorypicture));
                pDialog.dismiss();
            }
            else{
                new NetCheck().execute();
                pDialog.dismiss();
            }
        }
    }
}
