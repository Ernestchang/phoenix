package com.yulin.applib.widget;

import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

public class EnhancedScrollView extends ScrollView {

	private ArrayList<OnScrollChangedListener> mOnScrollListeners = new ArrayList<OnScrollChangedListener>();
	public EnhancedScrollView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public EnhancedScrollView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public EnhancedScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
		for (OnScrollChangedListener oscl : mOnScrollListeners) {
			oscl.onVerticalScrollChanged(t);
		}
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		post(new Runnable() {

			@Override
			public void run() {
				for (OnScrollChangedListener oscl : mOnScrollListeners) {
					oscl.onVerticalScrollChanged(getScrollY());
				}
				invalidate();
			}
		});
	}
	public static interface OnScrollChangedListener
	{
		public void onVerticalScrollChanged(int offsetY);
	}
	public void addOnScrollListener(OnScrollChangedListener onScrollListener) {
		mOnScrollListeners.add(onScrollListener);
	}
}
