package com.yulin.applib.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

public class SortHelper {

	public final static int SORT_RISE = 0x01;
	public final static int SORT_FALL = 0x02;
	public final static int SORT_DEFAULT = 0x04;

	private int mItemTextColor = Color.BLACK;
	private int mItemSelectedTextColor = Color.BLACK;
	private Map<TextView, Integer> mLstSortItems = new HashMap<TextView, Integer>();
	private TextView mCurrSortItem = null;
	private int mCurrSortType = SORT_DEFAULT;

	private int mImgRise = 0;
	private int mImgFall = 0;
	private int mImgDefault = 0;
	
	private int mItemPadding = 0;

	public SortHelper() {

	}

	/*
	 * 文字和箭头之间的padding
	 * padding px
	 */
	public void setItemPadding(int padding)
	{
		mItemPadding = padding;
	}
	
	public void setItemTextColor(int color) {
		mItemTextColor = color;
	}

	public void setItemSelectedTextColor(int color) {
		mItemSelectedTextColor = color;
	}

	public void setImageRise(int imgRise) {
		mImgRise = imgRise;
	}

	public void setImageFall(int imgFall) {
		mImgFall = imgFall;
	}

	public void setImageDefault(int imgDefault) {
		mImgDefault = imgDefault;
	}

	public void addSortItem(final TextView tv, int sortType) {
		if (tv != null) {
			mLstSortItems.put(tv, sortType);
			tv.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					doSort((TextView) v);
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
		mCurrSortItem.setCompoundDrawablePadding(mItemPadding);
		int sortType = mLstSortItems.get(mCurrSortItem);
		if (mCurrSortType == SORT_RISE) {
			if ((sortType & SORT_FALL) == SORT_FALL) {
				mCurrSortType = SORT_FALL;
				mCurrSortItem.setCompoundDrawablesWithIntrinsicBounds(0, 0, mImgFall, 0);
			} else if ((sortType & SORT_DEFAULT) == SORT_DEFAULT) {
				mCurrSortType = SORT_DEFAULT;
				mCurrSortItem.setCompoundDrawablesWithIntrinsicBounds(0, 0, mImgDefault, 0);
			}
		} else if (mCurrSortType == SORT_FALL) {
			if ((sortType & SORT_DEFAULT) == SORT_DEFAULT) {
				mCurrSortType = SORT_DEFAULT;
				mCurrSortItem.setCompoundDrawablesWithIntrinsicBounds(0, 0, mImgDefault, 0);
			} else if ((sortType & SORT_RISE) == SORT_RISE) {
				mCurrSortType = SORT_RISE;
				mCurrSortItem.setCompoundDrawablesWithIntrinsicBounds(0, 0, mImgRise, 0);
			}
		} else if (mCurrSortType == SORT_DEFAULT) {
			if ((sortType & SORT_RISE) == SORT_RISE) {
				mCurrSortType = SORT_RISE;
				mCurrSortItem.setCompoundDrawablesWithIntrinsicBounds(0, 0, mImgRise, 0);
			} else if ((sortType & SORT_FALL) == SORT_FALL) {
				mCurrSortType = SORT_FALL;
				mCurrSortItem.setCompoundDrawablesWithIntrinsicBounds(0, 0, mImgFall, 0);
			}
		}
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
		if (mCurrSortItem != v) {
			resetSortItem(mCurrSortItem);
		}
		mCurrSortItem = (TextView) v;
		mCurrSortItem.setTextColor(mItemSelectedTextColor);
		mCurrSortItem.setCompoundDrawablePadding(mItemPadding);
		mCurrSortType = sortType;
		if (mCurrSortType == SORT_FALL) {
			mCurrSortItem.setCompoundDrawablesWithIntrinsicBounds(0, 0, mImgFall, 0);
		} else if (mCurrSortType == SORT_DEFAULT) {
			mCurrSortItem.setCompoundDrawablesWithIntrinsicBounds(0, 0, mImgDefault, 0);
		} else if (mCurrSortType == SORT_RISE) {
			mCurrSortItem.setCompoundDrawablesWithIntrinsicBounds(0, 0, mImgRise, 0);
		}
	}

	/**
	 * 更新排行控件样式
	 */
	public void updateSort() {
		for (Entry<TextView, Integer> entry : mLstSortItems.entrySet()) {
			TextView tv = entry.getKey();
			int type = entry.getValue();
			if (tv == mCurrSortItem) {
				tv.setTextColor(mItemSelectedTextColor);
				mCurrSortItem.setCompoundDrawablePadding(mItemPadding);
				if (type == SORT_FALL) {
					mCurrSortItem.setCompoundDrawablesWithIntrinsicBounds(0, 0, mImgFall, 0);
				} else if (type == SORT_DEFAULT) {
					mCurrSortItem.setCompoundDrawablesWithIntrinsicBounds(0, 0, mImgDefault, 0);
				} else if (type == SORT_RISE) {
					mCurrSortItem.setCompoundDrawablesWithIntrinsicBounds(0, 0, mImgRise, 0);
				}
			} else {
				tv.setTextColor(mItemTextColor);
				tv.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
			}
			
		}
	}

	private void resetSortItem(TextView tv) {
		if (tv == null) {
			return;
		}
		mCurrSortItem.setCompoundDrawablePadding(0);
		tv.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
		tv.setTextColor(mItemTextColor);
	}

	private OnSortListener mSortListener = null;

	public void setOnSortListener(OnSortListener listener) {
		mSortListener = listener;
	}

	public static interface OnSortListener {
		public void onSort(TextView tv, int sortType);
	}
}
