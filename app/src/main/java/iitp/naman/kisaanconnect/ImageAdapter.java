package iitp.naman.kisaanconnect;

/**
 * Created by naman on 26-Nov-16.
 */

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


public class ImageAdapter extends BaseAdapter {
    private Context context;
    private final String[] categoryname;
    private final String[] categoryid;
    private final String[] categorydescription;
    private final String[] categorypicture;


    public ImageAdapter(Context context, String[] categoryname,String[] categorydescription,String[] categoryid,String[] categorypicture) {
        this.context = context;
        this.categorydescription=categorydescription;
        this.categoryid=categoryid;
        this.categoryname=categoryname;
        this.categorypicture=categorypicture;

    }

    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;

        if (convertView == null) {

            gridView = new View(context);

            // get layout from mobile.xml
            gridView = inflater.inflate(R.layout.mobile, null);

            // set value into textview
            TextView textView = (TextView) gridView
                    .findViewById(R.id.grid_item_label);
            textView.setText(categoryname[position]);
            TextView textView1 = (TextView) gridView
                    .findViewById(R.id.grid_item_id);
            textView1.setText(categoryid[position]);


            new DownloadImageTask((ImageView) gridView
                    .findViewById(R.id.grid_item_image))
                    .execute(categorypicture[position]);
            // set image based on selected text

           // ImageView imageView = (ImageView) gridView
                 //   .findViewById(R.id.grid_item_image);

            String mobile = categoryname[position];

            //if (imageView!=null) {
            //        //insert image here from given url
            //    imageView.setImageResource(R.drawable.arhar);
            //}

        } else {
            gridView = (View) convertView;
        }

        return gridView;
    }

    @Override
    public int getCount() {
        return categoryname.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

}
