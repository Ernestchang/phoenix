package com.hc.poem.widget.bar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import java.util.List;

/**
 * 标题栏 显示标题，以及左右导航按钮
 * 
 * emoney
 *
 */
public class TitleBar extends Bar {
    // protected int mItemTextColor = 0xfff8f8f8;
    // protected int mItemTextSize = 50;

    private LinearLayout mLeftContent = null;
    private LinearLayout mRightContent = null;
    private LinearLayout mCenterContent = null;

    private LinearLayout mCenterContent_sub = null;

    public static enum Position {
        LEFT, RIGHT, CENTER
    };

    public TitleBar(Context context) {
        super(context);
        init();
    }

    public TitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TitleBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        setItemTextSize(18);
        setItemTextColor(0xfff8f8f8);

        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        mLeftContent = new LinearLayout(getContext());
        params.addRule(ALIGN_PARENT_LEFT);
        params.addRule(CENTER_VERTICAL);
        mLeftContent.setLayoutParams(params);
        mLeftContent.setGravity(Gravity.CENTER);
        mLeftContent.setOrientation(LinearLayout.HORIZONTAL);
        addView(mLeftContent);

        params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        mRightContent = new LinearLayout(getContext());
        params.addRule(ALIGN_PARENT_RIGHT);
        params.addRule(CENTER_VERTICAL);
        mRightContent.setLayoutParams(params);
        mRightContent.setGravity(Gravity.CENTER);
        mRightContent.setOrientation(LinearLayout.HORIZONTAL);
        addView(mRightContent);

        params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        mCenterContent = new LinearLayout(getContext());
        mCenterContent.setId(mCenterContent.hashCode());
        params.addRule(CENTER_IN_PARENT);
        mCenterContent.setLayoutParams(params);
        mCenterContent.setGravity(Gravity.CENTER);
        mCenterContent.setOrientation(LinearLayout.HORIZONTAL);
        addView(mCenterContent);

        params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        mCenterContent_sub = new LinearLayout(getContext());
        params.addRule(CENTER_IN_PARENT);
        params.addRule(RIGHT_OF, mCenterContent.getId());
        params.setMargins(6, 0, 0, 0);
        mCenterContent_sub.setLayoutParams(params);
        mCenterContent_sub.setGravity(Gravity.CENTER);
        mCenterContent_sub.setOrientation(LinearLayout.HORIZONTAL);

        addView(mCenterContent_sub);

