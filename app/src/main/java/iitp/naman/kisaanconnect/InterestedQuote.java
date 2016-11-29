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


public class InterestedQuote extends Activity {
    String senderPhone1;
    String category1;
    String subcategory1;
    String quoteid1 ;
    String nameofseller1 ;
    String price1 ;
    String bidvalue1;
    String quantity1 ;
    String description1 ;
    String quoterating1;
    String receiverphone1 ;
    Button btnBack;
    Button btnInterested;
    EditText yourprice;
    EditText yourquantity;
    TextView currentprice;
    TextView availablequantity;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.interestedquote);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            senderPhone1 = extras.getString("senderphone");
            category1=extras.getString("category");
            subcategory1=extras.getString("subcategory");
            quoteid1 = extras.getString("quoteid");
            nameofseller1=extras.getString("nameofseller");
            price1=extras.getString("price");
            bidvalue1=extras.getString("bidvalue");
            quoteid1=extras.getString("quoteid");
            quantity1=extras.getString("quantity");
            description1=extras.getString("description");
            quoterating1=extras.getString("quoterating");
            receiverphone1=extras.getString("receiverphone");
        }
        Toast.makeText(getApplicationContext(), senderPhone1,
                Toast.LENGTH_LONG).show();


        btnBack = (Button) findViewById(R.id.back);
        btnInterested = (Button) findViewById(R.id.interested);
        yourprice = (EditText) findViewById(R.id.price);
        yourquantity = (EditText) findViewById(R.id.quantity);
        currentprice = (TextView) findViewById(R.id.currentbid);
        availablequantity = (TextView) findViewById(R.id.availablequantity);
        currentprice.setText(bidvalue1);
        availablequantity.setText((quantity1));


        Log.i("Response :","Status : ");

        btnBack.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                Intent upanel = new Intent(getApplicationContext(), GetQuotesofSubcategory.class);
                upanel.putExtra("phoneno", senderPhone1);
                upanel.putExtra("category", category1);
                upanel.putExtra("subcategory", subcategory1);

                startActivity(upanel);
            }
        });

        btnInterested.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                new InterestedNotification().execute();
                //send notification
                Intent upanel = new Intent(getApplicationContext(), Home.class);
                upanel.putExtra("phoneno", senderPhone1);

                startActivity(upanel);
                //gotohome;
            }
        });

    }

    private class InterestedNotification extends AsyncTask<String,Void,JSONObject> {

        /**
         * Defining Process dialog
         **/
        private ProgressDialog pDialog;
        String yourprice1,yourquantity1;



        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(InterestedQuote.this);
            pDialog.setTitle("Contacting Servers");
            pDialog.setMessage("Registering ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
            yourprice1 = yourprice.getText().toString();
            yourquantity1=yourquantity.getText().toString();
        }

        @Override
        protected JSONObject doInBackground(String... args) {

            JSONObject jsonIn = new JSONObject();
            try {
                jsonIn.put("senderid",senderPhone1);
                jsonIn.put("recieverid",receiverphone1);
                jsonIn.put("quoteid",quoteid1);
                jsonIn.put("price",yourprice1);
                jsonIn.put("quantity",yourquantity1);
                Log.i("Sending ",senderPhone1+" "+receiverphone1+" "+quoteid1+" "+yourprice1+" "+yourquantity1);


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
            String urlString = getResources().getString(R.string.network_notification)+"raiseinterest/";
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


                                    //insert category details here
                                    pDialog.dismiss();
                                    Toast.makeText(getApplicationContext(),
                                            response.getString("message") ,
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
            //que.add(jsonObjReq);

        }
    }

}
