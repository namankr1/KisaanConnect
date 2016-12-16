package iitp.naman.kisaanconnect;

/**
 * Created by naman on 15-Dec-16.
 */


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
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

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;


public class MarketInsight extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private FixedGridView gridViewsellList;
    private FixedGridView gridViewbuyList;
    private FixedGridView gridViewpriceList;
    private FixedGridviewImage gridViewrecommendationList;

    private String serverPhone;
    private Activity myactivity;

    private SharedPreferences.Editor marketinsighte;
    private SharedPreferences marketinsightsf;

    private String[] categoryname = new String[] {};
    private String[] categorydescription = new String[] {};
    private String[] categoryid = new String[] {};
    private String[] categorypicture = new String[] {};

    private String[][] subcategoryname = new String[][] {};
    private String[][] subcategorydescription = new String[][] {};
    private String[][] subcategoryid = new String[][] {};
    private String[][] subcategorypicture = new String[][] {};

    private String[][] whatOthersSellid = new String[][] {};
    private String[][] whatOthersBuyid = new String[][] {};
    private String[][] pricechangeid = new String[][] {};
    private String[][] recommendationid = new String[][] {};

    private String[][] whatOthersSellpercent = new String[][] {};
    private String[][] whatOthersBuypercent = new String[][] {};
    private String[][] pricechangepercent = new String[][] {};
    private String[][] recommendationpoint = new String[][] {};

    private String[][] whatOthersSellname = new String[][] {};
    private String[][] whatOthersBuyname = new String[][] {};
    private String[][] pricechangename = new String[][] {};
    private String[][] recommendationname = new String[][] {};

    private String[][] recommendationpict = new String[][] {};
    List<String> categories1;
    Spinner spinner;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.marketinsight);
        myactivity=this;

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            serverPhone=extras.getString("phoneno");
        }

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Market Insight");
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        //NetAsync(this.findViewById(android.R.id.content));

        gridViewbuyList = (FixedGridView) findViewById(R.id.buyList);
        gridViewpriceList = (FixedGridView) findViewById(R.id.priceList);
        gridViewrecommendationList = (FixedGridviewImage) findViewById(R.id.recommendationList);
        gridViewsellList = (FixedGridView) findViewById(R.id.sellList);



        // Spinner element
        spinner = (Spinner) findViewById(R.id.spinner1);
        new ProcessUpdateFromStored().execute();


    }
    @Override
    protected void onResume(){
        super.onResume();
    }

    @Override
    public void onBackPressed() {
//        Intent upanel = new Intent(getApplicationContext(), MyProfile.class);
//        upanel.putExtra("phoneno", serverPhone);
//        upanel.putExtra("name",serverName);
//        upanel.putExtra("address",serverAddress);
//        upanel.putExtra("type",serverType);
//        startActivity(upanel);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.refreshnotification, menu);
        return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();


        //gridViewsellList.setAdapter(new AdapterMarketInsight(getApplicationContext(),whatOthersSellid[0],whatOthersSellname[0],whatOthersSellpercent[0]));
        //gridViewpriceList.setAdapter(new AdapterMarketInsight(getApplicationContext(),pricechangeid[0],pricechangename[0],pricechangepercent[0]));
        //gridViewbuyList.setAdapter(new AdapterMarketInsight(getApplicationContext(),whatOthersBuyid[0],whatOthersBuyname[0],whatOthersBuypercent[0]));


        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
//                Intent upanel = new Intent(getApplicationContext(), MyProfile.class);
//                upanel.putExtra("phoneno", serverPhone);
//                upanel.putExtra("name",serverName);
//                upanel.putExtra("address",serverAddress);
//                upanel.putExtra("type",serverType);
//                startActivity(upanel);
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
            nDialog = MyCustomProgressDialog.ctor(MarketInsight.this);
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
            pDialog = MyCustomProgressDialog.ctor(MarketInsight.this);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(String... args) {
            JSONObject jsonIn = new JSONObject();
            marketinsightsf = getSharedPreferences("userorder"+serverPhone,MODE_PRIVATE);
            marketinsighte = marketinsightsf.edit();
            try {
                jsonIn.put("phone",serverPhone);
                RequestQueue que = Volley.newRequestQueue(getApplicationContext());
                String urlString = getResources().getString(R.string.network_notification)+"getmarketinsights/";
                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, urlString, jsonIn,
                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    String status = response.getString("status");
                                    if (status.compareTo("ok") == 0) {
                                        marketinsighte.putBoolean("alreadypresent",true);
                                        marketinsighte.putString("jsondata",response.toString());
                                        marketinsighte.commit();
                                        JSONArray tempdata =  response.getJSONArray("insights");
                                        int len = tempdata.length();

                                        categoryname = new String[len];
                                        categorydescription = new String[len];
                                        categoryid = new String[len];
                                        categorypicture = new String[len];

                                        subcategoryname = new String[len][];
                                        subcategorydescription = new String[len][];
                                        subcategoryid = new String[len][];
                                        subcategorypicture = new String[len][];

                                        whatOthersSellid = new String[len][];
                                        whatOthersBuyid = new String[len][];
                                        pricechangeid = new String[len][];
                                        recommendationid = new String[len][];

                                        whatOthersSellpercent = new String[len][];
                                        whatOthersBuypercent = new String[len][];
                                        pricechangepercent =new String[len][];
                                        recommendationpoint = new String[len][];

                                        whatOthersSellname = new String[len][];
                                        whatOthersBuyname = new String[len][];
                                        pricechangename =new String[len][];
                                        recommendationname = new String[len][];
                                        recommendationpict = new String[len][];

                                        for(int i=0;i<len;i++){
                                            categoryname[i]=tempdata.getJSONObject(i).getJSONObject("category").getString("name");
                                            categoryid[i]=tempdata.getJSONObject(i).getJSONObject("category").getString("id");
                                            categorydescription[i]=tempdata.getJSONObject(i).getJSONObject("category").getString("description");
                                            categorypicture[i]=tempdata.getJSONObject(i).getJSONObject("category").getString("picture");

                                            JSONArray tempdata1 =  tempdata.getJSONObject(i).getJSONArray("subcategories");
                                            JSONArray tempdata2 =  tempdata.getJSONObject(i).getJSONArray("whatOthersSell");
                                            JSONArray tempdata3 =  tempdata.getJSONObject(i).getJSONArray("whatOthersBuy");
                                            JSONArray tempdata4 =  tempdata.getJSONObject(i).getJSONArray("pricechange");
                                            JSONArray tempdata5 =  tempdata.getJSONObject(i).getJSONArray("recommendation");
                                            int len1 = tempdata1.length();

                                            subcategoryname[i] = new String[len1];
                                            subcategorydescription[i] = new String[len1];
                                            subcategoryid[i] = new String[len1];
                                            subcategorypicture[i] = new String[len1];

                                            whatOthersSellid[i] = new String[len1];
                                            whatOthersBuyid[i] = new String[len1];
                                            pricechangeid[i] = new String[len1];
                                            recommendationid[i] = new String[len1];

                                            whatOthersSellpercent[i] = new String[len1];
                                            whatOthersBuypercent[i] = new String[len1];
                                            pricechangepercent[i] = new String[len1];
                                            recommendationpoint[i] = new String[len1];

                                            whatOthersSellname[i] = new String[len1];
                                            whatOthersBuyname[i] = new String[len1];
                                            pricechangename[i] = new String[len1];
                                            recommendationname[i] = new String[len1];
                                            recommendationpict[i] = new String[len1];


                                            for(int j=0;j<len1;j++){
                                                subcategoryname[i][j]=tempdata1.getJSONObject(j).getString("name");
                                                subcategoryid[i][j]=tempdata1.getJSONObject(j).getString("id");
                                                subcategorydescription[i][j]=tempdata1.getJSONObject(j).getString("description");
                                                subcategorypicture[i][j]=tempdata1.getJSONObject(j).getString("picture");

                                                whatOthersSellid[i][j] = tempdata2.getJSONObject(j).getString("subcategoryid");
                                                whatOthersBuyid[i][j] = tempdata3.getJSONObject(j).getString("subcategoryid");
                                                pricechangeid[i][j] = tempdata4.getJSONObject(j).getString("subcategoryid");
                                                recommendationid[i][j] = tempdata5.getJSONObject(j).getString("subcategoryid");

                                                whatOthersSellname[i][j] = tempdata2.getJSONObject(j).getString("subcategoryname");
                                                whatOthersBuyname[i][j] = tempdata3.getJSONObject(j).getString("subcategoryname");
                                                pricechangename[i][j] = tempdata4.getJSONObject(j).getString("subcategoryname");
                                                recommendationname[i][j] = tempdata5.getJSONObject(j).getString("subcategoryname");

                                                whatOthersSellpercent[i][j] = tempdata2.getJSONObject(j).getString("percent");
                                                whatOthersBuypercent[i][j] = tempdata3.getJSONObject(j).getString("percent");
                                                pricechangepercent[i][j] = tempdata4.getJSONObject(j).getString("percent");
                                                recommendationpoint[i][j] = tempdata5.getJSONObject(j).getString("point");



                                            }

                                            for(int j=0;j<len1;j++){
                                                for(int k=0;k<len1;k++){
                                                    if(subcategoryid[i][k].equals(recommendationid[i][j])){
                                                        recommendationpict[i][j] = subcategorypicture[i][k];
                                                        break;
                                                    }
                                                }

                                            }



                                        }


                                        categories1 = new ArrayList<String>();
                                        for(int i=0;i<categoryname.length;i++){
                                            categories1.add(categoryname[i]);
                                        }
                                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(myactivity, android.R.layout.simple_spinner_item, categories1);
                                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        spinner.setAdapter(dataAdapter);
                                        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                String item = parent.getItemAtPosition(position).toString();

                                                gridViewsellList.setAdapter(new AdapterMarketInsight(getApplicationContext(),whatOthersSellid[position],whatOthersSellname[position],whatOthersSellpercent[position],1));
                                                gridViewpriceList.setAdapter(new AdapterMarketInsight(getApplicationContext(),pricechangeid[position],pricechangename[position],pricechangepercent[position],2));
                                                gridViewbuyList.setAdapter(new AdapterMarketInsight(getApplicationContext(),whatOthersBuyid[position],whatOthersBuyname[position],whatOthersBuypercent[position],3));
                                                gridViewrecommendationList.setAdapter(new ImageAdapter(getApplicationContext(),recommendationname[position],null,recommendationid[position],recommendationpict[position]));
                                                gridViewrecommendationList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                                                        String subcategoryid1 = ((TextView) v.findViewById(R.id.grid_item_id)).getText()+"";
                                                        String subcategoryname1 = ((TextView) v.findViewById(R.id.grid_item_label)).getText()+"";
                                                        Intent upanel = new Intent(getApplicationContext(), GetQuotesofSubcategory.class);
                                                        upanel.putExtra("phoneno", serverPhone);
                                                        upanel.putExtra("category",categoryid[position]);
                                                        upanel.putExtra("categoryname",categoryname[position]);
                                                        upanel.putExtra("subcategory",subcategoryid1);
                                                        upanel.putExtra("subcategoryname",subcategoryname1);
                                                        startActivity(upanel);
                                                        //finish();
                                                    }
                                                });
                                            }

                                            @Override
                                            public void onNothingSelected(AdapterView<?> parent) {

                                            }
                                        });
                                        pDialog.dismiss();
                                        resultserver=true;
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
                                    pDialog.dismiss();
                                }
                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Connection fail", Toast.LENGTH_SHORT).show();
                        pDialog.dismiss();
                    }
                });
                que.add(jsonObjReq);

            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Connection fail", Toast.LENGTH_SHORT).show();
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
            pDialog = MyCustomProgressDialog.ctor(MarketInsight.this);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            marketinsightsf = getSharedPreferences("userorder"+serverPhone,MODE_PRIVATE);
            Boolean alreadypresent1 = marketinsightsf.getBoolean("alreadypresent",false);
            JSONObject jsondata;
            if(alreadypresent1==true){
                String strjson = marketinsightsf.getString("jsondata",null);
                if(strjson!=null){
                    try{
                        jsondata= new JSONObject(strjson);


                        JSONArray tempdata =  jsondata.getJSONArray("insights");
                        int len = tempdata.length();

                        categoryname = new String[len];
                        categorydescription = new String[len];
                        categoryid = new String[len];
                        categorypicture = new String[len];

                        subcategoryname = new String[len][];
                        subcategorydescription = new String[len][];
                        subcategoryid = new String[len][];
                        subcategorypicture = new String[len][];

                        whatOthersSellid = new String[len][];
                        whatOthersBuyid = new String[len][];
                        pricechangeid = new String[len][];
                        recommendationid = new String[len][];

                        whatOthersSellpercent = new String[len][];
                        whatOthersBuypercent = new String[len][];
                        pricechangepercent =new String[len][];
                        recommendationpoint = new String[len][];

                        whatOthersSellname = new String[len][];
                        whatOthersBuyname = new String[len][];
                        pricechangename =new String[len][];
                        recommendationname = new String[len][];
                        recommendationpict = new String[len][];

                        for(int i=0;i<len;i++){
                            categoryname[i]=tempdata.getJSONObject(i).getJSONObject("category").getString("name");
                            categoryid[i]=tempdata.getJSONObject(i).getJSONObject("category").getString("id");
                            categorydescription[i]=tempdata.getJSONObject(i).getJSONObject("category").getString("description");
                            categorypicture[i]=tempdata.getJSONObject(i).getJSONObject("category").getString("picture");

                            JSONArray tempdata1 =  tempdata.getJSONObject(i).getJSONArray("subcategories");
                            JSONArray tempdata2 =  tempdata.getJSONObject(i).getJSONArray("whatOthersSell");
                            JSONArray tempdata3 =  tempdata.getJSONObject(i).getJSONArray("whatOthersBuy");
                            JSONArray tempdata4 =  tempdata.getJSONObject(i).getJSONArray("pricechange");
                            JSONArray tempdata5 =  tempdata.getJSONObject(i).getJSONArray("recommendation");
                            int len1 = tempdata1.length();

                            subcategoryname[i] = new String[len1];
                            subcategorydescription[i] = new String[len1];
                            subcategoryid[i] = new String[len1];
                            subcategorypicture[i] = new String[len1];

                            whatOthersSellid[i] = new String[len1];
                            whatOthersBuyid[i] = new String[len1];
                            pricechangeid[i] = new String[len1];
                            recommendationid[i] = new String[len1];

                            whatOthersSellpercent[i] = new String[len1];
                            whatOthersBuypercent[i] = new String[len1];
                            pricechangepercent[i] = new String[len1];
                            recommendationpoint[i] = new String[len1];

                            whatOthersSellname[i] = new String[len1];
                            whatOthersBuyname[i] = new String[len1];
                            pricechangename[i] = new String[len1];
                            recommendationname[i] = new String[len1];
                            recommendationpict[i] = new String[len1];

                            for(int j=0;j<len1;j++){
                                subcategoryname[i][j]=tempdata1.getJSONObject(j).getString("name");
                                subcategoryid[i][j]=tempdata1.getJSONObject(j).getString("id");
                                subcategorydescription[i][j]=tempdata1.getJSONObject(j).getString("description");
                                subcategorypicture[i][j]=tempdata1.getJSONObject(j).getString("picture");

                                whatOthersSellid[i][j] = tempdata2.getJSONObject(j).getString("subcategoryid");
                                whatOthersBuyid[i][j] = tempdata3.getJSONObject(j).getString("subcategoryid");
                                pricechangeid[i][j] = tempdata4.getJSONObject(j).getString("subcategoryid");
                                recommendationid[i][j] = tempdata5.getJSONObject(j).getString("subcategoryid");

                                whatOthersSellname[i][j] = tempdata2.getJSONObject(j).getString("subcategoryname");
                                whatOthersBuyname[i][j] = tempdata3.getJSONObject(j).getString("subcategoryname");
                                pricechangename[i][j] = tempdata4.getJSONObject(j).getString("subcategoryname");
                                recommendationname[i][j] = tempdata5.getJSONObject(j).getString("subcategoryname");

                                whatOthersSellpercent[i][j] = tempdata2.getJSONObject(j).getString("percent");
                                whatOthersBuypercent[i][j] = tempdata3.getJSONObject(j).getString("percent");
                                pricechangepercent[i][j] = tempdata4.getJSONObject(j).getString("percent");
                                recommendationpoint[i][j] = tempdata5.getJSONObject(j).getString("point");
                            }

                            for(int j=0;j<len1;j++){
                                for(int k=0;k<len1;k++){
                                    if(subcategoryid[i][k].equals(recommendationid[i][j])){
                                        recommendationpict[i][j] = subcategorypicture[i][k];
                                        break;
                                    }
                                }

                            }
                        }



                        resultserver="1";
                        //gridView.setAdapter(new ImageAdapter(, categoryname, categorydescription, categoryid, categorypicture));
                        //pDialog.dismiss();
                    }
                    catch(Exception e){

                    }

                }
            }

            return resultserver;
        }
        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            if(response.equals("1")) {
                categories1 = new ArrayList<String>();
                for(int i=0;i<categoryname.length;i++){
                    categories1.add(categoryname[i]);
                }
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(myactivity, android.R.layout.simple_spinner_item, categories1);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(dataAdapter);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String item = parent.getItemAtPosition(position).toString();

                        gridViewsellList.setAdapter(new AdapterMarketInsight(getApplicationContext(),whatOthersSellid[position],whatOthersSellname[position],whatOthersSellpercent[position],1));
                        gridViewpriceList.setAdapter(new AdapterMarketInsight(getApplicationContext(),pricechangeid[position],pricechangename[position],pricechangepercent[position],2));
                        gridViewbuyList.setAdapter(new AdapterMarketInsight(getApplicationContext(),whatOthersBuyid[position],whatOthersBuyname[position],whatOthersBuypercent[position],3));
                        gridViewrecommendationList.setAdapter(new ImageAdapter(getApplicationContext(),recommendationname[position],null,recommendationid[position],recommendationpict[position]));

                        gridViewrecommendationList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                                String subcategoryid1 = ((TextView) v.findViewById(R.id.grid_item_id)).getText()+"";
                                String subcategoryname1 = ((TextView) v.findViewById(R.id.grid_item_label)).getText()+"";
                                Intent upanel = new Intent(getApplicationContext(), GetQuotesofSubcategory.class);
                                upanel.putExtra("phoneno", serverPhone);
                                upanel.putExtra("category",categoryid[position]);
                                upanel.putExtra("categoryname",categoryname[position]);
                                upanel.putExtra("subcategory",subcategoryid1);
                                upanel.putExtra("subcategoryname",subcategoryname1);
                                startActivity(upanel);
                                //finish();
                            }
                        });
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                pDialog.dismiss();
            }
            else{
                new NetCheck().execute();
                pDialog.dismiss();
            }

        }
    }
}
