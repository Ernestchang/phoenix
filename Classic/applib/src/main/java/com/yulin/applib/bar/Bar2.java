package com.yulin.applib.bar;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Created by liulei0905 on 2016/11/1.
 *
 */
public class Bar2 extends RelativeLayout {

    private int mItemPaddingLeft;
    private int mItemPaddingTop;
    private int mItemPaddingRight;
    private int mItemPaddingBottom;

    private int mItemSelectedTextColor = Color.BLACK;
    private int mItemTextColor = Color.WHITE;

    public Bar2(Context context) {
        super(context);
    }

    public Bar2(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Bar2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setItemPaddings(int left, int top, int right, int bottom) {
        mItemPaddingLeft = left;
        mItemPaddingTop = top;
        mItemPaddingRight = right;
        mItemPaddingBottom = bottom;
    }

    public void setItemSelectedTextColor(int color) {
        mItemSelectedTextColor = color;
    }

    public void setItemTextColor(int color) {
        mItemTextColor = color;
    }

}
