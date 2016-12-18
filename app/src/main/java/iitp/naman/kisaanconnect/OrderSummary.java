package iitp.naman.kisaanconnect;

/**
 * Created by naman on 15-Dec-16.
 */

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class OrderSummary extends AppCompatActivity {
    private String notificationsenderphone;
    private String notificationsendername;
    private String notificationsenderaddress;
    private String notificationquantity;
    private String notificationprice;

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
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        getSupportActionBar().setTitle(" ");
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            notificationprice = extras.getString("notificationprice");
            notificationquantity = extras.getString("notificationquantity");
            notificationsenderaddress = extras.getString("notificationsenderaddress");
            notificationsendername = extras.getString("notificationsendername");
            notificationsenderphone = extras.getString("notificationsenderphone");
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
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:0"+notificationsenderphone));
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
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
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
