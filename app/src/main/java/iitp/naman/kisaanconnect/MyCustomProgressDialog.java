package iitp.naman.kisaanconnect;

/**
 * Created by naman on 04-Dec-16.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;import android.widget.ImageView;


public class MyCustomProgressDialog extends ProgressDialog {
    private AnimationDrawable animation;

    public static ProgressDialog ctor(Context context) {
//        MyCustomProgressDialog dialog = new MyCustomProgressDialog(context);
        MyCustomProgressDialog dialog1;
        if (Build.VERSION.SDK_INT >= 21) {
            dialog1   = new MyCustomProgressDialog(context, android.R.style.Theme_WallpaperSettings);
        } else {
            dialog1   = new MyCustomProgressDialog(context);
        }
//
        dialog1.setIndeterminate(true);
        dialog1.setCancelable(false);

//        if(Build.VERSION.SDK_INT>=23) {
//
//            try {
//                if (dialog.getWindow().getDecorView() != null) {
//                    InsetDrawable background =
//                            (InsetDrawable) dialog.getWindow().getDecorView().getBackground();
//                    background.setAlpha(0);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
        return dialog1;
    }

    public MyCustomProgressDialog(Context context) {
        super(context);
    }

    public MyCustomProgressDialog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_custom_progress_dialog);
        ImageView la = (ImageView) findViewById(R.id.animation);
        la.setBackgroundResource(R.drawable.custom_progress_dialog_animation);
        animation = (AnimationDrawable) la.getBackground();
    }

    @Override
    public void show() {
        super.show();
        animation.start();
    }

    @Override
    public void dismiss() {
        super.dismiss();
        animation.stop();
    }
}
