package iitp.naman.kisaanconnect;



/**
 * Created by naman on 01-Dec-16.
 */

import android.os.Bundle;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.widget.Button;
import android.widget.TextView;
import android.app.ProgressDialog;
import android.os.AsyncTask;
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

public class Negotiate extends AppCompatActivity {

    private String notificationid;
    private String notificationsenderphone;
    private String notificationsendername;
    private String notificationsenderaddress;
    private String notificationquantity;
    private String notificationprice;
    private String notificationquoteid;
    private String notificationstatus;
    private String inputPhone1;

    private EditText yourprice;
    private EditText yourquantity;
    private TextView quotedprice;
    private TextView quotedquantity;
    private TextView sendername;
    private TextView senderphone;
    private TextView senderaddress;
    private Button btnnegotiate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.negotiate);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Negotiate with Seller");
        getSupportActionBar().setDisplayShowTitleEnabled(true);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            inputPhone1 = extras.getString("inputPhone1");
            notificationid = extras.getString("notificationid");
            notificationprice = extras.getString("notificationprice");
            notificationquantity = extras.getString("notificationquantity");
            notificationquoteid = extras.getString("notificationquoteid");
            notificationsenderaddress = extras.getString("notificationsenderaddress");
            notificationsendername = extras.getString("notificationsendername");
            notificationsenderphone = extras.getString("notificationsenderphone");
            notificationstatus = extras.getString("notificationstatus");
        }

        yourprice = (EditText) findViewById(R.id.yourprice);
        yourquantity=(EditText) findViewById(R.id.yourquantity);
        quotedprice=(TextView) findViewById(R.id.quotedprice);
        quotedquantity=(TextView) findViewById(R.id.quotedquantity);
        sendername=(TextView) findViewById(R.id.sendername);
        senderphone=(TextView) findViewById(R.id.senderphone);
        senderaddress=(TextView) findViewById(R.id.senderaddress);
        btnnegotiate=(Button) findViewById(R.id.btnnegotiate);
        quotedprice.setText(notificationprice);
        quotedquantity.setText(notificationquantity);
        senderaddress.setText(notificationsenderaddress);
        senderphone.setText(notificationsenderphone);
        sendername.setText(notificationsendername);

        btnnegotiate.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                new ProcessNotification().execute();
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
//        Intent upanel = new Intent(getApplicationContext(), UserNotif.class);
//        upanel.putExtra("phoneno", inputPhone1);
//        startActivity(upanel);
        finish();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
//                Intent upanel = new Intent(getApplicationContext(), UserNotif.class);
//                upanel.putExtra("phoneno", inputPhone1);
//                startActivity(upanel);
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class ProcessNotification extends AsyncTask<String, Void, JSONObject> {
        private ProgressDialog pDialog;
        private String price2;
        private String quantity2;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = MyCustomProgressDialog.ctor(Negotiate.this);
            pDialog.setCancelable(true);
            pDialog.show();
            price2=yourprice.getText().toString();
            quantity2=yourquantity.getText().toString();
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            JSONObject jsonIn = new JSONObject();

            try {
                jsonIn.put("senderphone", inputPhone1);
                jsonIn.put("recieverphone", notificationsenderphone);
                jsonIn.put("quoteid", notificationquoteid);
                jsonIn.put("price", price2);
                jsonIn.put("quantity", quantity2);
                RequestQueue que = Volley.newRequestQueue(getApplicationContext());
                String urlString = getResources().getString(R.string.network_notification) + "negotiate/";
                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, urlString, jsonIn,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    String status = response.getString("status");
                                    if (status.compareTo("ok") == 0) {
                                        Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_LONG).show();
//                                        Intent upanel = new Intent(getApplicationContext(), UserNotif.class);
//                                        upanel.putExtra("phoneno", inputPhone1);
//                                        startActivity(upanel);
                                        pDialog.dismiss();
                                        finish();
                                    } else if (status.compareTo("err") == 0) {
                                        Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_LONG).show();
                                        pDialog.dismiss();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Connection fail", Toast.LENGTH_SHORT).show();
                                        pDialog.dismiss();
                                    }
                                } catch (JSONException e) {
                                    Toast.makeText(getApplicationContext(), "Connection fail", Toast.LENGTH_SHORT).show();
                                    pDialog.dismiss();
                                    e.printStackTrace();
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

            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Connection fail", Toast.LENGTH_SHORT).show();
                pDialog.dismiss();
                return null;
            }
            return jsonIn;
        }

        @Override
        protected void onPostExecute(JSONObject json) {


        }
    }
}
