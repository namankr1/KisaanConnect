package iitp.naman.kisaanconnect;

/**
 * Created by naman on 15-Dec-16.
 */

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.app.ProgressDialog;
import android.os.AsyncTask;
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
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class OrderSummary extends AppCompatActivity {
    private String notificationid;
    private String notificationsenderphone;
    private String notificationsendername;
    private String notificationsenderaddress;
    private String notificationquantity;
    private String notificationprice;
    private String notificationquoteid;
    private String notificationstatus;
    private String inputPhone1;

    private int yourstatus=0;

    private TextView quotedprice;
    private TextView quotedquantity;
    private TextView sendername;
    private TextView senderphone;
    private TextView senderaddress;
    private Button btnaccept;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ordersummary);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(" ");
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

        quotedprice=(TextView) findViewById(R.id.price);
        quotedquantity=(TextView) findViewById(R.id.quantity);

        sendername=(TextView) findViewById(R.id.sendername);
        senderphone=(TextView) findViewById(R.id.senderphone);
        senderaddress=(TextView) findViewById(R.id.senderaddress);

        quotedprice.setText(notificationprice);
        quotedquantity.setText(notificationquantity);
        sendername.setText(notificationsendername);
        senderaddress.setText(notificationsenderaddress);
        senderphone.setText(notificationsenderphone);
        btnaccept=(Button) findViewById(R.id.accept);

        btnaccept.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                yourstatus=1;
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:0"+notificationsenderphone));
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
//        Intent upanel = new Intent(getApplicationContext(), UserNotif.class);
//        upanel.putExtra("phoneno", inputPhone1);
//        startActivity(upanel);
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
//                Intent upanel = new Intent(getApplicationContext(), UserNotif.class);
//                upanel.putExtra("phoneno", inputPhone1);
//                startActivity(upanel);
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
