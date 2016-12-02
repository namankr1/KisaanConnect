package iitp.naman.kisaanconnect;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

public class InterestedQuote extends AppCompatActivity {
    String senderPhone1;
    String category1;

    String categoryname1;
    String subcategoryname1;
    String subcategory1;
    String quoteid1 ;
    String nameofseller1 ;
    String price1 ;
    String bidvalue1;
    String quantity1 ;
    String description1 ;
    String quoterating1;
    String receiverphone1 ;
    Button btnInterested;
    EditText yourprice;
    EditText yourquantity;
    TextView currentprice;
    TextView availablequantity;
    TextView quoterating;
    TextView nameofseller;
    TextView phoneseller;
    TextView description;
    TextView category;
    TextView subcategory;



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
            quantity1=extras.getString("quantity");
            description1=extras.getString("description");
            quoterating1=extras.getString("quoterating");
            receiverphone1=extras.getString("receiverphone");
            subcategoryname1=extras.getString("subcategoryname");
            categoryname1=extras.getString("categoryname");
        }

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Quote Details");
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        btnInterested = (Button) findViewById(R.id.interested);
        yourprice = (EditText) findViewById(R.id.price);
        yourquantity = (EditText) findViewById(R.id.quantity);

        currentprice = (TextView) findViewById(R.id.currentbid);
        availablequantity = (TextView) findViewById(R.id.availablequantity);
        quoterating = (TextView) findViewById(R.id.quoterating);
        nameofseller = (TextView) findViewById(R.id.nameofseller);
        phoneseller = (TextView) findViewById(R.id.phoneofseller);
        description = (TextView) findViewById(R.id.description);
        category = (TextView) findViewById(R.id.description);
        subcategory= (TextView) findViewById(R.id.description);
        currentprice.setText(bidvalue1);
        availablequantity.setText((quantity1));
        quoterating.setText(quoterating1);
        nameofseller.setText(nameofseller1);
        phoneseller.setText(receiverphone1);
        description.setText(description1);
        category.setText(categoryname1);
        subcategory.setText(subcategoryname1);

        btnInterested.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                new InterestedNotification().execute();
                Intent upanel = new Intent(getApplicationContext(), Home.class);
                upanel.putExtra("phoneno", senderPhone1);
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
                Intent upanel = new Intent(getApplicationContext(), GetQuotesofSubcategory.class);
                upanel.putExtra("phoneno", senderPhone1);
                upanel.putExtra("category", category1);
                upanel.putExtra("subcategory", subcategory1);
                upanel.putExtra("subcategoryname",subcategoryname1);
                upanel.putExtra("categoryname",categoryname1);
                startActivity(upanel);
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class InterestedNotification extends AsyncTask<String,Void,JSONObject> {
        private ProgressDialog pDialog;
        String yourprice1,yourquantity1;
        JSONObject resultserver=null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(InterestedQuote.this);
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
                jsonIn.put("senderphone",senderPhone1);
                jsonIn.put("recieverphone",receiverphone1);
                jsonIn.put("quoteid",quoteid1);
                jsonIn.put("price",yourprice1);
                jsonIn.put("quantity",yourquantity1);
                RequestQueue que = Volley.newRequestQueue(getApplicationContext());
                String urlString = getResources().getString(R.string.network_notification)+"raiseinterest/";
                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, urlString, jsonIn,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    String status = response.getString("status");
                                    if (status.compareTo("ok") == 0) {
                                        Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_SHORT).show();
                                    }else if(status.compareTo("err") == 0){
                                        Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        Toast.makeText(getApplicationContext(), "Connection fail", Toast.LENGTH_SHORT).show();
                                    }
                                    pDialog.dismiss();
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
                return null;
            }
            return resultserver;
        }
        @Override
        protected void onPostExecute(JSONObject response) {

        }
    }
}
