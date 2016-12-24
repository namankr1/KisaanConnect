package iitp.naman.kisaanconnect;

/**
 * Created by naman on 15-Dec-16.
 */

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import static android.content.Context.MODE_PRIVATE;

public class UserNegotiationAdapter extends BaseAdapter {
    private Activity myactivity;
    private Context context;
    private String[] notificationid;
    private String[] notificationsendername;
    private String[] notificationquantity;
    private String[] notificationprice;
    private String[] notificationstatus;

    private String[] notificationtype;
    private int poschooselan;

    public UserNegotiationAdapter(Activity myactivity,Context context, String[] notificationid,String[] notificationsendername,String[] notificationquantity,String[] notificationprice,String[] notificationstatus,String[] notificationtype) {
        this.context = context;
        this.myactivity=myactivity;
        this.notificationid=notificationid;
        this.notificationsendername=notificationsendername;
        this.notificationquantity=notificationquantity;
        this.notificationprice=notificationprice;
        this.notificationstatus=notificationstatus;
        this.notificationtype=notificationtype;
        SharedPreferences sfchoosenlan = context.getSharedPreferences("languagechoosen",MODE_PRIVATE);
        poschooselan = sfchoosenlan.getInt("position",0);
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        int notifstatus;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        try{
            notifstatus=Integer.parseInt(notificationstatus[position]);
        }
        catch (Exception e){
            notifstatus=0;
        }
        View gridView;
        //to supress warning
        if(myactivity!=null && notifstatus==0){
                notifstatus=0;
        }
        if (convertView == null) {
            gridView = inflater.inflate(R.layout.singlerownegotiation, null);
        } else {
            gridView = convertView;
        }
        TextView textView = (TextView) gridView.findViewById(R.id.list_item_string);

        if(notifstatus==1 || notifstatus==2){
            if(notifstatus==1){
                textView.setText(context.getResources().getString(R.string.javausernegotiationadapter_1)+" "+notificationsendername[position]+" "+context.getResources().getString(R.string.javausernegotiationadapter_2)+" "+notificationtype[position].split(";")[poschooselan]+" "+context.getResources().getString(R.string.javausernegotiationadapter_3) +" "+notificationquantity[position]+" "+context.getResources().getString(R.string.javausernegotiationadapter_4)+" "+notificationprice[position]+" ");
            }
            else{
                textView.setText(context.getResources().getString(R.string.javausernegotiationadapter_1)+" "+notificationsendername[position]+" "+context.getResources().getString(R.string.javausernegotiationadapter_2)+" "+notificationtype[position].split(";")[poschooselan]+" "+context.getResources().getString(R.string.javausernegotiationadapter_3) +" "+notificationquantity[position]+" "+context.getResources().getString(R.string.javausernegotiationadapter_4)+" "+notificationprice[position]+" ");
            }
        }

        return gridView;
    }

    @Override
    public int getCount() {
        return notificationid.length;
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

