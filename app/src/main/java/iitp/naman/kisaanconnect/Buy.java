package iitp.naman.kisaanconnect;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;

import android.content.SharedPreferences;
import android.text.InputType;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ToggleButton;
import android.content.res.Resources;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

public class Buy extends Activity {

    GridView gridView;
    String inputPhone1;
    Button btnBack;
    Boolean categoriesget=false;//checks if categories are already get from server or not;

    String[] categoryname = new String[] {};
    String[] categorydescription = new String[] {};
    String[] categoryid = new String[] {};
    String[] categorypicture = new String[] {};

    String[] notificationid = new String[] {"1","2","3","4","5"};
    String[] notificationreceiver = new String[] {"naman","naamna","naman","naman","naman"};
    String[] notificationquote = new String[] {"naman","naamna","naman","naman","naman"};
    String[] notificationprice = new String[] {"naman","naamna","naman","naman","naman"};
    String[] notificationquantity = new String[] {"naman","naamna","naman","naman","naman"};



    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.buy);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            inputPhone1 = extras.getString("phoneno");
        }
        NetAsync(this.findViewById(android.R.id.content));
        //new ProcessNotification().execute();

        Toast.makeText(getApplicationContext(), inputPhone1,
                Toast.LENGTH_LONG).show();


        //btnAddcateogy = (Button) findViewById(R.id.addcategory);
        btnBack = (Button) findViewById(R.id.back);

        gridView = (GridView) findViewById(R.id.gridView1);
        gridView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                String categoryid = ((TextView) v.findViewById(R.id.grid_item_id)).getText()+"";
                Intent upanel = new Intent(getApplicationContext(), buySubcategory.class);
                upanel.putExtra("phoneno", inputPhone1);
                upanel.putExtra("category",categoryid);
                startActivity(upanel);

                /*Toast.makeText(
                        getApplicationContext(),
                        ((TextView) v.findViewById(R.id.grid_item_id))
                                .getText(), Toast.LENGTH_SHORT).show();
                */
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
                Intent upanel = new Intent(getApplicationContext(), Home.class);
                upanel.putExtra("phoneno", inputPhone1);

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
            nDialog = new ProgressDialog(Buy.this);
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
            pDialog = new ProgressDialog(Buy.this);
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
            String urlString = getResources().getString(R.string.network_url_category)+"getcategories/";
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, urlString, json,
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
                                    categoryname =new String[len];
                                    categoryid=new String[len];
                                    categorypicture=new String[len];
                                    categorydescription=new String[len];

                                    for(int i=0;i<len;i++){
                                        categoryname[i]=tempdata.getJSONObject(i).getString("name");
                                        categoryid[i]=tempdata.getJSONObject(i).getString("id");
                                        categorydescription[i]=tempdata.getJSONObject(i).getString("description");
                                        categorypicture[i]=getResources().getString(R.string.network_home)+tempdata.getJSONObject(i).getString("picture");
                                        Log.i("Response4 :","Status : "+categoryid[i]+" "+categoryname[i]+" "+categorydescription[i]+" "+categorypicture[i]);

                                    }

                                    categoriesget=true;
                                    gridView.setAdapter(new ImageAdapter(getApplicationContext(), categoryname,categorydescription,categoryid,categorypicture));
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
            pDialog = new ProgressDialog(Buy.this);
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
