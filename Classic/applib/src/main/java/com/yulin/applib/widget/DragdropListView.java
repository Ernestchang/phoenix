package com.yulin.applib.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

/**
 * 允许拖拽的列表控件
 * emoney
 */
public class DragdropListView extends ListView
{
	private ImageView m_DragView;
	private WindowManager m_WindowManager;
	private WindowManager.LayoutParams m_WindowParams;
	private int m_nDragPos;
	private int m_nFirstDragPos; // 初始移动的位置
	private int m_nDragPoint; // 当前移动位置
	private int m_nCoordOffset; // 坐标
	private OnDragListener m_DragListener;
	private OnDropListener m_DropListener;
	private OnRemoveListener m_RemoveListener;
	private int m_nUpperBound;
	private int m_nLowerBound;
	private int m_nHeight;
	private GestureDetector m_GestureDetector;
	public static final int S_FLING = 0;
	public static final int S_SLIDE_RIGHT = 1;
	public static final int S_SLIDE_LEFT = 2;
	private int m_nRemoveMode = -1;
	private Rect m_TempRect = new Rect();
	private Bitmap m_DragBitmap;
	private int m_nTouchSlop;
	private int m_nItemHeightNormal = -1;
	private int m_nItemHeightExpanded = -1;
	private int m_nDragndropBackgroundColor = 0x00000000;

	private int mDragViewId = 0;
	public static final String DRAGDROP_DEFAULT_VIEW = "dragdroplistview_view";
	public DragdropListView(Context context, AttributeSet attrs)
	{
		this(context, attrs, 0);
		m_nTouchSlop = ViewConfiguration.getTouchSlop();
		mDragViewId = getContext().getResources().getIdentifier(DRAGDROP_DEFAULT_VIEW, "id", getContext().getPackageName());
	}

