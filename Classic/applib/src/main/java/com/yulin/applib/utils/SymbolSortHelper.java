package com.yulin.applib.utils;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.TextView;

public class SymbolSortHelper {
    private static class SortItem {
        private TextView mTvItem = null;
        private int mTvSortType = 0;

        SortItem(TextView tv, int sortType) {
            mTvItem = tv;
            mTvSortType = sortType;
        }

        public TextView getTvItem() {
            return mTvItem;
        }

        public int getSortType() {
            return mTvSortType;
        }
    }

    /**
     * 1:itemLable; 2:padding; 3:arrowColor(#ffffff); 4:arrow
     */
    private String SYMBOL_ITEM = "%s%s<font color=\"%s\"><b>%s</b></font>";

    public final static int SORT_RISE = 0x01;
    public final static int SORT_FALL = 0x02;
    public final static int SORT_DEFAULT = 0x04;

    private int mItemTextColor = Color.BLACK;
    private int mItemSelectedTextColor = Color.BLACK;
    // private Map<TextView, Integer> mLstSortItems = new HashMap<TextView,
    // Integer>();

    private List<SortItem> mLstSortItem = new ArrayList<SortItem>();

    private TextView mCurrSortItem = null;
    private int mCurrSortType = SORT_DEFAULT;

    private String mSymbolRise = "↓";
    private String mSymbolFall = "↑";
    private String mSymbolDefault = "　";
    private String mRiseColor = "#4690ef";
    private String mFallColor = "#4690ef";
    private String mDefaultColor = "#F8F8F8";
    private String mStrPadding = "";

    public SymbolSortHelper() {

    }

    public void setItemTextColor(int color) {
        mItemTextColor = color;
    }

    public void setItemSelectedTextColor(int color) {
        mItemSelectedTextColor = color;
    }

    /**
     * 向上箭头颜色
     * 
     * @param color 格式:"#e94b35"
     */
    public void setRiseSymbolColor(String color) {
        mRiseColor = color;
    }

    /**
     * 向下箭头颜色
     * 
     * @param color 格式:"#e94b35"
     */
    public void setFallSymbolColor(String color) {
        mFallColor = color;
    }
    
    /*
     * 设置不排序时symbol的颜色,一般设置为背景色
     */
    public void setDefaultSymbolColor(String color)
    {
        mDefaultColor = color;
    }

    public void setSymbolRise(String strRise) {
        mSymbolRise = strRise;
    }

    public void setSymbolFall(String strFall) {
        mSymbolFall = strFall;
    }

    public void setSymbolDefault(String strDefault) {
        mSymbolDefault = strDefault;
    }

    public void setStrPadding(String strPadding) {
        mStrPadding = strPadding;
    }

    public TextView getCurrentSortItem() {
        return mCurrSortItem;
    }

    public int getCurrentSortItemId() {
        View item = getCurrentSortItem();
        if (item != null) {
            return item.getId();
        }
        return -1;
    }

    public int getCurrentSortyType() {
        return mCurrSortType;
    }

    public void updateItemLable(List<String> lstItemLable) {
        resetSortHelper();
        for (int i = 0; i < mLstSortItem.size(); i++) {
            SortItem tItem = mLstSortItem.get(i);
            TextView tv = tItem.getTvItem();
            if (i <= lstItemLable.size() - 1) {
                tv.setText(lstItemLable.get(i) + mSymbolDefault);
                tv.setTag(lstItemLable.get(i));
            }
        }
    }

    public void updateItemLable_exceptSort(List<String> lstItemLable) {
        int tSortType = getCurrentSortyType();
        TextView tSortField = getCurrentSortItem();
        resetSortHelper();

        updateItemLable(lstItemLable);
        setDefaultSort(tSortField, tSortType);
    }

