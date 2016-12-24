package iitp.naman.kisaanconnect;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
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
    private String senderPhone1;

    private String categoryname1;
    private String subcategoryname1;
    private String quoteid1 ;
    private String nameofseller1 ;
    private String price1 ;
    private String bidvalue1;
    private String quantity1 ;
    private String description1 ;
    private String quoterating1;
    private String receiverphone1 ;
    private Button btnInterested;
    private EditText yourprice;
    private EditText yourquantity;
    private TextView currentprice;
    private TextView availablequantity;
    private TextView quoterating;
    private TextView nameofseller;
    private TextView phoneseller;
    private TextView description;
    private TextView category;
    private TextView subcategory;
    private int poschooselan;



    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.interestedquote);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            senderPhone1 = extras.getString("senderphone");
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
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        getSupportActionBar().setTitle(getResources().getString(R.string.javainterestedquote_1));
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        SharedPreferences sfchoosenlan = getSharedPreferences("languagechoosen",MODE_PRIVATE);
        poschooselan = sfchoosenlan.getInt("position",0);

        btnInterested = (Button) findViewById(R.id.interested);
        yourprice = (EditText) findViewById(R.id.price);
        yourquantity = (EditText) findViewById(R.id.quantity);

        currentprice = (TextView) findViewById(R.id.currentbid);
        availablequantity = (TextView) findViewById(R.id.availablequantity);
        quoterating = (TextView) findViewById(R.id.quoterating);
        nameofseller = (TextView) findViewById(R.id.nameofseller);
        phoneseller = (TextView) findViewById(R.id.phoneofseller);
        description = (TextView) findViewById(R.id.description);
        category = (TextView) findViewById(R.id.categoryname);
        subcategory= (TextView) findViewById(R.id.subcategoryname);
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
                String yourprice1 = yourprice.getText().toString();
                String yourquantity1=yourquantity.getText().toString();
                try{
                    float basequantity;
                    try{
                        basequantity = Float.parseFloat(quantity1);
                    }
                    catch (Exception e1){
                        basequantity = 0;
                    }
                    float yourquantity2 = Float.parseFloat(yourquantity1);
                    if(yourquantity2<=basequantity){
                        new InterestedNotification().execute();
                    }
                    else{
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.javainterestedquote_3), Toast.LENGTH_SHORT).show();
                    }

                }
                catch (Exception e){
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.javainterestedquote_4), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
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
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class InterestedNotification extends AsyncTask<String,Void,JSONObject> {
        private ProgressDialog pDialog;
        private String yourprice1,yourquantity1;
        private JSONObject resultserver=null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = MyCustomProgressDialog.ctor(InterestedQuote.this);
            pDialog.setCancelable(false);
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
                                        Toast.makeText(getApplicationContext(), response.getString("message").split(";")[poschooselan], Toast.LENGTH_SHORT).show();
                                        pDialog.dismiss();
                                        finish();

                                    }else if(status.compareTo("err") == 0){
                                        Toast.makeText(getApplicationContext(), response.getString("message").split(";")[poschooselan], Toast.LENGTH_SHORT).show();
                                        pDialog.dismiss();
                                    }
                                    else{
                                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.connectionfail), Toast.LENGTH_SHORT).show();
                                        pDialog.dismiss();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.connectionfail), Toast.LENGTH_SHORT).show();
                                    pDialog.dismiss();
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
        protected void onPostExecute(JSONObject response) {

        }
    }
}
