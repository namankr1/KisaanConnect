package iitp.naman.kisaanconnect;

/**
 * Created by naman on 30-Nov-16.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
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
import java.net.HttpURLConnection;
import java.net.URL;

public class AddQuotesofSubcategory extends AppCompatActivity {
    private String inputPhone1;
    private String subcategoryname1;
    private String subcategory1;
    private EditText type1;
    private EditText quantity1;
    private EditText price1;
    private EditText description1;
    private Button add;
    private int poschooselan;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addquotesofsubcategory);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            inputPhone1 = extras.getString("phoneno");
            subcategory1=extras.getString("subcategory");
            subcategoryname1=extras.getString("subcategoryname");

        }

        SharedPreferences sfchoosenlan = getSharedPreferences("languagechoosen",MODE_PRIVATE);
        poschooselan = sfchoosenlan.getInt("position",0);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
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
                if(type1.getText().toString().equals("") || description1.getText().toString().equals("") ||quantity1.getText().toString().equals("") || price1.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.javaregistert_3), Toast.LENGTH_SHORT).show();
                }
                else {
                    NetAsync(view);
                }
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
        finish();
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
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean th){
            if(th){
                new ProcessRegister().execute();
            }
            else{
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.cantconnect), Toast.LENGTH_SHORT).show();
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
                                        Toast.makeText(getApplicationContext(), response.getString("message").split(";")[poschooselan], Toast.LENGTH_SHORT).show();
                                        pDialog.dismiss();
                                       finish();
                                    }
                                    else if (status.compareTo("err") == 0) {
                                        Toast.makeText(getApplicationContext(), response.getString("message").split(";")[poschooselan], Toast.LENGTH_SHORT).show();
                                        pDialog.dismiss();
                                    }
                                    else {
                                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.connectionfail), Toast.LENGTH_SHORT).show();
                                        pDialog.dismiss();
                                    }
                                }
                                catch (JSONException e) {
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
            }
            catch (JSONException e) {
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

    public void NetAsync(View view){
        new NetCheck().execute();
    }
}
