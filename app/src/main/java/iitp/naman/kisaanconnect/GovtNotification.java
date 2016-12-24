package iitp.naman.kisaanconnect;

/**
 * Created by naman on 01-Oct-16.
 */

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
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

import static iitp.naman.kisaanconnect.R.id.url;

public class GovtNotification extends AppCompatActivity {
    private String inputPhone1;

    private String govtnotifid[] = new String[]{};
    private String govtnotiftitle[] = new String[]{};
    private String govtnotifbody[] = new String[]{};
    private String govtnotifurl[] = new String[]{};
    private GridView gridView;

    private SharedPreferences.Editor govte;
    private SharedPreferences govtsf;
    private int poschooselan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.govtnotification);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.govtnotif_1));
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        SharedPreferences sfchoosenlan = getSharedPreferences("languagechoosen",MODE_PRIVATE);
        poschooselan = sfchoosenlan.getInt("position",0);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            inputPhone1 = extras.getString("phoneno");
        }

        gridView = (GridView) findViewById(R.id.gridView1);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                try {
                    String url1 = ((TextView) v.findViewById(url)).getText() + "";
                    if (!url1.startsWith("http://") && !url1.startsWith("https://"))
                        url1 = "http://" + url1;
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url1));
                    startActivity(i);
                }
                catch (Exception e){
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.govtnotif_2), Toast.LENGTH_SHORT).show();
                }
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
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
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        new ProcessUpdateFromStored().execute();
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
            nDialog = MyCustomProgressDialog.ctor(GovtNotification.this);
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
                Toast.makeText(getApplicationContext(),getResources().getString(R.string.cantconnect), Toast.LENGTH_SHORT).show();
            }
            nDialog.dismiss();

        }
    }

    private class ProcessRegister extends AsyncTask<String,Void,Boolean> {
        private ProgressDialog pDialog;
        Boolean resultserver=false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = MyCustomProgressDialog.ctor(GovtNotification.this);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(String... args) {

            JSONObject jsonIn = new JSONObject();
            govtsf = getSharedPreferences("govtnotif",MODE_PRIVATE);
            govte = govtsf.edit();
            try {
                jsonIn.put("phone",inputPhone1);
                RequestQueue que = Volley.newRequestQueue(getApplicationContext());
                String urlString = getResources().getString(R.string.network_notification)+"getgovtnotifications/";
                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, urlString, jsonIn,
                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    String status = response.getString("status");
                                    if (status.compareTo("ok") == 0) {
                                        govte.putBoolean("alreadypresent",true);
                                        govte.putString("jsondata",response.toString());
                                        govte.commit();
                                        JSONArray tempdata =  response.getJSONArray("govtNotifications");
                                        int len=tempdata.length();
                                        govtnotifbody =new String[len];
                                        govtnotifid=new String[len];
                                        govtnotiftitle=new String[len];
                                        govtnotifurl=new String[len];

                                        for(int i=0;i<len;i++){
                                            govtnotifbody[i]=tempdata.getJSONObject(i).getString("body");
                                            govtnotifid[i]=tempdata.getJSONObject(i).getString("id");
                                            govtnotiftitle[i]=tempdata.getJSONObject(i).getString("title");
                                            govtnotifurl[i]=tempdata.getJSONObject(i).getString("url");

                                        }

                                        gridView.setAdapter( new Adaptercls(getApplicationContext(),govtnotifbody,govtnotifid,govtnotiftitle,govtnotifurl));
                                        resultserver=true;
                                        pDialog.dismiss();
                                    }else if(status.compareTo("err") == 0){
                                        Toast.makeText(getApplicationContext(), response.getString("message").split(";")[poschooselan], Toast.LENGTH_SHORT).show();
                                        pDialog.dismiss();
                                    }
                                    else{
                                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.connectionfail), Toast.LENGTH_SHORT).show();
                                        pDialog.dismiss();
                                    }
                                } catch (JSONException e) {
                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.connectionfail), Toast.LENGTH_SHORT).show();
                                    pDialog.dismiss();
                                    e.printStackTrace();
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
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.connectionfail), Toast.LENGTH_SHORT).show();
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

    private class ProcessUpdateFromStored extends AsyncTask<String,Void,Boolean> {
        private ProgressDialog pDialog;
        private Boolean resultserver=false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = MyCustomProgressDialog.ctor(GovtNotification.this);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(String... args) {
            govtsf = getSharedPreferences("govtnotif",MODE_PRIVATE);
            Boolean alreadypresent1 = govtsf.getBoolean("alreadypresent",false);
            JSONObject jsondata;
            if(alreadypresent1){
                String strjson = govtsf.getString("jsondata",null);
                if(strjson!=null){
                    try{
                        jsondata= new JSONObject(strjson);

                        JSONArray tempdata =  jsondata.getJSONArray("govtNotifications");
                        int len=tempdata.length();
                        govtnotifbody =new String[len];
                        govtnotifid=new String[len];
                        govtnotiftitle=new String[len];
                        govtnotifurl=new String[len];

                        for(int i=0;i<len;i++){
                            govtnotifbody[i]=tempdata.getJSONObject(i).getString("body");
                            govtnotifid[i]=tempdata.getJSONObject(i).getString("id");
                            govtnotiftitle[i]=tempdata.getJSONObject(i).getString("title");
                            govtnotifurl[i]=tempdata.getJSONObject(i).getString("url");

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
                gridView.setAdapter( new Adaptercls(getApplicationContext(),govtnotifbody,govtnotifid,govtnotiftitle,govtnotifurl));
                pDialog.dismiss();
            }
            else{
                new NetCheck().execute();
                pDialog.dismiss();
            }
        }
    }
}

