package com.yulin.applib.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Scroller;

/**
 * 支持左右上下滑动，表头可固定的列表 涉及到下拉刷新，继承至@RefreshListView
 * 
 * emoney
 * 
 */
public class FixedListView extends RefreshListView {

	/** 手势 */
	private GestureDetector mGesture;
	private ViewGroup mFixedHeader;
	/** 偏移坐标 */
	private int mOffset = 0;

	private boolean mFlag = false;

	private Scroller mScroller = null;

	private int mRightWidth = 0;

	private int mOneItemWidth = 0;

	private int mMaxHScrollOffset = 0;
	private int mRightVisiableWidth = 0;

	private OnScrollListener mOnScrollListener = null;

	/** 构造函数 */
	public FixedListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public FixedListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public FixedListView(Context context) {
		super(context);
		init();
	}

	public void init() {
		mGesture = new GestureDetector(getContext(), mOnGesture);
		mScroller = new Scroller(getContext());
		super.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				if (mOnScrollListener != null) {
					mOnScrollListener.onScrollStateChanged(view, scrollState);
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				int count = getChildCount();
				for (int i = 0; i < count; i++) {
					View child = ((ViewGroup) getChildAt(i)).getChildAt(1);
					if (child != null && child.getScrollX() != mOffset) {
						child.scrollTo(mOffset, 0);
					}
				}
				if (mOnScrollListener != null) {
					mOnScrollListener.onScroll(view, firstVisibleItem,
							visibleItemCount, totalItemCount);
				}
			}
		});
	}

	@Override
	public void setOnScrollListener(OnScrollListener l) {
		// TODO Auto-generated method stub
		mOnScrollListener = l;
	}

	/** 分发触摸事件 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (mGesture.onTouchEvent(ev)) {
			MotionEvent cancelEvent = MotionEvent.obtain(ev);
			cancelEvent.setAction(MotionEvent.ACTION_CANCEL);
			super.dispatchTouchEvent(cancelEvent);
			cancelEvent.recycle();
			return true;
		}
		return super.dispatchTouchEvent(ev);
	}

	/** 手势 */
	private OnGestureListener mOnGesture = new GestureDetector.SimpleOnGestureListener() {
		private int verticalMinDistance = 0;

		private int bigVelocity = 3500;
		private int minVelocity = 0;

		@Override
		public boolean onDown(MotionEvent e) {
			return false;
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {

			if (mFlag && !isRelease2Refresh() && !isPull2Refresh()) {
				int dx = 0;
				boolean bflag = false;
				if (e1.getX() - e2.getX() > verticalMinDistance
						&& Math.abs(velocityX) > minVelocity) {
					// 向左
					if (Math.abs(velocityX) > bigVelocity) {
						dx = mMaxHScrollOffset - mOffset;
						bflag = true;
					}
				} else if (e2.getX() - e1.getX() > verticalMinDistance
						&& Math.abs(velocityX) > minVelocity) {
					// 向右
					if (Math.abs(velocityX) > bigVelocity) {
						dx = -mOffset;
						bflag = true;
					}
				}
				if (bflag == true) {
					if (dx != 0) {
						int t_n = Math.abs(dx) / mOneItemWidth;
						mScroller.startScroll(mOffset, 0, dx, 0, t_n * 160);
						invalidate();
					}
					return true;
				}
				
			}
			
			return false;
		}

		/** 滚动 */
		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {

			if (Math.abs(distanceX) > Math.abs(distanceY) + 20) {
				mFlag = true;

				if (!mScroller.isFinished()) {
					mScroller.abortAnimation();
				}

				synchronized (FixedListView.this) {
					int moveX = (int) distanceX;
					int dx = 0;

					if (moveX > 0) {
						// 向左
						if (mOneItemWidth > 0) {
							if (mOffset < mMaxHScrollOffset) {
								int nextWith = 0;
								for (int i = 1;; i++) {
									nextWith = i * mOneItemWidth * 2;

									if (mOffset < nextWith) {
										break;
									}
								}
								if (nextWith > mMaxHScrollOffset) {
									nextWith = mMaxHScrollOffset;
								}
								dx = nextWith - mOffset;
							} else {
								dx = 0;
							}
						} else {
							dx = mMaxHScrollOffset - mOffset;
						}
					} else if (moveX < 0) {
						// 向右
						if (mOneItemWidth > 0) {
							if (mOffset > 0) {
								int nextWith = 0;
								for (int i = 1;; i++) {
									nextWith = mMaxHScrollOffset
											- (i * mOneItemWidth * 2);

									if (mOffset > nextWith) {
										break;
									}
								}
								if (nextWith < 0) {
									nextWith = 0;
								}
								dx = nextWith - mOffset;
							} else {
								dx = 0;
							}
						} else {
							dx = -mOffset;
						}
					}
					mScroller.startScroll(mOffset, 0, dx, 0, 300);

					invalidate();
					return true;

					// int curX = mFixedHeader.getScrollX();
					// int dx = moveX;
					// // 控制越界问题
					// if (curX + moveX < 0)
					// dx = 0;
					// if (curX + moveX + mRightVisiableWidth >= mRightWidth)
					// dx = mRightWidth - mRightVisiableWidth - curX;
					//
					// mOffset += dx;
					//
					// // 根据手势滚动Item视图
					// int count = getChildCount();
					// for (int i = 0; i < count; i++) {
					// View child = ((ViewGroup) getChildAt(i)).getChildAt(1);
					// if (child != null && child.getScrollX() != mOffset)
					// child.scrollTo(mOffset, 0);
					// }
					// mFixedHeader.scrollBy(dx, 0);
				}

			} else {
				mFlag = false;
				return false;
			}

		}

	};

	@Override
	public void computeScroll() {
		// TODO Auto-generated method stub
		if (mScroller.computeScrollOffset()) {
			int offset = mScroller.getCurrX();

			if (offset >= 0 && offset <= mMaxHScrollOffset) {
				mOffset = offset;
				int count = getChildCount();
				for (int i = 0; i < count; i++) {
					View child = ((ViewGroup) getChildAt(i)).getChildAt(1);
					if (child != null && child.getScrollX() != mOffset) {
						child.scrollTo(mOffset, 0);
					}
				}
				mFixedHeader.scrollTo(mOffset, 0);
				invalidate();
			}
		}
	}

	/** 获取列头偏移量 */
	public int getHeadScrollX() {
		return mFixedHeader.getScrollX();
	}

	/**
	 * 绑定固定表头
	 * 
	 * @param view
	 */
	public void bindFixedHeader(ViewGroup view) {
		mFixedHeader = view;
	}

	/**
	 * 设置一个item的宽度
	 * 
	 * @param view
	 */
	public void setOneItemWidth(int oneItemWidth) {
		this.mOneItemWidth = oneItemWidth;
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		super.onLayout(changed, l, t, r, b);
		if (getChildCount() > 0) {
			mRightWidth = 0;
			mRightVisiableWidth = mFixedHeader.getMeasuredWidth();
			for (int i = 0; i < mFixedHeader.getChildCount(); i++) {
				View child = mFixedHeader.getChildAt(i);
				mRightWidth += child.getMeasuredWidth();
			}
			mMaxHScrollOffset = mRightWidth - mRightVisiableWidth;
		}
	}

}
