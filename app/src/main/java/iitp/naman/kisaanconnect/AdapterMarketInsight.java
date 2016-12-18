package iitp.naman.kisaanconnect;

/**
 * Created by naman on 15-Dec-16.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AdapterMarketInsight extends BaseAdapter {
    private Context context;
    private String[] subcatid;
    private String[] subcatname;
    private String[] subcatpercent;
    private int count=0;

    public AdapterMarketInsight(Context context,String[] subcatid,String[] subcatname,String[] subcatpercent,int count) {
        this.context = context;
        this.subcatid=subcatid;
        this.subcatname=subcatname;
        this.subcatpercent=subcatpercent;
        this.count=count;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View gridView;
        if (convertView == null) {
            gridView = inflater.inflate(R.layout.singlerowmarketinsight, null);

            TextView textView = (TextView) gridView.findViewById(R.id.subcatname1);
            textView.setText(subcatname[position]);
            TextView textView1 = (TextView) gridView.findViewById(R.id.subcatpercent1);

            if(count==2){
                try{
                    if(subcatpercent[position].equals("Nil")){
                        textView1.setBackgroundResource(R.drawable.marketinsightnormal);
                    }
                    float temp;
                    if(subcatpercent[position].charAt(subcatpercent[position].length()-1)!='%'){
                        temp = Float.parseFloat(subcatpercent[position]);
                    }
                    else{
                        temp = Float.parseFloat(subcatpercent[position].substring(0,subcatpercent[position].length()-1));
                    }

                    if(temp==0){
                        textView1.setBackgroundResource(R.drawable.marketinsightstagnant);
                    }
                    else if(temp>0){
                        textView1.setBackgroundResource(R.drawable.marketinsightincrease);
                    }
                    else{
                        textView1.setBackgroundResource(R.drawable.marketinsightdecrease);
                    }

                }
                catch (Exception e){
                    textView1.setBackgroundResource(R.drawable.marketinsightnormal);
                }


            }
            else{
                textView1.setBackgroundResource(R.drawable.marketinsightnormal);
            }

            if(subcatpercent[position].length()>4){
                subcatpercent[position] = subcatpercent[position].substring(0,4);
            }
            if(!subcatpercent[position].equals("Nil") && subcatpercent[position].charAt(subcatpercent[position].length()-1)!='%'){
                subcatpercent[position] = subcatpercent[position]+"%";
            }

            textView1.setText(subcatpercent[position]);


        }
        else {
            gridView =  convertView;
        }
        return gridView;
    }

    @Override
    public int getCount() {
        return subcatid.length;
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
