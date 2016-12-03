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

public class GetQuotesofSubcategory extends AppCompatActivity {

    private GridView gridView;
    private String inputPhone1;
    private String category1;
    private String categoryname1;
    private String subcategoryname1;
    private String subcategory1;

    private String[] quoteid= new String[]{};
    private String[] quotetype= new String[]{};
    private String[] quotequantity= new String[]{};
    private String[] quoteprice= new String[]{};
    private String[] quotedescription= new String[]{};
    private String[] quotesubcategory= new String[]{};
    private String[] quoteisactive= new String[]{};
    private String[] quotebidvalue= new String[]{};
    private String[] quoterating= new String[]{};
    private String[] userrating= new String[] {};
    private String[] userdistance= new String[] {};
    private String[] userid=new String[]{};
    private String[] userphone= new String[]{};
    private String[] useraddress =  new String[]{};
    private String[] username = new String[]{};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.getquotesofsubcategory);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            inputPhone1 = extras.getString("phoneno");
            category1=extras.getString("category");
            subcategory1=extras.getString("subcategory");
            subcategoryname1=extras.getString("subcategoryname");
            categoryname1=extras.getString("categoryname");
        }

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(subcategoryname1);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        NetAsync(this.findViewById(android.R.id.content));

        gridView = (GridView) findViewById(R.id.gridView1);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                String quoteid1 = ((TextView) v.findViewById(R.id.quoteid)).getText()+"";
                String nameofseller1 =((TextView) v.findViewById(R.id.nameofseller)).getText()+"";
                String price1 =((TextView) v.findViewById(R.id.price)).getText()+"";
                String bidvalue1 =((TextView) v.findViewById(R.id.bidvalue)).getText()+"";
                String quantity1 =((TextView) v.findViewById(R.id.quantity)).getText()+"";
                String description1 =((TextView) v.findViewById(R.id.description)).getText()+"";
                String quoterating1 =((TextView) v.findViewById(R.id.quoterating)).getText()+"";
                String phone1 =((TextView) v.findViewById(R.id.phone)).getText()+"";

                Intent upanel = new Intent(getApplicationContext(), InterestedQuote.class);
                upanel.putExtra("senderphone", inputPhone1);
                upanel.putExtra("nameofseller",nameofseller1);
                upanel.putExtra("price", price1);
                upanel.putExtra("bidvalue",bidvalue1);
                upanel.putExtra("quantity", quantity1);
                upanel.putExtra("description",description1);
                upanel.putExtra("quoterating", quoterating1);
                upanel.putExtra("receiverphone",phone1);
                upanel.putExtra("quoteid",quoteid1);
                upanel.putExtra("category",category1);
                upanel.putExtra("subcategory",subcategory1);
                upanel.putExtra("subcategoryname",subcategoryname1);
                upanel.putExtra("categoryname",categoryname1);
                startActivity(upanel);
                finish();
            }
        });

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent upanel = new Intent(getApplicationContext(), UserNotif.class);
                upanel.putExtra("phoneno", inputPhone1);
                startActivity(upanel);
                finish();
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
                Intent upanel = new Intent(getApplicationContext(), buySubcategory.class);
                upanel.putExtra("phoneno", inputPhone1);
                upanel.putExtra("category", category1);
                upanel.putExtra("categoryname",categoryname1);
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
            nDialog = new ProgressDialog(GetQuotesofSubcategory.this);
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
                new ProcessRegister().execute();
            }
            else{
                Toast.makeText(getApplicationContext(), "Cannot Connect to Network", Toast.LENGTH_SHORT).show();
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
            pDialog = new ProgressDialog(GetQuotesofSubcategory.this);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(String... args) {
            JSONObject jsonIn = new JSONObject();
            try {
                jsonIn.put("phone",inputPhone1);
                jsonIn.put("subcategoryId",subcategory1);
                RequestQueue que = Volley.newRequestQueue(getApplicationContext());
                String urlString = getResources().getString(R.string.network_quotes)+"searchquotes/";
                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, urlString, jsonIn,
                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    String status = response.getString("status");
                                    if (status.compareTo("ok") == 0) {
                                        JSONArray tempdata =  response.getJSONArray("results");
                                        int len=tempdata.length();
                                        userdistance =new String[len];
                                        userrating=new String[len];
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
                                            userdistance[i]=tempdata.getJSONObject(i).getString("distance");
                                            userrating[i]=tempdata.getJSONObject(i).getString("rating");
                                            JSONObject temp = tempdata.getJSONObject(i).getJSONObject("quote");
                                            quoteid[i]=temp.getString("id");
                                            quotetype[i]=temp.getString("type");
                                            quotequantity[i]=temp.getString("quantity");
                                            quoteprice[i]=temp.getString("price");
                                            quotedescription[i]=temp.getString("description");
                                            JSONObject temp1 = temp.getJSONObject("profile");
                                            userid[i] = temp1.getString("userid");
                                            useraddress[i] = temp1.getString("address");
                                            userphone[i] = temp1.getString("phone");
                                            username[i] = temp1.getString("name");
                                            quotesubcategory[i]=temp.getString("subcategoryname");
                                            quoteisactive[i]=temp.getString("is_active");
                                            quotebidvalue[i]=temp.getString("bidvalue");
                                            quoterating[i]=temp.getString("rating");
                                        }

                                        resultserver=true;
                                        gridView.setAdapter(new AdapterQuotes(getApplicationContext(), quotebidvalue,quotedescription,quoteid,quoteprice,quotequantity,quoterating,userphone,username,useraddress,userrating));
                                    }else if(status.compareTo("err") == 0){
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
                return false;
            }
            return resultserver;

        }
        @Override
        protected void onPostExecute(Boolean json) {
            pDialog.dismiss();

        }
    }
    public void NetAsync(View view){
        new NetCheck().execute();
    }
}
