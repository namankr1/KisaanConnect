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

/**
 * Created by naman on 02-Dec-16.
 */

public class UpdateQuote extends AppCompatActivity {
    String quoteid1 ;
    String price1 ;
    String quantity1 ;
    String description1 ;
    String type1;
    String is_active1;
    String inputphone1;

    TextView desc;
    EditText price;
    EditText type;
    EditText quantity;
    EditText is_active;


    Button yes1;
    Button no1;

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
            type1=extras.getString("type");
            is_active1=extras.getString("is_active");

        }

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Delete Quote");
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        yes1 = (Button) findViewById(R.id.yes);
        no1 = (Button) findViewById(R.id.no);


        price = (EditText) findViewById(R.id.price);
        quantity = (EditText) findViewById(R.id.quantity);
        desc = (TextView) findViewById(R.id.description);
        type=(EditText) findViewById(R.id.type);
        is_active=(EditText) findViewById(R.id.is_active);

        price.setText(price1,TextView.BufferType.EDITABLE);
        quantity.setText(quantity1,TextView.BufferType.EDITABLE);
        type.setText(type1,TextView.BufferType.EDITABLE);
        is_active.setText(is_active1,TextView.BufferType.EDITABLE);
        desc.setText(description1);


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
                Intent upanel = new Intent(getApplicationContext(), Home.class);
                upanel.putExtra("phoneno", inputphone1);
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
                Intent upanel = new Intent(getApplicationContext(), Home.class);
                upanel.putExtra("phoneno", inputphone1);
                startActivity(upanel);
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class InterestedNotification extends AsyncTask<String,Void,JSONObject> {
        private ProgressDialog pDialog;
        JSONObject resultserver=null;
        String type2,quantity2,price2,isactive2;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(UpdateQuote.this);
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
            type2=type.getText().toString();
            quantity2=quantity.getText().toString();
            price2=price.getText().toString();
            isactive2=is_active.getText().toString();
        }

        @Override
        protected JSONObject doInBackground(String... args) {

            JSONObject jsonIn = new JSONObject();
            try {
                jsonIn.put("quoteid",quoteid1);
                jsonIn.put("type",type2);
                jsonIn.put("quantity",quantity2);
                jsonIn.put("price",price2);
                jsonIn.put("is_active",isactive2);
                RequestQueue que = Volley.newRequestQueue(getApplicationContext());
                String urlString = getResources().getString(R.string.network_notification)+"updatequote/";
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
                                    Intent upanel = new Intent(getApplicationContext(), Home.class);
                                    upanel.putExtra("phoneno", inputphone1);
                                    startActivity(upanel);
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

