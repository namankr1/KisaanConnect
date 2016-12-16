package iitp.naman.kisaanconnect;

/**
 * Created by naman on 15-Dec-16.
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

public class UserOrderAdapter extends BaseAdapter {
    private Activity myactivity;
    private Context context;
    private String[] notificationid;
    private String[] notificationsenderphone;
    private String[] notificationsendername;
    private String[] notificationsenderaddress;
    private String[] notificationquantity;
    private String[] notificationprice;
    private String[] notificationquoteid;
    private String[] notificationstatus;
    private String inputphone1;

    private String[] notificationtype;

    public UserOrderAdapter(Activity myactivity,Context context,String inputphone1, String[] notificationid,String[] notificationsenderphone,String[] notificationsendername,String[] notificationsenderaddress,String[] notificationquantity,String[] notificationprice,String[] notificationquoteid,String[] notificationstatus,String[] notificationtype) {
        this.context = context;
        this.myactivity=myactivity;
        this.notificationid=notificationid;
        this.notificationsenderphone=notificationsenderphone;
        this.notificationsendername=notificationsendername;
        this.notificationsenderaddress=notificationsenderaddress;
        this.notificationquantity=notificationquantity;
        this.notificationprice=notificationprice;
        this.notificationquoteid=notificationquoteid;
        this.notificationstatus=notificationstatus;
        this.inputphone1=inputphone1;
        this.notificationtype=notificationtype;


    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        int notifstatus=0;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        try{
            notifstatus=Integer.parseInt(notificationstatus[position]);
        }
        catch (Exception e){
            notifstatus=0;
        }


        View gridView;
        if (convertView == null) {
            gridView = new View(context);
                gridView = inflater.inflate(R.layout.singleroworder, null);
        } else {
            gridView = (View) convertView;
        }

        TextView textView = (TextView) gridView.findViewById(R.id.list_item_string);

        if(notifstatus==1 || notifstatus==2){
            if(notifstatus==1){
                textView.setText(notificationsendername[position]+" has accepted your quotation of "+notificationtype[position]+" and wants " +notificationquantity[position]+" kg at rate Rs. "+notificationprice[position]+" ");
            }
            else{
                textView.setText(notificationsendername[position]+" has accepted you bid of "+notificationtype[position]+" and has finalised " +notificationquantity[position]+" kg at rate Rs. "+notificationprice[position]+" ");
            }
        }

            Button btnendnegotiation  = ((Button) gridView.findViewById(R.id.delete_btn));

            btnendnegotiation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent upanel = new Intent(context, OrderSummary.class);
                    upanel.putExtra("notificationid", notificationid[position]);
                    upanel.putExtra("notificationsenderphone", notificationsenderphone[position]);
                    upanel.putExtra("notificationsendername", notificationsendername[position]);
                    upanel.putExtra("notificationsenderaddress", notificationsenderaddress[position]);
                    upanel.putExtra("notificationquantity", notificationquantity[position]);
                    upanel.putExtra("notificationprice", notificationprice[position]);
                    upanel.putExtra("notificationquoteid", notificationquoteid[position]);
                    upanel.putExtra("notificationstatus", notificationstatus[position]);
                    upanel.putExtra("inputPhone1", inputphone1);
                    myactivity.startActivity(upanel);
                    //myactivity.finish();
                }
            });
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
