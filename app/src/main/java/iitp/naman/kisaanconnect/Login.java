package iitp.naman.kisaanconnect;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ToggleButton;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;


public class Login extends AppCompatActivity {
    Button btnLogin;
    Button btnRegister;
    Button btnReset;
    EditText inputPhone;
    EditText inputPassword;
    ToggleButton tb;
    CheckBox ch2;
    SharedPreferences.Editor e;
    SharedPreferences sf;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inputPhone = (EditText) findViewById(R.id.phone);
        inputPassword = (EditText) findViewById(R.id.password);
        btnRegister = (Button) findViewById(R.id.signup);
        btnLogin = (Button) findViewById(R.id.login);
        btnReset = (Button)findViewById(R.id.forgotpassword);
        tb = (ToggleButton) findViewById(R.id.checkBox);
        ch2 = (CheckBox)findViewById(R.id.checkBox2);
        sf = getSharedPreferences("yaad",MODE_PRIVATE);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("Login");
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        String ph = sf.getString("phonenum","");
        String ps = sf.getString("passwordnum","");
        inputPhone.setText(ph);
        inputPassword.setText(ps);

        tb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    inputPassword.setInputType(InputType.TYPE_CLASS_TEXT);
                }
                else {
                    inputPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(getApplicationContext(), PasswordReset.class);
                myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(myIntent);
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(getApplicationContext(), Register.class);
                startActivity(myIntent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (  ( !inputPhone.getText().toString().equals("")) && ( !inputPassword.getText().toString().equals("")) ) {
                    NetAsync(view);
                }
                else if ( ( !inputPhone.getText().toString().equals("")) ) {
                    Toast.makeText(getApplicationContext(), "Password field empty", Toast.LENGTH_SHORT).show();
                }
                else if ( ( !inputPassword.getText().toString().equals("")) ) {
                    Toast.makeText(getApplicationContext(), "Phone field empty", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Phone and Password field are empty", Toast.LENGTH_SHORT).show();
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
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private class NetCheck extends AsyncTask<String, Void, Boolean>
    {
        private ProgressDialog nDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            nDialog = new ProgressDialog(Login.this){
                @Override
                public void onBackPressed() {
                    nDialog.dismiss();
                }};
            nDialog.setCancelable(false);
            nDialog.show();
        }

        @Override
        protected Boolean doInBackground(String... args) {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()) {
                try {
                    URL url = new URL(getResources().getString(R.string.network_check));
                    HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                    urlc.setConnectTimeout(8000);
                    urlc.connect();
                    if (urlc.getResponseCode() == 200)
                    {
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
        protected void onPostExecute(Boolean th) {

            if(th == true) {
                nDialog.dismiss();
                new ProcessLogin().execute();
            }
            else {
                Toast.makeText(getApplicationContext(), "Connection fail", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class ProcessLogin extends AsyncTask<String,Void,JSONObject> {

        private ProgressDialog pDialog;
        String inputPhone1,inputPassword1;
        JSONObject resultserver=null;

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(Login.this);
            pDialog.setCancelable(false);
            pDialog.show();
            super.onPreExecute();
            inputPhone1 = inputPhone.getText().toString();
            inputPassword1 = inputPassword.getText().toString();
        }

        @Override
        protected JSONObject doInBackground(String... args) {

            JSONObject jsonIn = new JSONObject();
            try {
                jsonIn.put("phone", inputPhone1);
                jsonIn.put("password", inputPassword1);
            }
            catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Connection fail", Toast.LENGTH_SHORT).show();
                return null;
            }
            return jsonIn;
        }

        @Override
        protected void onPostExecute(JSONObject jsonIn) {

            RequestQueue que = Volley.newRequestQueue(getApplicationContext());
            String urlString = getResources().getString(R.string.network_url) + "signin/";
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, urlString, jsonIn,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                String status = response.getString("status");
                                if (status.compareTo("ok") == 0) {
                                    if (ch2.isChecked()) {
                                        e = sf.edit();
                                        e.putString("phonenum", inputPhone1);
                                        e.putString("passwordnum", inputPassword1);
                                        e.putBoolean("rm", true);
                                        e.commit();
                                    }
                                    Intent upanel = new Intent(getApplicationContext(), Home.class);
                                    upanel.putExtra("phoneno", inputPhone1);
                                    startActivity(upanel);
                                    finish();
                                } else if (status.compareTo("err") == 0) {
                                    Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_SHORT).show();
                                } else {
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
        }
    }

    public void NetAsync(View view){
        new NetCheck().execute();
    }
}