package iitp.naman.kisaanconnect;

/**
 * Created by naman on 29-Nov-16.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import static android.content.Context.MODE_PRIVATE;

public class AdapterUserQuotes extends BaseAdapter {
    private Activity myactivity;
    private Context context;
    private String[] quotebidvalue;
    private String[] quotedescription;
    private String[] quoteid;
    private String[] quoteprice;
    private String[] quotequantity;
    private String[] quoterating;
    private String[] userphone;
    private String[] useraddress;
    private String[] quotetype;
    private String serverPhone;
    private String serverName;
    private String serverType;
    private String serverAddress;
    private int poschooselan;

    public AdapterUserQuotes(Activity myactivity,Context context, String[] quotebidvalue,String[] quotedescription,String[] quoteid,String[] quoteprice,String[] quotequantity,String[] quoterating,String[] userphone,String[] useraddress,String[] quotetype,String serverPhone,String serverName,String serverType,String serverAddress) {
        this.context = context;
        this.myactivity=myactivity;
        this.quotebidvalue=quotebidvalue;
        this.quotedescription=quotedescription;
        this.quoteid=quoteid;
        this.quoteprice=quoteprice;
        this.quoterating=quoterating;
        this.quotequantity=quotequantity;
        this.userphone=userphone;
        this.useraddress=useraddress;
        this.serverPhone=serverPhone;
        this.quotetype=quotetype;
        this.serverAddress=serverAddress;
        this.serverName=serverName;
        this.serverType=serverType;
        SharedPreferences sfchoosenlan = context.getSharedPreferences("languagechoosen",MODE_PRIVATE);
        poschooselan = sfchoosenlan.getInt("position",0);

    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View gridView;
        if (convertView == null) {
            gridView = inflater.inflate(R.layout.singlerowuserquote, null);
        }
        else {
            gridView =  convertView;
        }

        ((TextView) gridView.findViewById(R.id.bidvalue)).setText(quotebidvalue[position]);
        try {
            ((TextView) gridView.findViewById(R.id.description)).setText(quotedescription[position].split(";")[poschooselan]);
        }
        catch (Exception e){
            ((TextView) gridView.findViewById(R.id.description)).setText(context.getResources().getString(R.string.deletequote_7));
        }
        ((TextView) gridView.findViewById(R.id.quoteid)).setText(quoteid[position]);
        ((TextView) gridView.findViewById(R.id.price)).setText(quoteprice[position]);
        ((TextView) gridView.findViewById(R.id.address)).setText(useraddress[position]);
        ((TextView) gridView.findViewById(R.id.quoterating)).setText(quoterating[position]);
        ((TextView) gridView.findViewById(R.id.quantity)).setText(quotequantity[position]);
        ((TextView) gridView.findViewById(R.id.type)).setText(quotetype[position].split(";")[poschooselan]);
        ((TextView) gridView.findViewById(R.id.phone)).setText(userphone[position]);

        Button btndel  = ((Button) gridView.findViewById(R.id.delete));
        Button btnupdate = ((Button) gridView.findViewById(R.id.update));

        btnupdate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent upanel = new Intent(context, UpdateQuote.class);
                upanel.putExtra("serverphone",serverPhone);
                upanel.putExtra("servername",serverName);
                upanel.putExtra("serveraddress",serverAddress);
                upanel.putExtra("servertype",serverType);
                upanel.putExtra("quoteid",quoteid[position]);
                try {
                    upanel.putExtra("desc",quotedescription[position].split(";")[poschooselan]);
                }
                catch (Exception e){
                    upanel.putExtra("desc",context.getResources().getString(R.string.deletequote_7));
                }
                upanel.putExtra("price",quotebidvalue[position]);
                upanel.putExtra("quantity",quotequantity[position]);
                try {
                    upanel.putExtra("type",quotetype[position].split(";")[poschooselan]);
                }
                catch (Exception e){
                    upanel.putExtra("type",context.getResources().getString(R.string.deletequote_7));
                }
                myactivity.startActivity(upanel);
            }
        });

        btndel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent upanel = new Intent(context, DeleteQuote.class);
                upanel.putExtra("serverphone",serverPhone);
                upanel.putExtra("servername",serverName);
                upanel.putExtra("serveraddress",serverAddress);
                upanel.putExtra("servertype",serverType);
                upanel.putExtra("quoteid",quoteid[position]);
                try {
                    upanel.putExtra("desc",quotedescription[position].split(";")[poschooselan]);
                }
                catch (Exception e){
                    upanel.putExtra("desc",context.getResources().getString(R.string.deletequote_7));
                }
                upanel.putExtra("price",quotebidvalue[position]);
                upanel.putExtra("quantity",quotequantity[position]);
                myactivity.startActivity(upanel);
            }
        });

        return gridView;
    }

    @Override
    public int getCount() {
        return quoteid.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
}
