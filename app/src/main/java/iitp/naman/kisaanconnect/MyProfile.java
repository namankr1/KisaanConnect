package iitp.naman.kisaanconnect;

/**
 * Created by naman on 30-Nov-16.
 */

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.widget.TextView;

public class MyProfile extends AppCompatActivity{
    private String serverName;
    private String serverPhone;
    private String serverType;
    private String serverAddress;

    private TextView name1;
    private TextView phone1;
    private TextView address1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.myprofile);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            serverName = extras.getString("name");
            serverPhone=extras.getString("phoneno");
            serverAddress=extras.getString("address");
            serverType=extras.getString("type");
        }

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("My Profile");
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent upanel = new Intent(getApplicationContext(), UserNotif.class);
                upanel.putExtra("phoneno", serverPhone);
                startActivity(upanel);
                finish();
            }
        });

        name1 = (TextView) findViewById(R.id.name);
        phone1  = (TextView) findViewById(R.id.phone);
        address1 = (TextView) findViewById(R.id.address);
        name1.setText(serverName);
        address1.setText(serverAddress);
        phone1.setText(serverPhone);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.myprofilebase, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                Intent upanel = new Intent(getApplicationContext(), Home.class);
                upanel.putExtra("phoneno", serverPhone);
                startActivity(upanel);
                this.finish();
                return true;
            }
            case R.id.change_password: {
                Intent upanel = new Intent(getApplicationContext(), ChangePassword.class);
                upanel.putExtra("phoneno", serverPhone);
                upanel.putExtra("name",serverName);
                upanel.putExtra("address",serverAddress);
                upanel.putExtra("type",serverType);
                startActivity(upanel);
                this.finish();
                return true;
            }
            case R.id.update_profile: {
                Intent upanel = new Intent(getApplicationContext(), UpdateProfile.class);
                upanel.putExtra("phoneno", serverPhone);
                upanel.putExtra("name",serverName);
                upanel.putExtra("address",serverAddress);
                upanel.putExtra("type",serverType);
                startActivity(upanel);
                this.finish();
                return true;
            }
            case R.id.get_quotes: {
                Intent upanel = new Intent(getApplicationContext(), GetAllQuotesUser.class);
                upanel.putExtra("phoneno", serverPhone);
                upanel.putExtra("name",serverName);
                upanel.putExtra("address",serverAddress);
                upanel.putExtra("type",serverType);
                startActivity(upanel);
                this.finish();
                return true;
            }

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