        setItemSelectable(false);
    }

    private View addCustomView(Position pos, BarMenuCustomItem item, int index) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        View view = createCustomItem(item, index, params);
        if (pos == Position.LEFT) {
            mLeftContent.addView(view);
        } else if (pos == Position.RIGHT) {
            mRightContent.addView(view);
        } else if (pos == Position.CENTER) {
            mCenterContent.addView(view);
        }
        return view;
    }

    private TextView addTextButton(Position pos, BarMenuTextItem item, int index) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        TextView tv = createTextItem(item, index, params);
        if (pos == Position.LEFT) {
            mLeftContent.addView(tv);
        } else if (pos == Position.RIGHT) {
            mRightContent.addView(tv);
        } else if (pos == Position.CENTER) {
            mCenterContent.addView(tv);
        }
        return tv;
    }

    private ImageView addImageButton(Position pos, BarMenuImgItem item, int index) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        ImageView iv = createImageItem(item, index, params);
        if (pos == Position.LEFT) {
            mLeftContent.addView(iv);
        } else if (pos == Position.RIGHT) {
            mRightContent.addView(iv);
        } else if (pos == Position.CENTER) {
            mCenterContent.addView(iv);
        }
        return iv;
    }

    private ViewSwitcher addGroupView(Position pos, BarMenuGroupItem groupItem, int index) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        ViewSwitcher vs = createGroupItem(groupItem, index, params);
        List<BarMenuItem> items = groupItem.getItems();
        for (int i = 0; i < items.size(); i++) {
            BarMenuItem item = items.get(i);
            if (item instanceof BarMenuTextItem) {
                BarMenuTextItem textItem = (BarMenuTextItem) item;
                TextView tv = createTextItem(textItem, index, params);
                vs.addView(tv);
            } else if (item instanceof BarMenuImgItem) {
                BarMenuImgItem imgItem = (BarMenuImgItem) item;
                ImageView iv = createImageItem(imgItem, index, params);
                vs.addView(iv);
            } else if (item instanceof BarMenuCustomItem) {
                BarMenuCustomItem viewItem = (BarMenuCustomItem) item;
                View view = createCustomItem(viewItem, index, params);
                vs.addView(view);
            }
        }
        if (pos == Position.LEFT) {
            mLeftContent.addView(vs);
        } else if (pos == Position.RIGHT) {
            mRightContent.addView(vs);
        } else if (pos == Position.CENTER) {
            mCenterContent.addView(vs);
        }
        return vs;
    }

    public void addMenuItemNow(final BarMenuItem item, final int index) {
        if (item instanceof BarMenuTextItem) {
            BarMenuTextItem textItem = (BarMenuTextItem) item;
            TextView tv = addTextButton((Position) textItem.getTag(), textItem, index);
            textItem.setItemView(tv);
        } else if (item instanceof BarMenuImgItem) {
            BarMenuImgItem imgItem = (BarMenuImgItem) item;
            addImageButton((Position) imgItem.getTag(), imgItem, index);
        } else if (item instanceof BarMenuCustomItem) {
            BarMenuCustomItem viewItem = (BarMenuCustomItem) item;
            addCustomView((Position) viewItem.getTag(), viewItem, index);
        } else if (item instanceof BarMenuGroupItem) {
            BarMenuGroupItem groupItem = (BarMenuGroupItem) item;
            ViewSwitcher vs = addGroupView((Position) groupItem.getTag(), groupItem, index);
            groupItem.setSwitcher(vs);
        }
    }

    public void setProgress(View pb) {
        mCenterContent_sub.removeAllViews();
        mCenterContent_sub.removeAllViewsInLayout();
        mCenterContent_sub.addView(pb);
    }

    public void showProgress(boolean b) {
        if (b) {
            mCenterContent_sub.setVisibility(View.VISIBLE);
        } else {
            mCenterContent_sub.setVisibility(View.INVISIBLE);
        }
    }

    public void addMenuItemNow(BarMenuItem item) {
        int leftCount = mLeftContent.getChildCount();
        int rightCount = mRightContent.getChildCount();
        int centerCount = mCenterContent.getChildCount();
        int count = leftCount + rightCount + centerCount;
        addMenuItemNow(item, count);
    }

    public void notifyBarSetChanged() {
        clearAll();
        BarMenu menu = getBarMenu();
        List<BarMenuItem> items = menu.getItems();
        for (int i = 0; i < items.size(); i++) {
            BarMenuItem item = items.get(i);
            addMenuItemNow(item, i);
        }

        View pr = menu.getProgress();
        if (pr != null) {
            setProgress(pr);
        }
    }

    public void performItemClick(int index) {

    }

    public void clear(Position position) {
        if (position == Position.LEFT) {
            clearLeft();
        } else if (position == Position.CENTER) {
            clearCenter();
        } else if (position == Position.RIGHT) {
            clearRight();
        }
    }

    private void clearLeft() {
        if (mLeftContent != null) {
            mLeftContent.removeAllViews();
            mLeftContent.removeAllViewsInLayout();
        }
    }

    private void clearCenter() {
        if (mCenterContent != null) {
            mCenterContent.removeAllViews();
            mCenterContent.removeAllViewsInLayout();
        }
    }

    private void clearRight() {
        if (mRightContent != null) {
            mRightContent.removeAllViews();
            mRightContent.removeAllViewsInLayout();
        }
    }

    private void clearAll() {
        clearLeft();
        clearCenter();
        clearRight();
        clearProgress();
    }

    public void clearProgress() {
        if (mCenterContent_sub != null) {
            mCenterContent_sub.removeAllViews();
            mCenterContent_sub.removeAllViewsInLayout();
        }
    }
}
