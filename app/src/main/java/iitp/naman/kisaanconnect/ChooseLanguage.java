package iitp.naman.kisaanconnect;

/**
 * Created by naman on 16-Dec-16.
 */

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class ChooseLanguage extends AppCompatActivity {
    private String inputPhone1;
    private SharedPreferences sf;
    private SharedPreferences.Editor e;
    private Button btn1;

    private List<String> categories1;
    private Spinner spinner1;

    private String[] languagesavailable;
    private String[] languagesids;

    private String choosenlan="en";

    private int classtoload=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chooselanguage);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            inputPhone1 = extras.getString("phoneno");
        }
        if(inputPhone1!=null) {
            if (inputPhone1.equals("1")) {
                classtoload = 1;
            } else {
                classtoload = 2;
            }
        }
        languagesavailable = getResources().getStringArray(R.array.languagestotranslate);
        languagesids = getResources().getStringArray(R.array.codelanguagestotranslate);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.javachooselanguage_1));
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        btn1 = (Button)findViewById(R.id.btnLan);
        spinner1 = (Spinner) findViewById(R.id.spinnerLan);

        categories1 = new ArrayList<>();
        Collections.addAll(categories1, languagesavailable);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories1);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sf = getSharedPreferences("languagechoosen",MODE_PRIVATE);
        spinner1.setAdapter(dataAdapter);
        int pos = sf.getInt("position",0);
        spinner1.setSelection(pos);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                choosenlan = languagesids[position];
                e = sf.edit();
                e.putInt("position", position);
                e.commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btn1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                e = sf.edit();
                e.putString("lan", choosenlan);
                e.commit();
                Log.i("choosenlanguage : ","choosen language "+choosenlan);
                Locale locale = new Locale(choosenlan);
                Locale.setDefault(locale);
                Configuration config = new Configuration();
                config.locale = locale;
                getBaseContext().getResources().updateConfiguration(config,
                        getBaseContext().getResources().getDisplayMetrics());
                if(classtoload==2) {
                    Intent upanel = new Intent(getApplicationContext(), Home.class);
                    upanel.putExtra("phoneno", inputPhone1);
                    startActivity(upanel);
                    finish();
                }
                else{
                    Intent upanel = new Intent(getApplicationContext(), Login.class);
                    startActivity(upanel);
                    finish();
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        if(classtoload==2) {
            Intent upanel = new Intent(getApplicationContext(), Home.class);
            upanel.putExtra("phoneno", inputPhone1);
            startActivity(upanel);
            finish();
        }
        else{
            Intent upanel = new Intent(getApplicationContext(), Login.class);
            startActivity(upanel);
            finish();
        }
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
                if(classtoload==2) {
                    Intent upanel = new Intent(getApplicationContext(), Home.class);
                    upanel.putExtra("phoneno", inputPhone1);
                    startActivity(upanel);
                }
                else{
                    Intent upanel = new Intent(getApplicationContext(), Login.class);
                    startActivity(upanel);
                }
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
