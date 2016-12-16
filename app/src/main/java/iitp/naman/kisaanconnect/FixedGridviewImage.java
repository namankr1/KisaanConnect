package iitp.naman.kisaanconnect;

/**
 * Created by naman on 15-Dec-16.
 */

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;


public class FixedGridviewImage extends GridView {

    public FixedGridviewImage(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public FixedGridviewImage(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FixedGridviewImage(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec;
            expandSpec = MeasureSpec.makeMeasureSpec(
                    650, MeasureSpec.EXACTLY);

        super.onMeasure(widthMeasureSpec, expandSpec);

    }
}



