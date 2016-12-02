package iitp.naman.kisaanconnect;

/**
 * Created by naman on 29-Nov-16.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class AdapterUserQuotes extends BaseAdapter {
    private Context context;
    private String[] quotebidvalue;
    private String[] quotedescription;
    private String[] quoteid;
    private String[] quoteprice;
    private String[] quotequantity;
    private String[] quoterating;
    private String[] userphone;
    private String[] username;
    private String[] useraddress;
    private String[] quotetype;
    private String serverPhone;
    private String serverName;
    private String serverType;
    private String serverAddress;

    public AdapterUserQuotes(Context context, String[] quotebidvalue,String[] quotedescription,String[] quoteid,String[] quoteprice,String[] quotequantity,String[] quoterating,String[] userphone,String[] username,String[] useraddress,String[] quotetype,String serverPhone,String serverName,String serverType,String serverAddress) {
        this.context = context;
        this.quotebidvalue=quotebidvalue;
        this.quotedescription=quotedescription;
        this.quoteid=quoteid;
        this.quoteprice=quoteprice;
        this.quoterating=quoterating;
        this.quotequantity=quotequantity;
        this.userphone=userphone;
        this.username=username;
        this.useraddress=useraddress;
        this.serverPhone=serverPhone;
        this.quotetype=quotetype;
        this.serverAddress=serverAddress;
        this.serverName=serverName;
        this.serverType=serverType;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View gridView;
        if (convertView == null) {
            gridView = new View(context);
            gridView = inflater.inflate(R.layout.singlerowuserquote, null);
            TextView textView1 = (TextView) gridView.findViewById(R.id.bidvalue);
            textView1.setText(quotebidvalue[position]);
            TextView textView2 = (TextView) gridView.findViewById(R.id.description);
            textView2.setText(quotedescription[position]);
            TextView textView3 = (TextView) gridView.findViewById(R.id.quoteid);
            textView3.setText(quoteid[position]);
            TextView textView4 = (TextView) gridView.findViewById(R.id.price);
            textView4.setText(quoteprice[position]);
            TextView textView5 = (TextView) gridView.findViewById(R.id.address);
            textView5.setText(useraddress[position]);
            TextView textView6 = (TextView) gridView.findViewById(R.id.quoterating);
            textView6.setText(quoterating[position]);
            TextView textView7 = (TextView) gridView.findViewById(R.id.quantity);
            textView7.setText(quotequantity[position]);
            TextView textView8 = (TextView) gridView.findViewById(R.id.type);
            textView8.setText(quotetype[position]);
            TextView textView9 = (TextView) gridView.findViewById(R.id.phone);
            textView9.setText(userphone[position]);
        }
        else {
            gridView = (View) convertView;
        }

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
                upanel.putExtra("desc",quotedescription[position]);
                upanel.putExtra("price",quoteprice[position]);
                upanel.putExtra("quantity",quotequantity[position]);
                upanel.putExtra("type",quotetype[position]);
                context.startActivity(upanel);
                ((Activity)context).finish();
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
                upanel.putExtra("desc",quotedescription[position]);
                upanel.putExtra("price",quoteprice[position]);
                upanel.putExtra("quantity",quotequantity[position]);
                context.startActivity(upanel);
                ((Activity)context).finish();//finishes the activity
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
