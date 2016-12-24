package iitp.naman.kisaanconnect;

/**
 * Created by naman on 29-Nov-16.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import static android.content.Context.MODE_PRIVATE;

public class AdapterQuotes extends BaseAdapter {
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
    private String[] userrating;
    private int poschooselan;

    public AdapterQuotes(Context context, String[] quotebidvalue,String[] quotedescription,String[] quoteid,String[] quoteprice,String[] quotequantity,String[] quoterating,String[] userphone,String[] username,String[] useraddress,String[] userrating) {
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
        this.userrating=userrating;
        SharedPreferences sf1 = context.getSharedPreferences("languagechoosen",MODE_PRIVATE);
        poschooselan = sf1.getInt("position",0);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View gridView;
        if (convertView == null) {
            gridView = inflater.inflate(R.layout.singlerowquote, null);
        }
        else {
            gridView =  convertView;
        }

        ((TextView) gridView.findViewById(R.id.userrating)).setText(userrating[position]);
        ((TextView) gridView.findViewById(R.id.bidvalue)).setText(quotebidvalue[position]);
        try {
            ((TextView) gridView.findViewById(R.id.description)).setText(quotedescription[position].split(";")[poschooselan]);
        }
        catch(Exception e){
            ((TextView) gridView.findViewById(R.id.description)).setText(context.getResources().getString(R.string.deletequote_7));
        }
        ((TextView) gridView.findViewById(R.id.quoteid)).setText(quoteid[position]);
        ((TextView) gridView.findViewById(R.id.price)).setText(quoteprice[position]);
        ((TextView) gridView.findViewById(R.id.address)).setText(useraddress[position]);
        ((TextView) gridView.findViewById(R.id.quoterating)).setText(quoterating[position]);
        ((TextView) gridView.findViewById(R.id.quantity)).setText(quotequantity[position]);
        ((TextView) gridView.findViewById(R.id.nameofseller)).setText(username[position]);
        ((TextView) gridView.findViewById(R.id.phone)).setText(userphone[position]);

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
