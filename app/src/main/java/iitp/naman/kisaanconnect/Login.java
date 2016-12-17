package iitp.naman.kisaanconnect;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
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
import java.util.Locale;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;


public class Login extends AppCompatActivity {
    private Button btnLogin;
    private Button btnRegister;
    private Button btnReset;
    private EditText inputPhone;
    private EditText inputPassword;
    private ToggleButton tb;
    private CheckBox ch2;
    SharedPreferences.Editor e;
    SharedPreferences sf;
    SharedPreferences sf1;
    SharedPreferences.Editor e1;
    private int restrictlogin=0;//to ensure only one click
    private ProgressDialog iDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i("44","called");
        restrictlogin=0;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle(" ");
        getSupportActionBar().setDisplayShowTitleEnabled(true);


        sf = getSharedPreferences("yaad",MODE_PRIVATE);
        Boolean cbf = sf.getBoolean("rm",false);
        // rm.setChecked(sf.getBoolean("rm",false));
        String ph = sf.getString("phonenum","");
        String choosenlan="en";

        try{
            sf1 = getSharedPreferences("languagechoosen",MODE_PRIVATE);
            Log.i("login : ","shared prefernce for language");

            choosenlan = sf1.getString("lan", "en");
            Log.i("login : ","choosen language "+choosenlan);
        }
        catch(Exception e){
            choosenlan = "en";
        }

        Log.i("login : ","choosen language "+choosenlan);
        Locale locale = new Locale(choosenlan);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());

        if(cbf == true)
        {
            Intent upanel = new Intent(getApplicationContext(), Home.class);
            upanel.putExtra("phoneno", ph);
            Log.i("Start","activity");
            startActivity(upanel);
            finish();
        }else {
            inputPhone = (EditText) findViewById(R.id.phone);
            inputPassword = (EditText) findViewById(R.id.password);
            btnRegister = (Button) findViewById(R.id.signup);
            btnLogin = (Button) findViewById(R.id.login);
            btnReset = (Button)findViewById(R.id.forgotpassword);
            tb = (ToggleButton) findViewById(R.id.checkBox);
            ch2 = (CheckBox)findViewById(R.id.checkBox2);

            String ps = sf.getString("passwordnum","");
            inputPhone.setText(ph);
            inputPassword.setText(ps);

            tb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        inputPassword.setInputType(InputType.TYPE_CLASS_TEXT);
                    } else {
                        inputPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    }
                }
            });

            btnReset.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    Intent myIntent = new Intent(getApplicationContext(), PasswordReset.class);
                    startActivity(myIntent);
                    finish();
                }
            });

            btnRegister.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    Intent myIntent = new Intent(getApplicationContext(), Register.class);
                    startActivity(myIntent);
                    finish();
                }
            });

            btnLogin.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    if ((!inputPhone.getText().toString().equals("")) && (!inputPassword.getText().toString().equals(""))) {
                        if (restrictlogin == 0) {
                            //restrictlogin=1;
                            iDialog = MyCustomProgressDialog.ctor(Login.this);
                            iDialog.show();
                            NetAsync(view);
                        }
                    } else if ((!inputPhone.getText().toString().equals(""))) {
                        Toast.makeText(getApplicationContext(), "Password field cant be empty", Toast.LENGTH_SHORT).show();
                    } else if ((!inputPassword.getText().toString().equals(""))) {
                        Toast.makeText(getApplicationContext(), "Phone field cant be empty", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Phone and Password field cant be empty", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.loginbase, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuHelp:
                String url1 = "https://kisaanconnect.herokuapp.com";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url1));
                startActivity(i);
                return true;

            case R.id.menuLan:
                Intent upanel = new Intent(getApplicationContext(), ChooseLanguage.class);
                upanel.putExtra("phoneno", "1");
                startActivity(upanel);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class NetCheck extends AsyncTask<String, Void, Boolean>
    {
        private ProgressDialog nDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //nDialog =MyCustomProgressDialog.ctor(Login.this);
            //nDialog.setCancelable(false);
            //nDialog.show();
        }

        @Override
        protected Boolean doInBackground(String... args) {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()) {
                try {
                    URL url = new URL(getResources().getString(R.string.network_check));
                    HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                    urlc.setConnectTimeout(3000);
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
                new ProcessLogin().execute();
            }
            else {
                iDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Connection fail", Toast.LENGTH_SHORT).show();
            }
            //nDialog.dismiss();

        }
    }

    private class ProcessLogin extends AsyncTask<String,Void,JSONObject> {

        private ProgressDialog pDialog;
        private String inputPhone1,inputPassword1;
        private JSONObject resultserver=null;


        @Override
        protected void onPreExecute() {
            //pDialog = MyCustomProgressDialog.ctor(Login.this);
            //pDialog.setCancelable(false);
            //pDialog.show();
            super.onPreExecute();
            inputPhone1 = inputPhone.getText().toString();
            inputPassword1 = inputPassword.getText().toString();
        }


        @Override
        protected JSONObject doInBackground(String... args) {

            final JSONObject jsonIn = new JSONObject();
            try {
                jsonIn.put("phone", inputPhone1);
                jsonIn.put("password", inputPassword1);
                RequestQueue que = Volley.newRequestQueue(getApplicationContext());
                String urlString = getResources().getString(R.string.network_url) + "signin/";
                final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, urlString, jsonIn,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    String status = response.getString("status");
                                    if (status.compareTo("ok") == 0) {
                                        e = sf.edit();

                                        String ph1 = sf.getString("phonenum", null);
                                        if(!ph1.equals(inputPhone1)){
                                            e.clear();
                                            e.commit();
                                        }
                                        e.putBoolean("rm", true);
                                        e.putString("phonenum", inputPhone1);
                                        if (ch2.isChecked()) {
                                            e.putString("passwordnum", inputPassword1);
                                        }
                                        e.commit();

                                        Intent upanel = new Intent(getApplicationContext(), Home.class);
                                        upanel.putExtra("phoneno", inputPhone1);
                                        Log.i("Start","activity");
                                        resultserver=response;
                                        startActivity(upanel);
                                        iDialog.dismiss();
                                        Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_SHORT).show();
                                        finish();
                                    } else if (status.compareTo("err") == 0) {
                                        String resp = response.getString("message");
                                        if(resp.equals("User account is disabled")){
                                            resp="Please verify your account first.";
                                            Intent upanel = new Intent(getApplicationContext(), Otp.class);
                                            upanel.putExtra("phoneno", inputPhone1);
                                            startActivity(upanel);
                                            iDialog.dismiss();
                                            Toast.makeText(getApplicationContext(),resp , Toast.LENGTH_SHORT).show();
                                            finish();
                                        }
                                        else {
                                            iDialog.dismiss();
                                            Toast.makeText(getApplicationContext(), resp, Toast.LENGTH_SHORT).show();
                                        }

                                    } else {

                                        iDialog.dismiss();
                                        Toast.makeText(getApplicationContext(), "Connection fail", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {

                                    iDialog.dismiss();
                                    e.printStackTrace();
                                    Toast.makeText(getApplicationContext(), "Connection fail", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        iDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Connection fail", Toast.LENGTH_SHORT).show();
                    }
                });
                que.add(jsonObjReq);
            }
            catch (JSONException e) {
                e.printStackTrace();
                iDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Connection fail", Toast.LENGTH_SHORT).show();
                return null;
            }
            return resultserver;
        }

        @Override
        protected void onPostExecute(JSONObject jsonIn) {
        }
    }

    public void NetAsync(View view){
        new NetCheck().execute();
    }
}