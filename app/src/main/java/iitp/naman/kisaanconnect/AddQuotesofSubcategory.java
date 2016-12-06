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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class AddQuotesofSubcategory extends AppCompatActivity {
    private String inputPhone1;
    private String category1;
    private String categoryname1;
    private String subcategoryname1;
    private String subcategory1;
    private EditText type1;
    private EditText quantity1;
    private EditText price1;
    private EditText description1;
    private Button add;

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
        getMenuInflater().inflate(R.menu.govtnotification, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
//        Intent upanel = new Intent(getApplicationContext(), sellSubcategory.class);
//        upanel.putExtra("phoneno", inputPhone1);
//        upanel.putExtra("category", category1);
//        upanel.putExtra("categoryname",categoryname1);
//        startActivity(upanel);
        finish();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
//                Intent upanel = new Intent(getApplicationContext(), sellSubcategory.class);
//                upanel.putExtra("phoneno", inputPhone1);
//                upanel.putExtra("category", category1);
//                upanel.putExtra("categoryname",categoryname1);
//                startActivity(upanel);
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
            nDialog = MyCustomProgressDialog.ctor(AddQuotesofSubcategory.this);
            nDialog.setCancelable(false);
            nDialog.show();
            super.onPreExecute();
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
                }
                catch (MalformedURLException e1) {
                    e1.printStackTrace();
                }
                catch (IOException e) {
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

    private class ProcessRegister extends AsyncTask<String,Void,JSONObject> {
        private ProgressDialog pDialog;
        private String type2;
        private String quantity2;
        private String price2;
        private String desc2;
        private JSONObject resultserver=null;

        @Override
        protected void onPreExecute() {
            pDialog = MyCustomProgressDialog.ctor(AddQuotesofSubcategory.this);
            pDialog.setCancelable(false);
            pDialog.show();
            super.onPreExecute();
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
                RequestQueue que = Volley.newRequestQueue(getApplicationContext());
                String urlString = getResources().getString(R.string.network_quotes)+"addquote/";
                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, urlString, jsonIn,
                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    String status = response.getString("status");
                                    if (status.compareTo("ok") == 0) {
                                        Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_SHORT).show();
//                                        Intent upanel = new Intent(getApplicationContext(), Home.class);
//                                        upanel.putExtra("phoneno", inputPhone1);
//                                        startActivity(upanel);
                                        pDialog.dismiss();
                                       finish();
                                    }
                                    else if (status.compareTo("err") == 0) {
                                        Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_SHORT).show();
                                        pDialog.dismiss();
                                    }
                                    else {
                                        Toast.makeText(getApplicationContext(), "Connection fail", Toast.LENGTH_SHORT).show();
                                        pDialog.dismiss();
                                    }
                                }
                                catch (JSONException e) {
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
            }
            catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Connection fail", Toast.LENGTH_SHORT).show();
                pDialog.dismiss();
                return null;
            }
            return resultserver;
        }

        @Override
        protected void onPostExecute(JSONObject response) {

        }
    }

    public void NetAsync(View view){
        new NetCheck().execute();
    }
}
