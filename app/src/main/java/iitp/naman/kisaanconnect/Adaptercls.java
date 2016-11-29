package iitp.naman.kisaanconnect;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by naman on 27-Nov-16.
 */

public class Adaptercls extends ArrayAdapter<String>

{
    Context context;
    int []images;
    String []b;
    Adaptercls(Context c,String []b,int []imgs)
    {
        super(c,R.layout.single_row_notif,R.id.textView,b);
        this.context = c;
        this.images = imgs;
        this.b = b;

    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.single_row_notif,parent,false);
        ImageView grainImage = (ImageView) row.findViewById(R.id.imageView);
        TextView buy_arr = (TextView) row.findViewById(R.id.textView);
        grainImage.setImageResource(images[position]);
        buy_arr.setText(b[position]);
        return row;
    }
}
