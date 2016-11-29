package iitp.naman.kisaanconnect;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;

/**
 * Created by naman on 27-Nov-16.
 */

public class Adaptercls extends BaseAdapter {
    Context context;
    String []govtnotifbody;
    String []govtnotifid;
    String []govtnotiftitle;
    String []govtnotifurl;
    Adaptercls(Context c,String []govtnotifbody,String []govtnotifid,String []govtnotiftitle,String []govtnotifurl)
    {
        this.context = c;
        this.govtnotifbody = govtnotifbody;
        this.govtnotifid=govtnotifid;
        this.govtnotiftitle=govtnotiftitle;
        this.govtnotifurl=govtnotifurl;

    }

    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;
        Log.i("Response :","adapter : ");
        if (convertView == null) {

            gridView = new View(context);

            // get layout from mobile.xml
            gridView = inflater.inflate(R.layout.single_row_notif, null);

            // set value into textview
            TextView textView = (TextView) gridView
                    .findViewById(R.id.notifid);
            textView.setText(govtnotifid[position]);
            TextView textView1 = (TextView) gridView
                    .findViewById(R.id.body);
            textView1.setText(govtnotifbody[position]);
            TextView textView2 = (TextView) gridView
                    .findViewById(R.id.title);
            textView2.setText(govtnotiftitle[position]);
            TextView textView3 = (TextView) gridView
                    .findViewById(R.id.url);
            textView3.setText(govtnotifurl[position]);


        } else {
            gridView = (View) convertView;
        }
        Log.i("Response :","adapter : ");

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
