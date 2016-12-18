package iitp.naman.kisaanconnect;

/**
 * Created by naman on 30-Sep-16.
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
import java.net.HttpURLConnection;
import java.net.URL;

public class PasswordReset extends AppCompatActivity {

    private EditText inputPhone;
    private Button btnReset;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.passwordreset);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        getSupportActionBar().setTitle(" ");
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        inputPhone = (EditText) findViewById(R.id.phone);
        btnReset = (Button) findViewById(R.id.reset);

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
        Intent upanel = new Intent(getApplicationContext(), Login.class);
        startActivity(upanel);
        finish();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent upanel = new Intent(getApplicationContext(), Login.class);
                startActivity(upanel);
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
        protected void onPreExecute()
        {
            super.onPreExecute();
            nDialog = MyCustomProgressDialog.ctor(PasswordReset.this);
            nDialog.setCancelable(false);
            nDialog.show();
        }

        @Override
        protected Boolean doInBackground(String... args)
        {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected())
            {
                try
                {
                    URL url = new URL(getResources().getString(R.string.network_check));
                    HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                    urlc.setConnectTimeout(3000);
                    urlc.connect();
                    if (urlc.getResponseCode() == 200)
                    {
                        return true;
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            return false;

        }
        @Override
        protected void onPostExecute(Boolean th)
        {
            if(th)
            {
                new ProcessRegister().execute();
            }
            else
            {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.cantconnect), Toast.LENGTH_SHORT).show();
            }
            nDialog.dismiss();
        }
    }


    private class ProcessRegister extends AsyncTask<String,Void,JSONObject> {

        private ProgressDialog pDialog;
        private String inputPhone1;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            inputPhone1 = inputPhone.getText().toString();
            pDialog = MyCustomProgressDialog.ctor(PasswordReset.this);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {

            JSONObject jsonIn = new JSONObject();
            try {
                jsonIn.put("phone", inputPhone1);
                RequestQueue que = Volley.newRequestQueue(getApplicationContext());
                String urlString = getResources().getString(R.string.network_url)+"sendotp/";
                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, urlString, jsonIn,
                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    String status = response.getString("status");
                                    if (status.compareTo("ok") == 0) {
                                        Intent upanel = new Intent(getApplicationContext(), ForgotPassword.class);
                                        upanel.putExtra("phoneno", inputPhone1);
                                        startActivity(upanel);
                                        pDialog.dismiss();
                                        finish();
                                    }else if(status.compareTo("err") == 0){
                                        Toast.makeText(getApplicationContext(), response.getString("message") , Toast.LENGTH_LONG).show();
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
            }
            catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.connectionfail), Toast.LENGTH_SHORT).show();
                pDialog.dismiss();
                return null;
            }
            return jsonIn;

        }

        @Override
        protected void onPostExecute(JSONObject json) {

        }
    }
    public void NetAsync(View view){
        new NetCheck().execute();
    }
}