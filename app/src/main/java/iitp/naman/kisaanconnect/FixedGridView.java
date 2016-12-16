package iitp.naman.kisaanconnect;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by naman on 15-Dec-16.
 */

public class FixedGridView extends GridView {

    public FixedGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public FixedGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FixedGridView(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec;
        if(getLayoutParams().height==LayoutParams.WRAP_CONTENT){
            expandSpec = MeasureSpec.makeMeasureSpec(
                    Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        }
        else{
            expandSpec = heightMeasureSpec;
        }
        super.onMeasure(widthMeasureSpec, expandSpec);

    }
}