	public DragdropListView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		m_nTouchSlop = ViewConfiguration.getTouchSlop();
		mDragViewId = getContext().getResources().getIdentifier(DRAGDROP_DEFAULT_VIEW, "id", getContext().getPackageName());
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev)
	{
		if (m_RemoveListener != null && m_GestureDetector == null)
		{
			if (m_nRemoveMode == S_FLING)
			{
				m_GestureDetector = new GestureDetector(new SimpleOnGestureListener()
				{
					public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
					{
						if (m_DragView != null)
						{
							if (velocityX > 1000)
							{
								Rect r = m_TempRect;
								m_DragView.getDrawingRect(r);
								if (e2.getX() > r.right * 2 / 3)
								{
									stopDragging();
									m_RemoveListener.onRemove(m_nFirstDragPos);
									unExpandViews(true);
								}
							}
							return true;
						}
						return false;
					}
				});
			}
		}
		
		if (m_DragListener != null || m_DropListener != null)
		{
			switch (ev.getAction())
			{
				case MotionEvent.ACTION_DOWN:
					int x = (int) ev.getX();
					int y = (int) ev.getY();
					int itemnum = pointToPosition(x, y);
					if (itemnum == AdapterView.INVALID_POSITION)
					{
						break;
					}
					ViewGroup item = (ViewGroup) getChildAt(itemnum - getFirstVisiblePosition());
					m_nDragPoint = y - item.getTop();
					m_nCoordOffset = ((int) ev.getRawY()) - y;
					if(mDragViewId == 0)
					{
						throw new RuntimeException(String.format("You forgot call setDragViewId, Or you can add this attributes or use '%s' identifiers", DRAGDROP_DEFAULT_VIEW));
					}
					View dragger = item.findViewById(mDragViewId);
					Rect r = m_TempRect;

					r.left = dragger.getLeft();
					r.right = dragger.getRight();
					r.top = dragger.getTop();
					r.bottom = dragger.getBottom();

					if ((r.left < x) && (x < r.right) && (r.top < y) && (r.bottom > y))
					{
						item.setDrawingCacheEnabled(true);
						// item缓存，可设置缓存质量
						Bitmap bitmap = Bitmap.createBitmap(item.getDrawingCache());
						startDragging(bitmap, y);
						m_nDragPos = itemnum;
						m_nFirstDragPos = m_nDragPos;
						m_nHeight = getHeight();
						int touchSlop = m_nTouchSlop;
						m_nUpperBound = Math.min(y - touchSlop, m_nHeight / 5);
						m_nLowerBound = Math.max(y + touchSlop, m_nHeight * 2 / 5);
						return true;
					}
					m_DragView = null;
					break;
				case MotionEvent.ACTION_UP:
					break;
			}
		}
		return super.onInterceptTouchEvent(ev);
	}

	private int myPointToPosition(int x, int y)
	{
		Rect frame = m_TempRect;
		final int count = getChildCount();
		for (int i = count - 1; i >= 0; i--)
		{
			final View child = getChildAt(i);
			child.getHitRect(frame);
			if (frame.contains(x, y))
			{
				return getFirstVisiblePosition() + i;
			}
		}
		return INVALID_POSITION;
	}

	private int getItemForPosition(int y)
	{
		int nbyHeight = 0;
		int nPosx = getPaddingLeft() + 5;
		if(getChildAt(0) != null)
		{
			nbyHeight = getChildAt(0).getHeight() / 2;
			nPosx = getPaddingLeft() + getChildAt(0).getWidth() / 2;
		}
		int adjustedy = y - m_nDragPoint - nbyHeight;
		int pos = myPointToPosition(nPosx, adjustedy);
		if (pos >= 0)
		{
			if (pos <= m_nFirstDragPos)
			{
				pos += 1;
			}
		} else if (adjustedy < 0)
		{
			pos = 0;
		}
		return pos;
	}

	private void adjustScrollBounds(int y)
	{
		if (y >= m_nHeight / 3)
		{
			m_nUpperBound = m_nHeight / 3;
		}
		if (y <= m_nHeight * 2 / 3)
		{
			m_nLowerBound = m_nHeight * 2 / 3;
		}
	}

	private void unExpandViews(boolean deletion)
	{
		for (int i = 0;; i++)
		{
			View v = getChildAt(i);
			if (v == null)
			{
				if (deletion)
				{
					int position = getFirstVisiblePosition();
					int y = getChildAt(0).getTop();
					setAdapter(getAdapter());
					setSelectionFromTop(position, y);
				}
				layoutChildren(); // 强制刷新孩子节点
				v = getChildAt(i);
				if (v == null)
				{
					break;
				}
			}
			ViewGroup.LayoutParams params = v.getLayoutParams();
			params.height = m_nItemHeightNormal;
			v.setLayoutParams(params);
			v.setVisibility(View.VISIBLE);
		}
	}

	private void doExpansion()
	{
		int childnum = m_nDragPos - getFirstVisiblePosition();
		if (m_nDragPos > m_nFirstDragPos)
		{
			childnum++;
		}

		View first = getChildAt(m_nFirstDragPos - getFirstVisiblePosition());

		for (int i = 0;; i++)
		{
			View vv = getChildAt(i);
			if (vv == null)
			{
				break;
			}
			int height = m_nItemHeightNormal;
			int visibility = View.VISIBLE;
			if (vv.equals(first))
			{
				if (m_nDragPos == m_nFirstDragPos)
				{
					visibility = View.INVISIBLE;
				} else
				{
					height = 1;
				}
			} else if (i == childnum)
			{
				if (m_nDragPos < getCount() - 1)
				{
					height = m_nItemHeightExpanded;
				}
			}
			ViewGroup.LayoutParams params = vv.getLayoutParams();
			params.height = height;
			vv.setLayoutParams(params);
			vv.setVisibility(visibility);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev)
	{
		if (m_GestureDetector != null)
		{
			m_GestureDetector.onTouchEvent(ev);
		}
		
		if ((m_DragListener != null || m_DropListener != null) && m_DragView != null)
		{
			int action = ev.getAction();
			switch (action)
			{
				case MotionEvent.ACTION_UP:
				case MotionEvent.ACTION_CANCEL:
					Rect r = m_TempRect;
					m_DragView.getDrawingRect(r);
					stopDragging();

					if (m_nRemoveMode == S_SLIDE_RIGHT && ev.getX() > r.left + (r.width() * 3 / 4))
					{
						if (m_RemoveListener != null)
						{
							m_RemoveListener.onRemove(m_nFirstDragPos);
						}
						unExpandViews(true);
					} else if (m_nRemoveMode == S_SLIDE_LEFT && ev.getX() < r.left + (r.width() / 4))
					{
						if (m_RemoveListener != null)
						{
							m_RemoveListener.onRemove(m_nFirstDragPos);
						}
						unExpandViews(true);
					} else
					{
						if (m_DropListener != null && m_nDragPos >= 0 && m_nDragPos < getCount())
						{
							m_DropListener.onDrop(m_nFirstDragPos, m_nDragPos);
						}
						unExpandViews(false);
					}
					break;

				case MotionEvent.ACTION_DOWN:
				case MotionEvent.ACTION_MOVE:
					int x = (int) ev.getX();
					int y = (int) ev.getY();
					dragView(x, y);
					int nHeight = 0;
					if(m_DragView != null)
					{
						nHeight = m_DragView.getHeight();
					}
					int itemnum = getItemForPosition(y + nHeight / 2);
					if (itemnum >= 0)
					{
						if (action == MotionEvent.ACTION_DOWN || itemnum != m_nDragPos)
						{
							if (m_DragListener != null)
							{
								m_DragListener.onDrag(m_nDragPos, itemnum);
							}
							m_nDragPos = itemnum;
							doExpansion();
							unExpandViews(false);
						}
						int speed = 0;
						adjustScrollBounds(y);
						if (y > m_nLowerBound)
						{
							speed = y > (m_nHeight + m_nLowerBound) / 2 ? 16 : 4;
						} else if (y < m_nUpperBound)
						{
							speed = y < m_nUpperBound / 2 ? -16 : -4;
						}
						if (speed != 0)
						{
							int ref = pointToPosition(0, m_nHeight / 2);
							if (ref == AdapterView.INVALID_POSITION)
							{
								ref = pointToPosition(0, m_nHeight / 2 + getDividerHeight() + 64);
							}
							View v = getChildAt(ref - getFirstVisiblePosition());
							if (v != null)
							{
								int pos = v.getTop();
								setSelectionFromTop(ref, pos - speed);
							}
						}
					}
					break;
			}
			return true;
		}
		return super.onTouchEvent(ev);
	}

	private void startDragging(Bitmap bm, int y)
	{
		stopDragging();

		m_WindowParams = new WindowManager.LayoutParams();
		m_WindowParams.gravity = Gravity.TOP;
		m_WindowParams.x = 0;
		m_WindowParams.y = y - m_nDragPoint + m_nCoordOffset;

		m_WindowParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
		m_WindowParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
		m_WindowParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
				| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
		m_WindowParams.format = PixelFormat.TRANSLUCENT;
		m_WindowParams.windowAnimations = 0;
		m_WindowParams.gravity = Gravity.TOP | Gravity.LEFT;
		
		ImageView v = new ImageView(getContext());
		//int backGroundColor = getContext().getResources().getColor(R.color.dragndrop_background);
		v.setBackgroundColor(m_nDragndropBackgroundColor);
//		v.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.yicai_info_shape_gray));
		v.setImageBitmap(bm);
		m_DragBitmap = bm;

		m_WindowManager = (WindowManager) getContext().getSystemService("window");
		m_WindowManager.addView(v, m_WindowParams);
		m_DragView = v;
	}

	private void dragView(int x, int y)
	{
		float alpha = 1.0f;//透明度
//		int width = m_DragView.getWidth();

		if (m_nRemoveMode == S_SLIDE_RIGHT)
		{
			//if (x > width / 2) {
			//	alpha = ((float) (width - x)) / (width / 2);
			//}
			m_WindowParams.alpha = alpha;
		} else if (m_nRemoveMode == S_SLIDE_LEFT)
		{
			//if (x < width / 2) {
			//	alpha = ((float) x) / (width / 2);
			//}
			m_WindowParams.alpha = alpha;
		}
		m_WindowParams.y = y - m_nDragPoint + m_nCoordOffset;
		m_WindowManager.updateViewLayout(m_DragView, m_WindowParams);
	}

	private void stopDragging()
	{
		if (m_DragView != null)
		{
			WindowManager wm = (WindowManager) getContext().getSystemService("window");
			wm.removeView(m_DragView);
			m_DragView.setImageDrawable(null);
			m_DragView = null;
		}
		if (m_DragBitmap != null)
		{
			m_DragBitmap.recycle();
			m_DragBitmap = null;
		}
	}

	public void setDragViewId(int id)
	{
		mDragViewId = id;
	}
	public void setOnDragListener(OnDragListener l)
	{
		m_DragListener = l;
	}

	public void setOnDropListener(OnDropListener l)
	{
		m_DropListener = l;
	}

	public void setOnRemoveListener(OnRemoveListener l)
	{
		m_RemoveListener = l;
	}

	public interface OnDragListener
	{
		void onDrag(int from, int to);
	}

	public interface OnDropListener
	{
		void onDrop(int from, int to);
	}

	public interface OnRemoveListener
	{
		void onRemove(int which);
	}
	
	public void OnDestroy()
	{
		stopDragging();
	}
}
