package iitp.naman.kisaanconnect;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class GetAllQuotesUser extends AppCompatActivity {

    GridView gridView;
    String serverName;
    String serverPhone;
    String serverType;
    String serverAddress;

    String[] quoteid= new String[]{};
    String[] quotetype= new String[]{};
    String[] quotequantity= new String[]{};
    String[] quoteprice= new String[]{};
    String[] quotedescription= new String[]{};
    String[] quotesubcategory= new String[]{};
    String[] quoteisactive= new String[]{};
    String[] quotebidvalue= new String[]{};
    String[] quoterating= new String[]{};
    String[] userid=new String[]{};
    String[] userphone= new String[]{};
    String[] useraddress =  new String[]{};
    String[] username = new String[]{};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.getquotesofsubcategory);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            serverName = extras.getString("name");
            serverPhone=extras.getString("phoneno");
            serverAddress=extras.getString("address");
            serverType=extras.getString("type");
        }

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Your all quotes!");
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        NetAsync(this.findViewById(android.R.id.content));

        gridView = (GridView) findViewById(R.id.gridView1);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.govtnotification, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent upanel = new Intent(getApplicationContext(), MyProfile.class);
                upanel.putExtra("phoneno", serverPhone);
                upanel.putExtra("name",serverName);
                upanel.putExtra("address",serverAddress);
                upanel.putExtra("type",serverType);
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
            nDialog = new ProgressDialog(GetAllQuotesUser.this);
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

    private class ProcessRegister extends AsyncTask<String,Void,Boolean> {
        private ProgressDialog pDialog;
        Boolean resultserver=false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(GetAllQuotesUser.this);
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(String... args) {
            JSONObject jsonIn = new JSONObject();
            try {
                jsonIn.put("phone",serverPhone);
                RequestQueue que = Volley.newRequestQueue(getApplicationContext());
                String urlString = getResources().getString(R.string.network_quotes)+"getallquotesbyuser/";
                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, urlString, jsonIn,
                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    String status = response.getString("status");
                                    if (status.compareTo("ok") == 0) {
                                        JSONArray temp =  response.getJSONArray("quote");
                                        int len=temp.length();
                                        quoteid=new String[len];
                                        quotetype=new String[len];
                                        quotequantity=new String[len];
                                        quoteprice=new String[len];
                                        quotedescription=new String[len];
                                        userid=new String[len];
                                        useraddress=new String[len];
                                        userphone=new String[len];
                                        username=new String[len];
                                        quotesubcategory=new String[len];
                                        quoteisactive=new String[len];
                                        quotebidvalue=new String[len];
                                        quoterating=new String[len];
                                        for(int i=0;i<len;i++){
                                            quoteid[i]=temp.getJSONObject(i).getString("id");
                                            quotetype[i]=temp.getJSONObject(i).getString("type");
                                            quotequantity[i]=temp.getJSONObject(i).getString("quantity");
                                            quoteprice[i]=temp.getJSONObject(i).getString("price");
                                            quotedescription[i]=temp.getJSONObject(i).getString("description");
                                            JSONObject temp1 = temp.getJSONObject(i).getJSONObject("profile");
                                            userid[i] = temp1.getString("userid");
                                            useraddress[i] = temp1.getString("address");
                                            userphone[i] = temp1.getString("phone");
                                            username[i] = temp1.getString("name");
                                            quotesubcategory[i]=temp.getJSONObject(i).getString("subcategoryname");
                                            quoteisactive[i]=temp.getJSONObject(i).getString("is_active");
                                            quotebidvalue[i]=temp.getJSONObject(i).getString("bidvalue");
                                            quoterating[i]=temp.getJSONObject(i).getString("rating");
                                        }

                                        resultserver=true;
                                        gridView.setAdapter(new AdapterUserQuotes(getApplicationContext(), quotebidvalue,quotedescription,quoteid,quoteprice,quotequantity,quoterating,userphone,username,useraddress,quotetype,serverPhone,serverName,serverType,serverAddress));
                                        pDialog.dismiss();
                                    }else if(status.compareTo("err") == 0){
                                        Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_SHORT).show();
                                        pDialog.dismiss();
                                    }
                                    else{
                                        Toast.makeText(getApplicationContext(), "Connection fail", Toast.LENGTH_SHORT).show();
                                        pDialog.dismiss();
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
}
