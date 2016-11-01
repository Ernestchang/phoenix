package com.yulin.applib.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher.ViewFactory;

/**
 * 滚动文字
 * 一组文字的滚动切换，可通过传入动画自定义滚动方式
 * emoney
 *
 */
public class TextScroller extends LinearLayout {

	private String[] mSzTexts = null;
	private TextSwitcher mSwitcher = null;
	private boolean mIsSwitching = false;
	private int mShowTime = 5000;
	private int mTextIndex = 0;
	
	private int mTextSize = 14;
	private int mTextColor = Color.WHITE;
	
	private int mSpeed = 15;
	private Runnable mRunnable = new Runnable(){

		@Override
		public void run() {
			// TODO Auto-generated method stub
			mTextIndex++;
			if(mTextIndex > mSzTexts.length - 1)
			{
				mTextIndex = 0;
			}
			mSwitcher.setText(mSzTexts[mTextIndex]);
		}
		
	};
	public TextScroller(Context context) {
		super(context);
		init();
	}

	public TextScroller(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init()
	{
		mSwitcher = new TextSwitcher(getContext());
		LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		mSwitcher.setLayoutParams(params);
		
		addView(mSwitcher);
	}
	public void setTextSize(int textSize)
	{
		mTextSize = textSize;
	}
	public void setTextColor(int textColor)
	{
		mTextColor = textColor;
	}
	public void setTexts(String[] strs)
	{
		mSzTexts = strs;
		mTextIndex = 0;
	}
	/**
	 * 设置文字滚动
	 * @param speed
	 */
	public void setScrollSpeed(int speed)
	{
		mSpeed = speed;
	}
	/**
	 * 开始切换
	 * @param time
	 */
	public void startSwitching(int time)
	{
		mIsSwitching = true;
		mShowTime = time;
		mTextIndex = 0;
		mSwitcher.setFactory(new ViewFactory() {  
			
			public View makeView() {  
				ScrollTextView tv = new ScrollTextView(getContext());  
				tv.setTextSize(mTextSize);  
				tv.setTextColor(mTextColor);
				tv.setSingleLine();
				tv.setHorizontallyScrolling(true);
				tv.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if(mTextClickListener != null)
						{
							mTextClickListener.onTextClick(mTextIndex);
						}
					}
				});
				return tv;  
			}  
		});
		mSwitcher.setText(mSzTexts[mTextIndex]);
	}
	/**
	 * 开始切换，附带动画
	 * @param time
	 * @param inAnimResId
	 * @param outAnimResId
	 */
	public void startScrolling(int time, int inAnimResId, int outAnimResId)
	{
		stopSwitching();
		Animation inAnim = AnimationUtils.loadAnimation(getContext(), inAnimResId);
		inAnim.setAnimationListener(new AnimationListener(){

			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				final ScrollTextView tv = (ScrollTextView) mSwitcher.getCurrentView();
				int width = getWidth();
				int w = tv.getWidth();
//				Toast.makeText(getContext(), "屏幕宽度："+width+" 文本宽度："+w, Toast.LENGTH_SHORT).show();
				//开始左右滑动
				
				tv.setWidth(w);
				if(w > width)
				{
					int offsetW = w - width;
					int time = offsetW*mSpeed;
					TranslateAnimation anim = new TranslateAnimation(0, -offsetW, 0, 0);
					anim.setAnimationListener(new AnimationListener() {
						
						@Override
						public void onAnimationStart(Animation animation) {
							// TODO Auto-generated method stub
							
						}
						
						@Override
						public void onAnimationRepeat(Animation animation) {
							// TODO Auto-generated method stub
							
						}
						
						@Override
						public void onAnimationEnd(Animation animation) {
							// TODO Auto-generated method stub
							postDelayed(mRunnable, mShowTime);
							
						}
					});
					anim.setDuration(time);
					anim.setFillAfter(true);
					anim.setFillEnabled(true);
					tv.startAnimation(anim);
				}
				else
				{
					postDelayed(mRunnable, mShowTime);
				}
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
		});
		Animation outAnim = AnimationUtils.loadAnimation(getContext(), outAnimResId);
		mSwitcher.setInAnimation(inAnim);
		mSwitcher.setOutAnimation(outAnim);
		startSwitching(time);
	}
	public void stopSwitching()
	{
		removeCallbacks(mRunnable);
		mIsSwitching = false;
	}
	public boolean isSwitching()
	{
		return mIsSwitching;
	}
	private OnTextClickListener mTextClickListener = null;
	public static interface OnTextClickListener
	{
		public void onTextClick(int index);
	}
	public void setOnTextClickListener(OnTextClickListener listener)
	{
		mTextClickListener = listener;
	}
	static class ScrollTextView extends TextView
	{

		public ScrollTextView(Context context, AttributeSet attrs, int defStyle) {
			super(context, attrs, defStyle);
			// TODO Auto-generated constructor stub
		}

		public ScrollTextView(Context context, AttributeSet attrs) {
			super(context, attrs);
			// TODO Auto-generated constructor stub
		}

		public ScrollTextView(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
		}
		public boolean isFocused()
		{
			return true;
		}

		@Override
		protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
			// TODO Auto-generated method stub
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
			int textW = (int) getPaint().measureText(getText().toString()) + 1;
			int w = getMeasuredWidth();
			if(w < textW)
			{
				w = textW;
			}
			int h = getMeasuredHeight();
			setMeasuredDimension(w, h);
		}
	}
}
