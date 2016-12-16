package iitp.naman.kisaanconnect;

/**
 * Created by naman on 26-Nov-16.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View gridView;
        if (convertView == null) {
            gridView = new View(context);
            gridView = inflater.inflate(R.layout.mobile, null);
            TextView textView = (TextView) gridView.findViewById(R.id.grid_item_label);
            textView.setText(categoryname[position]);
            TextView textView1 = (TextView) gridView.findViewById(R.id.grid_item_id);
            textView1.setText(categoryid[position]);
            new DownloadImageTaskOffline((ImageView) gridView.findViewById(R.id.grid_item_image)).execute(categorypicture[position]);
            //new DownloadImageTaskOnline((ImageView) gridView.findViewById(R.id.grid_item_image)).execute(categorypicture[position]);
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

    private class DownloadImageTaskOnline extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTaskOnline(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            urldisplay = context.getResources().getString(R.string.network_home) +urldisplay;
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
                String extr = Environment.getExternalStorageDirectory().toString();
                File mFolder = new File(extr + "/KisaanConnect");

                if (!mFolder.exists()) {
                    mFolder.mkdir();
                }
                String strF = mFolder.getAbsolutePath();

                String temp = urls[0];

                String[] subnames = temp.split("/");
                int lensubnames = subnames.length;
                File mSubFolder = new File(strF +"/" +subnames[lensubnames-2]);
                if (!mSubFolder.exists()) {
                    mSubFolder.mkdir();
                }
                String s = subnames[lensubnames-1];
                File f = new File(mSubFolder.getAbsolutePath(),s);
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(f);
                    mIcon11.compress(Bitmap.CompressFormat.JPEG,100, fos);

                    fos.flush();
                    fos.close();
                    //   MediaStore.Images.Media.insertImage(getContentResolver(), b, "Screen", "screen");
                }catch (FileNotFoundException e) {

                    e.printStackTrace();
                } catch (Exception e) {

                    e.printStackTrace();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

    private class DownloadImageTaskOffline extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;
        String urldis="";

        public DownloadImageTaskOffline(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            Bitmap mIcon11 = null;
            urldis=urls[0];
            try {

                String extr = Environment.getExternalStorageDirectory().toString();
                File mFolder = new File(extr + "/KisaanConnect");
                String strF = mFolder.getAbsolutePath();

                String temp = urls[0];

                String[] subnames = temp.split("/");
                int lensubnames = subnames.length;
                File mSubFolder = new File(strF +"/" +subnames[lensubnames-2]);
                String s = subnames[lensubnames-1];
                File f = new File(mSubFolder.getAbsolutePath(),s);

                mIcon11 = BitmapFactory.decodeFile(f.getAbsolutePath());


            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            if(result!=null) {
                bmImage.setImageBitmap(result);
            }
            else{
                new DownloadImageTaskOnline(bmImage).execute(urldis);
            }
        }
    }
}
