package iitp.naman.kisaanconnect;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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


public class GetQuotesofSubcategory extends Activity {

    GridView gridView;
    String inputPhone1;
    String category1;
    String subcategory1;
    //Button btnAddcateogy;
    Button btnBack;
    Boolean subcategoriesget=false;//checks if categories are already get from server or not;
    String[] quoteid= new String[]{"id","id"};
    String[] quotetype= new String[]{"id","id"};
    String[] quotequantity= new String[]{"id","id"};
    String[] quoteprice= new String[]{"id","id"};
    String[] quotedescription= new String[]{"id","id"};
    String[] quotesubcategory= new String[]{"id","id"};
    String[] quoteisactive= new String[]{"id","id"};
    String[] quotebidvalue= new String[]{"id","id"};
    String[] quoterating= new String[]{"id","id"};
    String[] userrating= new String[] {"rating","id"};
    String[] userdistance= new String[] {"distance","id"};
    String[] userid=new String[]{"userid","id"};
    String[] userphone= new String[]{"phone","id"};
    String[] useraddress =  new String[]{"address","id"};
    String[] username = new String[]{"name","id"};



    String[] notificationid = new String[] {"1","2","3","4","5"};
    String[] notificationreceiver = new String[] {"naman","naamna","naman","naman","naman"};
    String[] notificationquote = new String[] {"naman","naamna","naman","naman","naman"};
    String[] notificationprice = new String[] {"naman","naamna","naman","naman","naman"};
    String[] notificationquantity = new String[] {"naman","naamna","naman","naman","naman"};



    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.getquotesofsubcategory);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            inputPhone1 = extras.getString("phoneno");
            category1=extras.getString("category");
            subcategory1=extras.getString("subcategory");
        }
        //NetAsync(this.findViewById(android.R.id.content));
        //new ProcessNotification().execute();

        Toast.makeText(getApplicationContext(), inputPhone1,
                Toast.LENGTH_LONG).show();


        //btnAddcateogy = (Button) findViewById(R.id.addcategory);
        btnBack = (Button) findViewById(R.id.back);
        Log.i("Response :","Status : ");
        gridView = (GridView) findViewById(R.id.gridView1);
        Log.i("Response :","Status : ");
        gridView.setAdapter(new AdapterQuotes(getApplicationContext(), quotebidvalue,quotedescription,quoteid,quoteprice,quotequantity,quoterating,userphone,username,useraddress,userrating));
        Log.i("Response :","Status : ");
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
                startActivity(upanel);

            }
        });

        /*
        btnAddcateogy.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                Intent upanel = new Intent(getApplicationContext(), AddCategory.class);
                upanel.putExtra("phoneno", inputPhone1);

                startActivity(upanel);
            }
        });
        */
        btnBack.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                Intent upanel = new Intent(getApplicationContext(), buySubcategory.class);
                upanel.putExtra("phoneno", inputPhone1);
                upanel.putExtra("category", category1);

                startActivity(upanel);
            }
        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int len=notificationid.length;
                String notifall="";
                for(int i=0;i<len;i++){
                    notifall+=notificationid[i]+" "+notificationquote[i]+" "+notificationprice[i]+" "+notificationreceiver[i]+"\n";
                }

                Snackbar snackbar = Snackbar.make(view, notifall, Snackbar.LENGTH_LONG)
                        .setAction("Action", null);
                View snackbarView = snackbar.getView();
                TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setMaxLines(len);
                snackbar.show();

            }
        });
    }


    /**
     * Async Task to check whether internet connection is working
     **/

    private class NetCheck extends AsyncTask<String, Void, Boolean>
    {
        private ProgressDialog nDialog;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            nDialog = new ProgressDialog(GetQuotesofSubcategory.this);
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

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(GetQuotesofSubcategory.this);
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
                jsonIn.put("subcategoryId",subcategory1);

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
            String urlString = getResources().getString(R.string.network_quotes)+"searchquote/";
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

                                    /*
                                    Intent upanel = new Intent(getApplicationContext(), Buy.class);
                                    upanel.putExtra("phoneno", inputPhone1);

                                    pDialog.dismiss();

                                    startActivity(upanel);
                                    */

                                    Log.i("Response1 :","Status : "+status);
                                    JSONArray tempdata =  response.getJSONArray("results");
                                    int len=tempdata.length();
                                    userdistance =new String[len];
                                    userrating=new String[len];
                                    userdistance=new String[len];
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


                                        Log.i("Response4 :","Status : "+userdistance[i]+" "+userrating[i]);

                                    }

                                    subcategoriesget=true;
                                    gridView.setAdapter(new AdapterQuotes(getApplicationContext(), quotebidvalue,quotedescription,quoteid,quoteprice,quotequantity,quoterating,userphone,username,useraddress,userrating));
                                    //insert category details here
                                    pDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), "Welcome ",
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

    private class ProcessNotification extends AsyncTask<String,Void,JSONObject> {

        /**
         * Defining Process dialog
         **/
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(GetQuotesofSubcategory.this);
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
                jsonIn.put("userid",inputPhone1);


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
                                Log.i("Response :","Status : "+status);
                                if (status.compareTo("ok") == 0) {
                                    Log.i("Status Ok :","Loading User Space ");
                                    pDialog.setMessage("Loading User Space");
                                    pDialog.setTitle("Getting Data");

                                    /*
                                    Intent upanel = new Intent(getApplicationContext(), Buy.class);
                                    upanel.putExtra("phoneno", inputPhone1);

                                    pDialog.dismiss();

                                    startActivity(upanel);
                                    */

                                    Log.i("Response1 :","Status : "+status);
                                    JSONArray tempdata =  response.getJSONArray("categories");
                                    int len=tempdata.length();
                                    notificationid =new String[len];
                                    notificationprice=new String[len];
                                    notificationquantity=new String[len];
                                    notificationquote=new String[len];
                                    notificationreceiver=new String[len];

                                    for(int i=0;i<len;i++){
                                        notificationid[i]=tempdata.getJSONObject(i).getString("id");
                                        notificationreceiver[i]=tempdata.getJSONObject(i).getString("receiver");
                                        notificationquantity[i]=tempdata.getJSONObject(i).getString("quantity");
                                        notificationprice[i]=tempdata.getJSONObject(i).getString("price");
                                        notificationquote[i]=tempdata.getJSONObject(i).getString("quote");
                                        Log.i("Response4 :","Status : got notification");

                                    }

                                    //insert category details here
                                    pDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), "Welcome ",
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
}
