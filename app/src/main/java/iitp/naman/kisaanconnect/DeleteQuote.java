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

/**
 * Created by naman on 02-Dec-16.
 */

public class DeleteQuote extends AppCompatActivity {
    private String quoteid1 ;
    private String price1 ;
    private String quantity1 ;
    private String description1 ;
    private String inputphone1;

    private String serverName;
    private String serverType;
    private String serverAddress;

    private TextView currentprice;
    private TextView availablequantity;
    private TextView description;

    private Button yes1;
    private Button no1;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.deletequote);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            quoteid1 = extras.getString("quoteid");
            price1=extras.getString("price");
            quantity1=extras.getString("quantity");
            description1=extras.getString("desc");
            inputphone1=extras.getString("serverphone");
            serverAddress=extras.getString("serveraddress");
            serverName=extras.getString("servername");
            serverType=extras.getString("servertype");
        }

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Delete Quote");
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        yes1 = (Button) findViewById(R.id.yes);
        no1 = (Button) findViewById(R.id.no);


        currentprice = (TextView) findViewById(R.id.currentbid);
        availablequantity = (TextView) findViewById(R.id.availablequantity);
        description = (TextView) findViewById(R.id.description);

        availablequantity.setText((quantity1));
        description.setText(description1);
        currentprice.setText(price1);

        yes1.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                new InterestedNotification().execute();
            }
        });

        no1.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                Intent upanel = new Intent(getApplicationContext(), GetAllQuotesUser.class);
                upanel.putExtra("phoneno", inputphone1);
                upanel.putExtra("name",serverName);
                upanel.putExtra("address",serverAddress);
                upanel.putExtra("type",serverType);
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
                Intent upanel = new Intent(getApplicationContext(), GetAllQuotesUser.class);
                upanel.putExtra("phoneno", inputphone1);
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

    private class InterestedNotification extends AsyncTask<String,Void,JSONObject> {
        private ProgressDialog pDialog;
        private JSONObject resultserver=null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(DeleteQuote.this);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {

            JSONObject jsonIn = new JSONObject();
            try {
                jsonIn.put("quoteId",quoteid1);
                RequestQueue que = Volley.newRequestQueue(getApplicationContext());
                String urlString = getResources().getString(R.string.network_quotes)+"deletequote/";
                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, urlString, jsonIn,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    String status = response.getString("status");
                                    if (status.compareTo("ok") == 0) {
                                        Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_SHORT).show();
                                        Intent upanel = new Intent(getApplicationContext(), GetAllQuotesUser.class);
                                        upanel.putExtra("phoneno", inputphone1);
                                        upanel.putExtra("name",serverName);
                                        upanel.putExtra("address",serverAddress);
                                        upanel.putExtra("type",serverType);
                                        startActivity(upanel);
                                        finish();
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
                return null;
            }
            return resultserver;
        }
        @Override
        protected void onPostExecute(JSONObject response) {
            pDialog.dismiss();
        }
    }
}
