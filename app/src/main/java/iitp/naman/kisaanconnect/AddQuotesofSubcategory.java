package iitp.naman.kisaanconnect;

/**
 * Created by naman on 30-Nov-16.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
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

public class AddQuotesofSubcategory extends AppCompatActivity {

    GridView gridView;
    String inputPhone1;
    String category1;
    String categoryname1;
    String subcategoryname1;
    String subcategory1;
    Button btnBack;

    EditText type1;
    EditText quantity1;
    EditText price1;
    EditText description1;
    Button add;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.addquotesofsubcategory);
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

        type1 = (EditText) findViewById(R.id.type);
        price1= (EditText) findViewById(R.id.price);
        description1 = (EditText) findViewById(R.id.description);
        quantity1 = (EditText) findViewById(R.id.quantity);
        add = (Button) findViewById(R.id.add);



        Toast.makeText(getApplicationContext(), inputPhone1,
                Toast.LENGTH_LONG).show();


        //btnAddcateogy = (Button) findViewById(R.id.addcategory);

        Log.i("Response :","Status : ");


        add.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                NetAsync(view);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.govtnotification, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent upanel = new Intent(getApplicationContext(), sellSubcategory.class);
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

    /**
     * Async Task to check whether internet connection is working
     **/

    private class NetCheck extends AsyncTask<String, Void, Boolean>
    {
        private ProgressDialog nDialog;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            nDialog = new ProgressDialog(AddQuotesofSubcategory.this);
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
        String type2;
        String quantity2;
        String price2;
        String desc2;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AddQuotesofSubcategory.this);
            pDialog.setTitle("Contacting Servers");
            pDialog.setMessage("Registering ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
            type2=type1.getText().toString();
            desc2=description1.getText().toString();
            quantity2=quantity1.getText().toString();
            price2=price1.getText().toString();
        }

        @Override
        protected JSONObject doInBackground(String... args) {


            JSONObject jsonIn = new JSONObject();
            try {
                jsonIn.put("phone",inputPhone1);
                jsonIn.put("subcategoryId",subcategory1);
                jsonIn.put("type",type2);
                jsonIn.put("quantity",quantity2);
                jsonIn.put("price",price2);
                jsonIn.put("description",desc2);
                Log.i("phone "+inputPhone1," "+subcategory1);

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
            String urlString = getResources().getString(R.string.network_quotes)+"addquote/";
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


                                    pDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), response.getString("message") ,
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
}
