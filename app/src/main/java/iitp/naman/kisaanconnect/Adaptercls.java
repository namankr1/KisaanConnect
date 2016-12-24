package iitp.naman.kisaanconnect;

/**
 * Created by naman on 27-Nov-16.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.BaseAdapter;

import static android.content.Context.MODE_PRIVATE;

public class Adaptercls extends BaseAdapter {
    private Context context;
    private String []govtnotifbody;
    private String []govtnotifid;
    private String []govtnotiftitle;
    private String []govtnotifurl;
    private int poschooselan;

    Adaptercls(Context c,String []govtnotifbody,String []govtnotifid,String []govtnotiftitle,String []govtnotifurl) {
        this.context = c;
        this.govtnotifbody = govtnotifbody;
        this.govtnotifid=govtnotifid;
        this.govtnotiftitle=govtnotiftitle;
        this.govtnotifurl=govtnotifurl;
        SharedPreferences sfchoosenlan = context.getSharedPreferences("languagechoosen",MODE_PRIVATE);
        poschooselan = sfchoosenlan.getInt("position",0);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View gridView;
        if (convertView == null) {
            gridView = inflater.inflate(R.layout.single_row_notif, null);
        }
        else {
            gridView = convertView;
        }

        String temp;
        try {
            temp= govtnotifbody[position].split(";")[poschooselan];
        }
        catch (Exception e){
            temp= govtnotifbody[position].split(";")[0];
            e.printStackTrace();
        }
        if(temp.length()>120){
            temp = temp.substring(0,120)+context.getResources().getString(R.string.readmore);
        }
        ((TextView) gridView.findViewById(R.id.notifid)).setText(govtnotifid[position]);
        ((TextView) gridView.findViewById(R.id.body)).setText(temp);
        try {
            ((TextView) gridView.findViewById(R.id.title)).setText(govtnotiftitle[position].split(";")[poschooselan]);
        }
        catch (Exception e){
            ((TextView) gridView.findViewById(R.id.title)).setText(context.getResources().getString(R.string.deletequote_7));
            e.printStackTrace();
        }
        ((TextView) gridView.findViewById(R.id.url)).setText(govtnotifurl[position]);
        return gridView;
    }

    @Override
    public int getCount() {
        return govtnotifid.length;
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