    public void addSortItem(final TextView tv, int sortType) {
        if (tv != null) {
            String lable = tv.getText().toString();
            tv.setTag(lable);
            SortItem tItem = new SortItem(tv, sortType);
            mLstSortItem.add(tItem);

            tv.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (mItemClickListener != null) {
                        int iRet = mItemClickListener.enableClick(tv);
                        if (iRet >= 0) {
                            doSort((TextView) v);
                        }
                    } else {
                        doSort((TextView) v);
                    }
                }
            });
        }

    }

    private void doSort(TextView v) {
        if (mCurrSortItem != v) {
            resetSortItem(mCurrSortItem);
            mCurrSortType = SORT_DEFAULT;
        }
        mCurrSortItem = (TextView) v;
        mCurrSortItem.setTextColor(mItemSelectedTextColor);

        int sortType = SORT_DEFAULT;
        for (int i = 0; i < mLstSortItem.size(); i++) {
            if (mLstSortItem.get(i).getTvItem() == mCurrSortItem) {
                sortType = mLstSortItem.get(i).getSortType();
            }
        }

        if (mCurrSortType == SORT_RISE) {
            if ((sortType & SORT_FALL) == SORT_FALL) {
                mCurrSortType = SORT_FALL;
                String item = String.format(SYMBOL_ITEM, mCurrSortItem.getTag(), mStrPadding, mFallColor, mSymbolFall);
                Spanned spannedItem = Html.fromHtml(item);
                mCurrSortItem.setText(spannedItem);
            } else if ((sortType & SORT_DEFAULT) == SORT_DEFAULT) {
                mCurrSortType = SORT_DEFAULT;
                String item = String.format(SYMBOL_ITEM, mCurrSortItem.getTag(), mStrPadding, mDefaultColor, mSymbolDefault);
                Spanned spannedItem = Html.fromHtml(item);
                mCurrSortItem.setText(spannedItem);
            }
        } else if (mCurrSortType == SORT_FALL) {
            if ((sortType & SORT_DEFAULT) == SORT_DEFAULT) {
                mCurrSortType = SORT_DEFAULT;
                String item = String.format(SYMBOL_ITEM, mCurrSortItem.getTag(), mStrPadding, mDefaultColor, mSymbolDefault);
                Spanned spannedItem = Html.fromHtml(item);
                mCurrSortItem.setText(spannedItem);
            } else if ((sortType & SORT_RISE) == SORT_RISE) {
                mCurrSortType = SORT_RISE;
                String item = String.format(SYMBOL_ITEM, mCurrSortItem.getTag(), mStrPadding, mRiseColor, mSymbolRise);
                Spanned spannedItem = Html.fromHtml(item);
                mCurrSortItem.setText(spannedItem);
            }
        } else if (mCurrSortType == SORT_DEFAULT) {
            if ((sortType & SORT_RISE) == SORT_RISE) {
                mCurrSortType = SORT_RISE;
                String item = String.format(SYMBOL_ITEM, mCurrSortItem.getTag(), mStrPadding, mRiseColor, mSymbolRise);
                Spanned spannedItem = Html.fromHtml(item);
                mCurrSortItem.setText(spannedItem);
            } else if ((sortType & SORT_FALL) == SORT_FALL) {
                mCurrSortType = SORT_FALL;
                String item = String.format(SYMBOL_ITEM, mCurrSortItem.getTag(), mStrPadding, mFallColor, mSymbolFall);
                Spanned spannedItem = Html.fromHtml(item);
                mCurrSortItem.setText(spannedItem);
            }
        }
        notifySort();
    }

    /**
     * 通知界面排序
     */
    public void notifySort() {
        if (mSortListener != null) {
            mSortListener.onSort(mCurrSortItem, mCurrSortType);
        }
    }

    /**
     * 设置初始排序
     * 
     * @param v
     * @param sortType
     */
    public void setDefaultSort(TextView v, int sortType) {
        if (v == null) {
            return;
        }
        if (mCurrSortItem != v) {
            resetSortItem(mCurrSortItem);
        }
        mCurrSortItem = (TextView) v;
        mCurrSortItem.setTextColor(mItemSelectedTextColor);
        mCurrSortType = sortType;
        if (mCurrSortType == SORT_FALL) {
            String item = String.format(SYMBOL_ITEM, mCurrSortItem.getTag(), mStrPadding, mFallColor, mSymbolFall);
            Spanned spannedItem = Html.fromHtml(item);
            mCurrSortItem.setText(spannedItem);
        } else if (mCurrSortType == SORT_DEFAULT) {
            String item = String.format(SYMBOL_ITEM, mCurrSortItem.getTag(), mStrPadding, mDefaultColor, mSymbolDefault);
            Spanned spannedItem = Html.fromHtml(item);
            mCurrSortItem.setText(spannedItem);
        } else if (mCurrSortType == SORT_RISE) {
            String item = String.format(SYMBOL_ITEM, mCurrSortItem.getTag(), mStrPadding, mRiseColor, mSymbolRise);
            Spanned spannedItem = Html.fromHtml(item);
            mCurrSortItem.setText(spannedItem);
        }

    }

    /**
     * 更新排行控件样式
     */
    public void updateSort() {
        for (SortItem sortItem : mLstSortItem) {
            TextView tv = sortItem.getTvItem();
            int type = sortItem.getSortType();
            if (tv == mCurrSortItem) {
                tv.setTextColor(mItemSelectedTextColor);
                if (type == SORT_FALL) {
                    String item = String.format(SYMBOL_ITEM, mCurrSortItem.getTag(), mStrPadding, mFallColor, mSymbolFall);
                    Spanned spannedItem = Html.fromHtml(item);
                    mCurrSortItem.setText(spannedItem);
                } else if (type == SORT_DEFAULT) {
                    String item = String.format(SYMBOL_ITEM, mCurrSortItem.getTag(), mStrPadding, mDefaultColor, mSymbolDefault);
                    Spanned spannedItem = Html.fromHtml(item);
                    mCurrSortItem.setText(spannedItem);
                } else if (type == SORT_RISE) {
                    String item = String.format(SYMBOL_ITEM, mCurrSortItem.getTag(), mStrPadding, mRiseColor, mSymbolRise);
                    Spanned spannedItem = Html.fromHtml(item);
                    mCurrSortItem.setText(spannedItem);
                }
            } else {
                tv.setTextColor(mItemTextColor);
                String item = String.format(SYMBOL_ITEM, tv.getTag(), mStrPadding, mDefaultColor, mSymbolDefault);
                Spanned spannedItem = Html.fromHtml(item);
                tv.setText(spannedItem);
            }

        }
    }

    private void resetSortItem(TextView tv) {
        if (tv == null) {
            return;
        }

        tv.setTextColor(mItemTextColor);
        String sLable = (String) tv.getTag();
        String item = String.format(SYMBOL_ITEM, sLable, mStrPadding, mDefaultColor, mSymbolDefault);
        Spanned spannedItem = Html.fromHtml(item);
        tv.setText(spannedItem);
    }

    public void resetSortHelper() {
        for (SortItem sortItem : mLstSortItem) {
            TextView tv = sortItem.getTvItem();
            resetSortItem(tv);
        }

        mCurrSortItem = null;
        mCurrSortType = SORT_DEFAULT;
    }

    public static interface OnSortListener {
        public void onSort(TextView tv, int sortType);
    }

    private OnSortListener mSortListener = null;

    public void setOnSortListener(OnSortListener listener) {
        mSortListener = listener;
    }

    public static interface OnItemClickListener {
        public int enableClick(TextView tv);
    }

    private OnItemClickListener mItemClickListener = null;

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

}
