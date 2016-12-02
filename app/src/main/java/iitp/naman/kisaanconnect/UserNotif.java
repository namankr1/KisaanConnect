package iitp.naman.kisaanconnect;

/**
 * Created by naman on 30-Nov-16.
 */

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.widget.GridView;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UserNotif extends AppCompatActivity {
    private GridView gridView;
    String inputPhone1;

    String[] notificationid = new String[]{""};
    String[] notificationquoteid = new String[]{"No new Notifications"};
    String[] notificationprice = new String[]{""};
    String[] notificationquantity = new String[]{""};
    String[] notificationsenderphone = new String[]{""};
    String[] notificationsendername = new String[]{""};
    String[] notificationsenderaddress = new String[]{""};
    String[] notificationstatus = new String[]{""};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.usernotif);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Your Notifications");
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            inputPhone1 = extras.getString("phoneno");
        }
        new ProcessNotification().execute();
        gridView = (GridView) findViewById(R.id.gridView11);
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
                upanel.putExtra("phoneno", inputPhone1);
                startActivity(upanel);
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private class ProcessNotification extends AsyncTask<String, Void, JSONObject> {

        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(UserNotif.this);
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {

            JSONObject jsonIn = new JSONObject();
            try {

                jsonIn.put("phone", inputPhone1);
                RequestQueue que = Volley.newRequestQueue(getApplicationContext());
                String urlString = getResources().getString(R.string.network_notification) + "getnotifications/";
                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, urlString, jsonIn,
                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    String status = response.getString("status");
                                    if (status.compareTo("ok") == 0) {
                                        JSONArray tempdata = response.getJSONArray("notifications");
                                        int len = tempdata.length();
                                        if (len != 0) {
                                            notificationid = new String[len];
                                            notificationprice = new String[len];
                                            notificationquantity = new String[len];
                                            notificationquoteid = new String[len];
                                            notificationsenderphone = new String[len];
                                            notificationsendername = new String[len];
                                            notificationsenderaddress = new String[len];
                                            notificationstatus = new String[len];

                                            for (int i = 0; i < len; i++) {
                                                notificationid[i] = tempdata.getJSONObject(i).getString("id");
                                                JSONObject temp = tempdata.getJSONObject(i).getJSONObject("sender");
                                                notificationsenderphone[i] = temp.getString("phone");
                                                notificationsendername[i] = temp.getString("name");
                                                notificationsenderaddress[i] = temp.getString("address");
                                                notificationquantity[i] = tempdata.getJSONObject(i).getString("quantity");
                                                notificationprice[i] = tempdata.getJSONObject(i).getString("price");
                                                notificationquoteid[i] = tempdata.getJSONObject(i).getString("quoteid");
                                                notificationstatus[i] = tempdata.getJSONObject(i).getString("status");
                                                Log.i("Response4 :4", "Status : got notification");

                                            }

                                            gridView.setAdapter( new UserNotifAdapter(getApplicationContext(),inputPhone1,notificationid,notificationsenderphone,notificationsendername,notificationsenderaddress,notificationquantity,notificationprice,notificationquoteid,notificationstatus));

                                        }
                                        pDialog.dismiss();

                                    } else if (status.compareTo("err") == 0) {
                                        Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_LONG).show();
                                        pDialog.dismiss();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Connection fail", Toast.LENGTH_SHORT).show();
                                        pDialog.dismiss();
                                    }
                                } catch (JSONException e) {
                                    Toast.makeText(getApplicationContext(), "Connection fail", Toast.LENGTH_SHORT).show();
                                    e.printStackTrace();
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
            return jsonIn;

        }

        @Override
        protected void onPostExecute(JSONObject json) {
        }
    }
}