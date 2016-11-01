/*
 * Copyright (C) 2013 47 Degrees, LLC
 * http://47deg.com
 * hello@47deg.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.yulin.applib.widget;

import static com.nineoldandroids.view.ViewHelper.setAlpha;
import static com.nineoldandroids.view.ViewHelper.setTranslationX;
import static com.nineoldandroids.view.ViewPropertyAnimator.animate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Rect;
import android.os.Handler;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewConfigurationCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.view.ViewHelper;
/**
 * ListView subclass that provides the swipe functionality
 */
public class SwipeListView extends ListView {


	/**
     * Used when user want change swipe list mode on some rows
     */
    public final static int SWIPE_MODE_DEFAULT = -1;

    /**
     * Disables all swipes
     */
    public final static int SWIPE_MODE_NONE = 0;

    /**
     * Enables both left and right swipe
     */
    public final static int SWIPE_MODE_BOTH = 1;

    /**
     * Enables right swipe
     */
    public final static int SWIPE_MODE_RIGHT = 2;

    /**
     * Enables left swipe
     */
    public final static int SWIPE_MODE_LEFT = 3;

    /**
     * Binds the swipe gesture to reveal a view behind the row (Drawer style)
     */
    public final static int SWIPE_ACTION_REVEAL = 0;

    /**
     * Dismisses the cell when swiped over
     */
    public final static int SWIPE_ACTION_DISMISS = 1;

    /**
     * Marks the cell as checked when swiped and release
     */
    public final static int SWIPE_ACTION_CHOICE = 2;

    /**
     * No action when swiped
     */
    public final static int SWIPE_ACTION_NONE = 3;

    /**
     * Default ids for front view
     */
    public final static String SWIPE_DEFAULT_FRONT_VIEW = "swipelistview_frontview";

    /**
     * Default id for back view
     */
    public final static String SWIPE_DEFAULT_BACK_VIEW = "swipelistview_backview";

    /**
     * Indicates no movement
     */
    private final static int TOUCH_STATE_REST = 0;

    /**
     * State scrolling x position
     */
    private final static int TOUCH_STATE_SCROLLING_X = 1;

    /**
     * State scrolling y position
     */
    private final static int TOUCH_STATE_SCROLLING_Y = 2;

    private int touchState = TOUCH_STATE_REST;

    private float lastMotionX;
    private float lastMotionY;
    private int touchSlop;

    int swipeFrontView = 0;
    int swipeBackView = 0;

    /**
     * Internal listener for common swipe events
     */
    private OnSwipeListener swipeListViewListener;

    /**
     * Internal touch listener
     */
    private OnSwipeTouchListener touchListener;


    /**
     * If you create a View programmatically you need send back and front identifier
     *
     * @param context        Context
     * @param swipeBackView  Back Identifier
     * @param swipeFrontView Front Identifier
     */
    public SwipeListView(Context context, int swipeBackView, int swipeFrontView) {
        super(context);
        this.swipeBackView = swipeBackView;
        this.swipeFrontView = swipeFrontView;
        init(null);
    }

    /**
     * @see android.widget.ListView#ListView(android.content.Context, android.util.AttributeSet)
     */
    public SwipeListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    /**
     * @see android.widget.ListView#ListView(android.content.Context, android.util.AttributeSet, int)
     */
    public SwipeListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    /**
     * Init ListView
     *
     * @param attrs AttributeSet
     */
    private void init(AttributeSet attrs) {

        int swipeMode = SWIPE_MODE_BOTH;
        boolean swipeOpenOnLongPress = true;
        boolean swipeCloseAllItemsWhenMoveList = true;
        long swipeAnimationTime = 0;
        float swipeOffsetLeft = 0;
        float swipeOffsetRight = 0;
        int swipeDrawableChecked = 0;
        int swipeDrawableUnchecked = 0;

        int swipeActionLeft = SWIPE_ACTION_REVEAL;
        int swipeActionRight = SWIPE_ACTION_REVEAL;

//        if (attrs != null) {
//            TypedArray styled = getContext().obtainStyledAttributes(attrs, R.styleable.SwipeListView);
//            swipeMode = styled.getInt(R.styleable.SwipeListView_swipeMode, SWIPE_MODE_BOTH);
//            swipeActionLeft = styled.getInt(R.styleable.SwipeListView_swipeActionLeft, SWIPE_ACTION_REVEAL);
//            swipeActionRight = styled.getInt(R.styleable.SwipeListView_swipeActionRight, SWIPE_ACTION_REVEAL);
//            swipeOffsetLeft = styled.getDimension(R.styleable.SwipeListView_swipeOffsetLeft, 0);
//            swipeOffsetRight = styled.getDimension(R.styleable.SwipeListView_swipeOffsetRight, 0);
//            swipeOpenOnLongPress = styled.getBoolean(R.styleable.SwipeListView_swipeOpenOnLongPress, true);
//            swipeAnimationTime = styled.getInteger(R.styleable.SwipeListView_swipeAnimationTime, 0);
//            swipeCloseAllItemsWhenMoveList = styled.getBoolean(R.styleable.SwipeListView_swipeCloseAllItemsWhenMoveList, true);
//            swipeDrawableChecked = styled.getResourceId(R.styleable.SwipeListView_swipeDrawableChecked, 0);
//            swipeDrawableUnchecked = styled.getResourceId(R.styleable.SwipeListView_swipeDrawableUnchecked, 0);
//            swipeFrontView = styled.getResourceId(R.styleable.SwipeListView_swipeFrontView, 0);
//            swipeBackView = styled.getResourceId(R.styleable.SwipeListView_swipeBackView, 0);
//        }
        if (swipeFrontView == 0 || swipeBackView == 0) {
            swipeFrontView = getContext().getResources().getIdentifier(SWIPE_DEFAULT_FRONT_VIEW, "id", getContext().getPackageName());
            swipeBackView = getContext().getResources().getIdentifier(SWIPE_DEFAULT_BACK_VIEW, "id", getContext().getPackageName());

//            if (swipeFrontView == 0 || swipeBackView == 0) {
//                throw new RuntimeException(String.format("You forgot the attributes swipeFrontView or swipeBackView. You can add this attributes or use '%s' and '%s' identifiers", SWIPE_DEFAULT_FRONT_VIEW, SWIPE_DEFAULT_BACK_VIEW));
//            }
        }
        
        final ViewConfiguration configuration = ViewConfiguration.get(getContext());
        touchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(configuration);
        touchListener = new OnSwipeTouchListener(this);
        if (swipeAnimationTime > 0) {
            touchListener.setAnimationTime(swipeAnimationTime);
        }
        touchListener.setRightOffset(swipeOffsetRight);
        touchListener.setLeftOffset(swipeOffsetLeft);
        touchListener.setSwipeActionLeft(swipeActionLeft);
        touchListener.setSwipeActionRight(swipeActionRight);
        touchListener.setSwipeMode(swipeMode);
        touchListener.setSwipeClosesAllItemsWhenListMoves(swipeCloseAllItemsWhenMoveList);
        touchListener.setSwipeOpenOnLongPress(swipeOpenOnLongPress);
        touchListener.setSwipeDrawableChecked(swipeDrawableChecked);
        touchListener.setSwipeDrawableUnchecked(swipeDrawableUnchecked);
        setOnTouchListener(touchListener);
        setOnScrollListener(touchListener.makeScrollListener());
    }

    /**
     * Recycle cell. This method should be called from getView in Adapter when use SWIPE_ACTION_CHOICE
     *
     * @param convertView parent view
     * @param position    position in list
     */
    public void recycle(View convertView, int position) {
        touchListener.reloadChoiceStateInView(convertView.findViewById(swipeFrontView), position);
    }

    /**
     * Get if item is selected
     *
     * @param position position in list
     * @return
     */
    public boolean isChecked(int position) {
        return touchListener.isChecked(position);
    }

    /**
     * Get positions selected
     *
     * @return
     */
    public List<Integer> getPositionsSelected() {
        return touchListener.getPositionsSelected();
    }

    /**
     * Count selected
     *
     * @return
     */
    public int getCountSelected() {
        return touchListener.getCountSelected();
    }

    /**
     * Unselected choice state in item
     */
    public void unselectedChoiceStates() {
        touchListener.unselectedChoiceStates();
    }

    /**
     * @see android.widget.ListView#setAdapter(android.widget.ListAdapter)
     */
    @Override
    public void setAdapter(ListAdapter adapter) {
        super.setAdapter(adapter);
        touchListener.resetItems();
        adapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                onListChanged();
                touchListener.resetItems();
            }
        });
    }

    /**
     * Dismiss item
     *
     * @param position Position that you want open
     */
    public void dismiss(int position) {
        int height = touchListener.dismiss(position);
        if (height > 0) {
            touchListener.handlerPendingDismisses(height);
        } else {
            int[] dismissPositions = new int[1];
            dismissPositions[0] = position;
            onDismiss(dismissPositions);
            touchListener.resetPendingDismisses();
        }
    }

    /**
     * Dismiss items selected
     */
    public void dismissSelected() {
        List<Integer> list = touchListener.getPositionsSelected();
        int[] dismissPositions = new int[list.size()];
        int height = 0;
        for (int i = 0; i < list.size(); i++) {
            int position = list.get(i);
            dismissPositions[i] = position;
            int auxHeight = touchListener.dismiss(position);
            if (auxHeight > 0) {
                height = auxHeight;
            }
        }
        if (height > 0) {
            touchListener.handlerPendingDismisses(height);
        } else {
            onDismiss(dismissPositions);
            touchListener.resetPendingDismisses();
        }
        touchListener.returnOldActions();
    }

    /**
     * Open ListView's item
     *
     * @param position Position that you want open
     */
    public void openAnimate(int position) {
        touchListener.openAnimate(position);
    }

    /**
     * Close ListView's item
     *
     * @param position Position that you want open
     */
    public void closeAnimate(int position) {
        touchListener.closeAnimate(position);
    }

    /**
     * Notifies onDismiss
     *
     * @param reverseSortedPositions All dismissed positions
     */
    protected void onDismiss(int[] reverseSortedPositions) {
        if (swipeListViewListener != null) {
            swipeListViewListener.onDismiss(reverseSortedPositions);
        }
    }

    /**
     * Start open item
     *
     * @param position list item
     * @param action   current action
     * @param right    to right
     */
    protected void onStartOpen(int position, int action, boolean right) {
        if (swipeListViewListener != null && position != ListView.INVALID_POSITION) {
            swipeListViewListener.onStartOpen(position, action, right);
        }
    }

    /**
     * Start close item
     *
     * @param position list item
     * @param right
     */
    protected void onStartClose(int position, boolean right) {
        if (swipeListViewListener != null && position != ListView.INVALID_POSITION) {
            swipeListViewListener.onStartClose(position, right);
        }
    }

    /**
     * Notifies onClickFrontView
     *
     * @param position item clicked
     */
    protected void onClickFrontView(int position) {
        if (swipeListViewListener != null && position != ListView.INVALID_POSITION) {
            swipeListViewListener.onClickFrontView(position);
        }
    }

    /**
     * Notifies onClickBackView
     *
     * @param position back item clicked
     */
    protected void onClickBackView(int position) {
        if (swipeListViewListener != null && position != ListView.INVALID_POSITION) {
            swipeListViewListener.onClickBackView(position);
        }
    }

    /**
     * Notifies onOpened
     *
     * @param position Item opened
     * @param toRight  If should be opened toward the right
     */
    protected void onOpened(int position, boolean toRight) {
        if (swipeListViewListener != null && position != ListView.INVALID_POSITION) {
            swipeListViewListener.onOpened(position, toRight);
        }
    }

    /**
     * Notifies onClosed
     *
     * @param position  Item closed
     * @param fromRight If open from right
     */
    protected void onClosed(int position, boolean fromRight) {
        if (swipeListViewListener != null && position != ListView.INVALID_POSITION) {
            swipeListViewListener.onClosed(position, fromRight);
        }
    }

    /**
     * Notifies onChoiceChanged
     *
     * @param position position that choice
     * @param selected if item is selected or not
     */
    protected void onChoiceChanged(int position, boolean selected) {
        if (swipeListViewListener != null && position != ListView.INVALID_POSITION) {
            swipeListViewListener.onChoiceChanged(position, selected);
        }
    }

    /**
     * User start choice items
     */
    protected void onChoiceStarted() {
        if (swipeListViewListener != null) {
            swipeListViewListener.onChoiceStarted();
        }
    }

    /**
     * User end choice items
     */
    protected void onChoiceEnded() {
        if (swipeListViewListener != null) {
            swipeListViewListener.onChoiceEnded();
        }
    }

    /**
     * User is in first item of list
     */
    protected void onFirstListItem() {
        if (swipeListViewListener != null) {
            swipeListViewListener.onFirstListItem();
        }
    }

    /**
     * User is in last item of list
     */
    protected void onLastListItem() {
        if (swipeListViewListener != null) {
            swipeListViewListener.onLastListItem();
        }
    }

    /**
     * Notifies onListChanged
     */
    protected void onListChanged() {
        if (swipeListViewListener != null) {
            swipeListViewListener.onListChanged();
        }
    }

    /**
     * Notifies onMove
     *
     * @param position Item moving
     * @param x        Current position
     */
    protected void onMove(int position, float x) {
        if (swipeListViewListener != null && position != ListView.INVALID_POSITION) {
            swipeListViewListener.onMove(position, x);
        }
    }

    protected int changeSwipeMode(int position) {
        if (swipeListViewListener != null && position != ListView.INVALID_POSITION) {
            return swipeListViewListener.onChangeSwipeMode(position);
        }
        return SWIPE_MODE_DEFAULT;
    }

    /**
     * Sets the Listener
     *
     * @param swipeListViewListener Listener
     */
    public void setOnSwipeListener(OnSwipeListener swipeListViewListener) {
        this.swipeListViewListener = swipeListViewListener;
    }

    /**
     * Resets scrolling
     */
    public void resetScrolling() {
        touchState = TOUCH_STATE_REST;
    }

    /**
     * Set offset on right
     *
     * @param offsetRight Offset
     */
    public void setOffsetRight(float offsetRight) {
        touchListener.setRightOffset(offsetRight);
    }

    /**
     * Set offset on left
     *
     * @param offsetLeft Offset
     */
    public void setOffsetLeft(float offsetLeft) {
        touchListener.setLeftOffset(offsetLeft);
    }

    /**
     * Set if all items opened will be closed when the user moves the ListView
     *
     * @param swipeCloseAllItemsWhenMoveList
     */
    public void setSwipeCloseAllItemsWhenMoveList(boolean swipeCloseAllItemsWhenMoveList) {
        touchListener.setSwipeClosesAllItemsWhenListMoves(swipeCloseAllItemsWhenMoveList);
    }

    /**
     * Sets if the user can open an item with long pressing on cell
     *
     * @param swipeOpenOnLongPress
     */
    public void setSwipeOpenOnLongPress(boolean swipeOpenOnLongPress) {
        touchListener.setSwipeOpenOnLongPress(swipeOpenOnLongPress);
    }

    /**
     * Set swipe mode
     *
     * @param swipeMode
     */
    public void setSwipeMode(int swipeMode) {
        touchListener.setSwipeMode(swipeMode);
    }

    /**
     * Return action on left
     *
     * @return Action
     */
    public int getSwipeActionLeft() {
        return touchListener.getSwipeActionLeft();
    }

    /**
     * Set action on left
     *
     * @param swipeActionLeft Action
     */
    public void setSwipeActionLeft(int swipeActionLeft) {
        touchListener.setSwipeActionLeft(swipeActionLeft);
    }

    /**
     * Return action on right
     *
     * @return Action
     */
    public int getSwipeActionRight() {
        return touchListener.getSwipeActionRight();
    }

    /**
     * Set action on right
     *
     * @param swipeActionRight Action
     */
    public void setSwipeActionRight(int swipeActionRight) {
        touchListener.setSwipeActionRight(swipeActionRight);
    }

    /**
     * Sets animation time when user drops cell
     *
     * @param animationTime milliseconds
     */
    public void setAnimationTime(long animationTime) {
        touchListener.setAnimationTime(animationTime);
    }

    /**
     * @see android.widget.ListView#onInterceptTouchEvent(android.view.MotionEvent)
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = MotionEventCompat.getActionMasked(ev);
        final float x = ev.getX();
        final float y = ev.getY();

        if (isEnabled() && touchListener.isSwipeEnabled()) {

            if (touchState == TOUCH_STATE_SCROLLING_X) {
                return touchListener.onTouch(this, ev);
            }

            switch (action) {
                case MotionEvent.ACTION_MOVE:
                    checkInMoving(x, y);
                    return touchState == TOUCH_STATE_SCROLLING_Y;
                case MotionEvent.ACTION_DOWN:
                    touchListener.onTouch(this, ev);
                    touchState = TOUCH_STATE_REST;
                    lastMotionX = x;
                    lastMotionY = y;
                    return false;
                case MotionEvent.ACTION_CANCEL:
                    touchState = TOUCH_STATE_REST;
                    break;
                case MotionEvent.ACTION_UP:
                    touchListener.onTouch(this, ev);
                    return touchState == TOUCH_STATE_SCROLLING_Y;
                default:
                    break;
            }
        }

        return super.onInterceptTouchEvent(ev);
    }

    /**
     * Check if the user is moving the cell
     *
     * @param x Position X
     * @param y Position Y
     */
    private void checkInMoving(float x, float y) {
        final int xDiff = (int) Math.abs(x - lastMotionX);
        final int yDiff = (int) Math.abs(y - lastMotionY);

        final int touchSlop = this.touchSlop;
        boolean xMoved = xDiff > touchSlop;
        boolean yMoved = yDiff > touchSlop;

        if (xMoved) {
            touchState = TOUCH_STATE_SCROLLING_X;
            lastMotionX = x;
            lastMotionY = y;
        }

        if (yMoved) {
            touchState = TOUCH_STATE_SCROLLING_Y;
            lastMotionX = x;
            lastMotionY = y;
        }
    }

    /**
     * Close all opened items
     */
    public void closeOpenedItems() {
        touchListener.closeOpenedItems();
    }

    public static interface OnSwipeListener {

        /**
         * Called when open animation finishes
         * @param position list item
         * @param toRight Open to right
         */
        void onOpened(int position, boolean toRight);

        /**
         * Called when close animation finishes
         * @param position list item
         * @param fromRight Close from right
         */
        void onClosed(int position, boolean fromRight);

        /**
         * Called when the list changed
         */
        void onListChanged();

        /**
         * Called when user is moving an item
         * @param position list item
         * @param x Current position X
         */
        void onMove(int position, float x);

        /**
         * Start open item
         * @param position list item
         * @param action current action
         * @param right to right
         */
        void onStartOpen(int position, int action, boolean right);

        /**
         * Start close item
         * @param position list item
         * @param right
         */
        void onStartClose(int position, boolean right);

        /**
         * Called when user clicks on the front view
         * @param position list item
         */
        void onClickFrontView(int position);

        /**
         * Called when user clicks on the back view
         * @param position list item
         */
        void onClickBackView(int position);

        /**
         * Called when user dismisses items
         * @param reverseSortedPositions Items dismissed
         */
        void onDismiss(int[] reverseSortedPositions);

        /**
         * Used when user want to change swipe list mode on some rows. Return SWIPE_MODE_DEFAULT
         * if you don't want to change swipe list mode
         * @param position position that you want to change
         * @return type
         */
        int onChangeSwipeMode(int position);

        /**
         * Called when user choice item
         * @param position position that choice
         * @param selected if item is selected or not
         */
        void onChoiceChanged(int position, boolean selected);

        /**
         * User start choice items
         */
        void onChoiceStarted();

        /**
         * User end choice items
         */
        void onChoiceEnded();

        /**
         * User is in first item of list
         */
        void onFirstListItem();

        /**
         * User is in last item of list
         */
        void onLastListItem();

    }
    public void setSwipeFrontView(int viewId)
    {
    	swipeFrontView = viewId;
    	touchListener.setSwipeFrontView(viewId);
    }
    
    public void setSwipeBackView(int viewId)
    {
    	swipeBackView = viewId;
    	touchListener.setSwipeBackView(viewId);
    }
}
class OnSwipeTouchListener implements View.OnTouchListener {
	
	private static final int DISPLACE_CHOICE = 80;
	
	private int swipeMode = SwipeListView.SWIPE_MODE_BOTH;
	private boolean swipeOpenOnLongPress = true;
	private boolean swipeClosesAllItemsWhenListMoves = true;
	
	private int swipeFrontView = 0;
	private int swipeBackView = 0;
	
	private Rect rect = new Rect();
	
	// Cached ViewConfiguration and system-wide constant values
	private int slop;
	private int minFlingVelocity;
	private int maxFlingVelocity;
	private long configShortAnimationTime;
	private long animationTime;
	
	private float leftOffset = 0;
	private float rightOffset = 0;
	
	private int swipeDrawableChecked = 0;
	private int swipeDrawableUnchecked = 0;
	
	// Fixed properties
	private SwipeListView swipeListView;
	private int viewWidth = 1; // 1 and not 0 to prevent dividing by zero
	
	private List<PendingDismissData> pendingDismisses = new ArrayList<PendingDismissData>();
	private int dismissAnimationRefCount = 0;
	
	private float downX;
	private boolean swiping;
	private boolean swipingRight;
	private VelocityTracker velocityTracker;
	private int downPosition;
	private View parentView;
	private View frontView;
	private View backView;
	private boolean paused;
	
	private int swipeCurrentAction = SwipeListView.SWIPE_ACTION_NONE;
	
	private int swipeActionLeft = SwipeListView.SWIPE_ACTION_REVEAL;
	private int swipeActionRight = SwipeListView.SWIPE_ACTION_REVEAL;
	
	private List<Boolean> opened = new ArrayList<Boolean>();
	private List<Boolean> openedRight = new ArrayList<Boolean>();
	private boolean listViewMoving;
	private List<Boolean> checked = new ArrayList<Boolean>();
	private int oldSwipeActionRight;
	private int oldSwipeActionLeft;
	
	/**
	 * Constructor
	 *
	 * @param swipeListView  SwipeListView
	 * @param swipeFrontView front view Identifier
	 * @param swipeBackView  back view Identifier
	 */
    public OnSwipeTouchListener(SwipeListView swipeListView) {
        ViewConfiguration vc = ViewConfiguration.get(swipeListView.getContext());
        slop = vc.getScaledTouchSlop();
        minFlingVelocity = vc.getScaledMinimumFlingVelocity();
        maxFlingVelocity = vc.getScaledMaximumFlingVelocity();
        configShortAnimationTime = swipeListView.getContext().getResources().getInteger(android.R.integer.config_shortAnimTime);
        animationTime = configShortAnimationTime;
        this.swipeListView = swipeListView;
    }
    public void setSwipeFrontView(int viewId)
    {
    	swipeFrontView = viewId;
    }
    public void setSwipeBackView(int viewId)
    {
    	swipeBackView = viewId;
    }
	/**
	 * Sets current item's parent view
	 *
	 * @param parentView Parent view
	 */
	private void setParentView(View parentView) {
		this.parentView = parentView;
	}
	
	/**
	 * Sets current item's front view
	 *
	 * @param frontView Front view
	 */
	private void setFrontView(View frontView) {
		this.frontView = frontView;
		frontView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				swipeListView.onClickFrontView(downPosition);
			}
		});
		if (swipeOpenOnLongPress) {
			frontView.setOnLongClickListener(new View.OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {
					openAnimate(downPosition);
					return false;
				}
			});
		}
	}
	
	/**
	 * Set current item's back view
	 *
	 * @param backView
	 */
	private void setBackView(View backView) {
		this.backView = backView;
		backView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				swipeListView.onClickBackView(downPosition);
			}
		});
	}
	
	/**
	 * @return true if the list is in motion
	 */
	public boolean isListViewMoving() {
		return listViewMoving;
	}
	
	/**
	 * Sets animation time when the user drops the cell
	 *
	 * @param animationTime milliseconds
	 */
	public void setAnimationTime(long animationTime) {
		if (animationTime > 0) {
			this.animationTime = animationTime;
		} else {
			this.animationTime = configShortAnimationTime;
		}
	}
	
	/**
	 * Sets the right offset
	 *
	 * @param rightOffset Offset
	 */
	public void setRightOffset(float rightOffset) {
		this.rightOffset = rightOffset;
	}
	
	/**
	 * Set the left offset
	 *
	 * @param leftOffset Offset
	 */
	public void setLeftOffset(float leftOffset) {
		this.leftOffset = leftOffset;
	}
	
	/**
	 * Set if all item opened will be close when the user move ListView
	 *
	 * @param swipeClosesAllItemsWhenListMoves
	 */
	public void setSwipeClosesAllItemsWhenListMoves(boolean swipeClosesAllItemsWhenListMoves) {
		this.swipeClosesAllItemsWhenListMoves = swipeClosesAllItemsWhenListMoves;
	}
	
	/**
	 * Set if the user can open an item with long press on cell
	 *
	 * @param swipeOpenOnLongPress
	 */
	public void setSwipeOpenOnLongPress(boolean swipeOpenOnLongPress) {
		this.swipeOpenOnLongPress = swipeOpenOnLongPress;
	}
	
	/**
	 * Sets the swipe mode
	 *
	 * @param swipeMode
	 */
	public void setSwipeMode(int swipeMode) {
		this.swipeMode = swipeMode;
	}
	
	/**
	 * Check is swiping is enabled
	 *
	 * @return
	 */
	protected boolean isSwipeEnabled() {
		return swipeMode != SwipeListView.SWIPE_MODE_NONE;
	}
	
	/**
	 * Return action on left
	 *
	 * @return Action
	 */
	public int getSwipeActionLeft() {
		return swipeActionLeft;
	}
	
	/**
	 * Set action on left
	 *
	 * @param swipeActionLeft Action
	 */
	public void setSwipeActionLeft(int swipeActionLeft) {
		this.swipeActionLeft = swipeActionLeft;
	}
	
	/**
	 * Return action on right
	 *
	 * @return Action
	 */
	public int getSwipeActionRight() {
		return swipeActionRight;
	}
	
	/**
	 * Set action on right
	 *
	 * @param swipeActionRight Action
	 */
	public void setSwipeActionRight(int swipeActionRight) {
		this.swipeActionRight = swipeActionRight;
	}
	
	/**
	 * Set drawable checked (only SWIPE_ACTION_CHOICE)
	 *
	 * @param swipeDrawableChecked drawable
	 */
	protected void setSwipeDrawableChecked(int swipeDrawableChecked) {
		this.swipeDrawableChecked = swipeDrawableChecked;
	}
	
	/**
	 * Set drawable unchecked (only SWIPE_ACTION_CHOICE)
	 *
	 * @param swipeDrawableUnchecked drawable
	 */
	protected void setSwipeDrawableUnchecked(int swipeDrawableUnchecked) {
		this.swipeDrawableUnchecked = swipeDrawableUnchecked;
	}
	
	/**
	 * Adds new items when adapter is modified
	 */
	public void resetItems() {
		if (swipeListView.getAdapter() != null) {
			int count = swipeListView.getAdapter().getCount();
			for (int i = opened.size(); i <= count; i++) {
				opened.add(false);
				openedRight.add(false);
				checked.add(false);
			}
		}
	}
	
	/**
	 * Open item
	 *
	 * @param position Position of list
	 */
	protected void openAnimate(int position) {
		openAnimate(swipeListView.getChildAt(position - swipeListView.getFirstVisiblePosition()).findViewById(swipeFrontView), position);
	}
	
	/**
	 * Close item
	 *
	 * @param position Position of list
	 */
	protected void closeAnimate(int position) {
		closeAnimate(swipeListView.getChildAt(position - swipeListView.getFirstVisiblePosition()).findViewById(swipeFrontView), position);
	}
	
	/**
	 * Swap choice state in item
	 *
	 * @param position position of list
	 */
	private void swapChoiceState(int position) {
		int lastCount = getCountSelected();
		boolean lastChecked = checked.get(position);
		checked.set(position, !lastChecked);
		int count = lastChecked ? lastCount - 1 : lastCount + 1;
		if (lastCount == 0 && count == 1) {
			swipeListView.onChoiceStarted();
			closeOpenedItems();
			setActionsTo(SwipeListView.SWIPE_ACTION_CHOICE);
		}
		if (lastCount == 1 && count == 0) {
			swipeListView.onChoiceEnded();
			returnOldActions();
		}
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//                swipeListView.setItemChecked(position, !lastChecked);
//            }
		swipeListView.onChoiceChanged(position, !lastChecked);
		reloadChoiceStateInView(frontView, position);
	}
	
	/**
	 * Unselected choice state in item
	 */
	protected void unselectedChoiceStates() {
		int start = swipeListView.getFirstVisiblePosition();
		int end = swipeListView.getLastVisiblePosition();
		for (int i = 0; i < checked.size(); i++) {
			if (checked.get(i) && i >= start && i <= end) {
				reloadChoiceStateInView(swipeListView.getChildAt(i - start).findViewById(swipeFrontView), i);
			}
			checked.set(i, false);
		}
		swipeListView.onChoiceEnded();
		returnOldActions();
	}
	
	/**
	 * Unselected choice state in item
	 */
	protected int dismiss(int position) {
		int start = swipeListView.getFirstVisiblePosition();
		int end = swipeListView.getLastVisiblePosition();
		View view = swipeListView.getChildAt(position - start);
		++dismissAnimationRefCount;
		if (position >= start && position <= end) {
			performDismiss(view, position, false);
			return view.getHeight();
		} else {
			pendingDismisses.add(new PendingDismissData(position, null));
			return 0;
		}
	}
	
	/**
	 * Draw cell for display if item is selected or not
	 *
	 * @param frontView view to draw
	 * @param position  position in list
	 */
	protected void reloadChoiceStateInView(View frontView, int position) {
		if (isChecked(position)) {
			if (swipeDrawableChecked > 0) frontView.setBackgroundResource(swipeDrawableChecked);
		} else {
			if (swipeDrawableUnchecked > 0) frontView.setBackgroundResource(swipeDrawableUnchecked);
		}
	}
	
	/**
	 * Get if item is selected
	 *
	 * @param position position in list
	 * @return
	 */
	protected boolean isChecked(int position) {
		return position < checked.size() && checked.get(position);
	}
	
	/**
	 * Count selected
	 *
	 * @return
	 */
	protected int getCountSelected() {
		int count = 0;
		for (int i = 0; i < checked.size(); i++) {
			if (checked.get(i)) {
				count++;
			}
		}
		Log.d("SwipeListView", "selected: " + count);
		return count;
	}
	
	/**
	 * Get positions selected
	 *
	 * @return
	 */
	protected List<Integer> getPositionsSelected() {
		List<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i < checked.size(); i++) {
			if (checked.get(i)) {
				list.add(i);
			}
		}
		return list;
	}
	
	/**
	 * Open item
	 *
	 * @param view     affected view
	 * @param position Position of list
	 */
	private void openAnimate(View view, int position) {
		if (!opened.get(position)) {
			generateRevealAnimate(view, true, false, position);
		}
	}
	
	/**
	 * Close item
	 *
	 * @param view     affected view
	 * @param position Position of list
	 */
	private void closeAnimate(View view, int position) {
		if (opened.get(position)) {
			generateRevealAnimate(view, true, false, position);
		}
	}
	
	/**
	 * Create animation
	 *
	 * @param view      affected view
	 * @param swap      If state should change. If "false" returns to the original position
	 * @param swapRight If swap is true, this parameter tells if move is to the right or left
	 * @param position  Position of list
	 */
	private void generateAnimate(final View view, final boolean swap, final boolean swapRight, final int position) {
		Log.d("SwipeListView", "swap: " + swap + " - swapRight: " + swapRight + " - position: " + position);
		if (swipeCurrentAction == SwipeListView.SWIPE_ACTION_REVEAL) {
			generateRevealAnimate(view, swap, swapRight, position);
		}
		if (swipeCurrentAction == SwipeListView.SWIPE_ACTION_DISMISS) {
			generateDismissAnimate(parentView, swap, swapRight, position);
		}
		if (swipeCurrentAction == SwipeListView.SWIPE_ACTION_CHOICE) {
			generateChoiceAnimate(view, position);
		}
	}
	
	/**
	 * Create choice animation
	 *
	 * @param view     affected view
	 * @param position list position
	 */
	private void generateChoiceAnimate(final View view, final int position) {
		animate(view)
		.translationX(0)
		.setDuration(animationTime)
		.setListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				swipeListView.resetScrolling();
				resetCell();
			}
		});
	}
	
	/**
	 * Create dismiss animation
	 *
	 * @param view      affected view
	 * @param swap      If will change state. If is "false" returns to the original position
	 * @param swapRight If swap is true, this parameter tells if move is to the right or left
	 * @param position  Position of list
	 */
	private void generateDismissAnimate(final View view, final boolean swap, final boolean swapRight, final int position) {
		int moveTo = 0;
		if (opened.get(position)) {
			if (!swap) {
				moveTo = openedRight.get(position) ? (int) (viewWidth - rightOffset) : (int) (-viewWidth + leftOffset);
			}
		} else {
			if (swap) {
				moveTo = swapRight ? (int) (viewWidth - rightOffset) : (int) (-viewWidth + leftOffset);
			}
		}
		
		int alpha = 1;
		if (swap) {
			++dismissAnimationRefCount;
			alpha = 0;
		}
		
		animate(view)
		.translationX(moveTo)
		.alpha(alpha)
		.setDuration(animationTime)
		.setListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				if (swap) {
					closeOpenedItems();
					performDismiss(view, position, true);
				}
				resetCell();
			}
		});
		
	}
	
	/**
	 * Create reveal animation
	 *
	 * @param view      affected view
	 * @param swap      If will change state. If "false" returns to the original position
	 * @param swapRight If swap is true, this parameter tells if movement is toward right or left
	 * @param position  list position
	 */
	private void generateRevealAnimate(final View view, final boolean swap, final boolean swapRight, final int position) {
		int moveTo = 0;
		if (opened.get(position)) {
			if (!swap) {
				moveTo = openedRight.get(position) ? (int) (viewWidth - rightOffset) : (int) (-viewWidth + leftOffset);
			}
		} else {
			if (swap) {
				moveTo = swapRight ? (int) (viewWidth - rightOffset) : (int) (-viewWidth + leftOffset);
			}
		}
		
		animate(view)
		.translationX(moveTo)
		.setDuration(animationTime)
		.setListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				swipeListView.resetScrolling();
				if (swap) {
					boolean aux = !opened.get(position);
					opened.set(position, aux);
					if (aux) {
						swipeListView.onOpened(position, swapRight);
						openedRight.set(position, swapRight);
					} else {
						swipeListView.onClosed(position, openedRight.get(position));
					}
				}
				resetCell();
			}
		});
	}
	
	private void resetCell() {
		if (downPosition != ListView.INVALID_POSITION) {
			if (swipeCurrentAction == SwipeListView.SWIPE_ACTION_CHOICE) {
				backView.setVisibility(View.VISIBLE);
			}
			frontView.setClickable(opened.get(downPosition));
			frontView.setLongClickable(opened.get(downPosition));
			frontView = null;
			backView = null;
			downPosition = ListView.INVALID_POSITION;
		}
	}
	
	/**
	 * Set enabled
	 *
	 * @param enabled
	 */
	 public void setEnabled(boolean enabled) {
		paused = !enabled;
	}
	
	/**
	 * Return ScrollListener for ListView
	 *
	 * @return OnScrollListener
	 */
	 public AbsListView.OnScrollListener makeScrollListener() {
		 return new AbsListView.OnScrollListener() {
			 
			 private boolean isFirstItem = false;
			 private boolean isLastItem = false;
			 
			 @Override
			 public void onScrollStateChanged(AbsListView absListView, int scrollState) {
				 setEnabled(scrollState != AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL);
				 if (swipeClosesAllItemsWhenListMoves && scrollState == SCROLL_STATE_TOUCH_SCROLL) {
					 closeOpenedItems();
				 }
				 if (scrollState == SCROLL_STATE_TOUCH_SCROLL) {
					 listViewMoving = true;
					 setEnabled(false);
				 }
				 if (scrollState != AbsListView.OnScrollListener.SCROLL_STATE_FLING && scrollState != SCROLL_STATE_TOUCH_SCROLL) {
					 listViewMoving = false;
					 downPosition = ListView.INVALID_POSITION;
					 swipeListView.resetScrolling();
					 new Handler().postDelayed(new Runnable() {
						 public void run() {
							 setEnabled(true);
						 }
					 }, 500);
				 }
			 }
			 
			 @Override
			 public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				 if (isFirstItem) {
					 boolean onSecondItemList = firstVisibleItem == 1;
					 if (onSecondItemList) {
						 isFirstItem = false;
					 }
				 } else {
					 boolean onFirstItemList = firstVisibleItem == 0;
					 if (onFirstItemList) {
						 isFirstItem = true;
						 swipeListView.onFirstListItem();
					 }
				 }
				 if (isLastItem) {
					 boolean onBeforeLastItemList = firstVisibleItem + visibleItemCount == totalItemCount - 1;
					 if (onBeforeLastItemList) {
						 isLastItem = false;
					 }
				 } else {
					 boolean onLastItemList = firstVisibleItem + visibleItemCount >= totalItemCount;
					 if (onLastItemList) {
						 isLastItem = true;
						 swipeListView.onLastListItem();
					 }
				 }
			 }
		 };
	 }
	 
	 /**
	  * Close all opened items
	  */
	 void closeOpenedItems() {
		 if (opened != null) {
			 int start = swipeListView.getFirstVisiblePosition();
			 int end = swipeListView.getLastVisiblePosition();
			 for (int i = start; i <= end; i++) {
				 if (opened.get(i)) {
					 closeAnimate(swipeListView.getChildAt(i - start).findViewById(swipeFrontView), i);
				 }
			 }
		 }
		 
	 }
	 
	 /**
	  * @see View.OnTouchListener#onTouch(android.view.View, android.view.MotionEvent)
	  */
	 @Override
	 public boolean onTouch(View view, MotionEvent motionEvent) {
		 if (!isSwipeEnabled()) {
			 return false;
		 }
		 
		 if (viewWidth < 2) {
			 viewWidth = swipeListView.getWidth();
		 }
		 
		 switch (MotionEventCompat.getActionMasked(motionEvent)) {
		 case MotionEvent.ACTION_DOWN: {
			 if (paused && downPosition != ListView.INVALID_POSITION) {
				 return false;
			 }
			 swipeCurrentAction = SwipeListView.SWIPE_ACTION_NONE;
			 
			 int childCount = swipeListView.getChildCount();
			 int[] listViewCoords = new int[2];
			 swipeListView.getLocationOnScreen(listViewCoords);
			 int x = (int) motionEvent.getRawX() - listViewCoords[0];
			 int y = (int) motionEvent.getRawY() - listViewCoords[1];
			 View child;
			 for (int i = 0; i < childCount; i++) {
				 child = swipeListView.getChildAt(i);
				 child.getHitRect(rect);
				 
				 int childPosition = swipeListView.getPositionForView(child);
				 
				 // dont allow swiping if this is on the header or footer or IGNORE_ITEM_VIEW_TYPE or enabled is false on the adapter
				 boolean allowSwipe = swipeListView.getAdapter().isEnabled(childPosition) && swipeListView.getAdapter().getItemViewType(childPosition) >= 0;
				 
				 if (allowSwipe && rect.contains(x, y)) {
					 setParentView(child);
					 setFrontView(child.findViewById(swipeFrontView));
					 
					 downX = motionEvent.getRawX();
					 downPosition = childPosition;
					 
					 frontView.setClickable(!opened.get(downPosition));
					 frontView.setLongClickable(!opened.get(downPosition));
					 
					 velocityTracker = VelocityTracker.obtain();
					 velocityTracker.addMovement(motionEvent);
					 if (swipeBackView > 0) {
						 setBackView(child.findViewById(swipeBackView));
					 }
					 break;
				 }
			 }
			 view.onTouchEvent(motionEvent);
			 return true;
		 }
		 
		 case MotionEvent.ACTION_UP: {
			 if (velocityTracker == null || !swiping || downPosition == ListView.INVALID_POSITION) {
				 break;
			 }
			 
			 float deltaX = motionEvent.getRawX() - downX;
			 velocityTracker.addMovement(motionEvent);
			 velocityTracker.computeCurrentVelocity(1000);
			 float velocityX = Math.abs(velocityTracker.getXVelocity());
			 if (!opened.get(downPosition)) {
				 if (swipeMode == SwipeListView.SWIPE_MODE_LEFT && velocityTracker.getXVelocity() > 0) {
					 velocityX = 0;
				 }
				 if (swipeMode == SwipeListView.SWIPE_MODE_RIGHT && velocityTracker.getXVelocity() < 0) {
					 velocityX = 0;
				 }
			 }
			 float velocityY = Math.abs(velocityTracker.getYVelocity());
			 boolean swap = false;
			 boolean swapRight = false;
			 if (minFlingVelocity <= velocityX && velocityX <= maxFlingVelocity && velocityY * 2 < velocityX) {
				 swapRight = velocityTracker.getXVelocity() > 0;
				 Log.d("SwipeListView", "swapRight: " + swapRight + " - swipingRight: " + swipingRight);
				 if (swapRight != swipingRight && swipeActionLeft != swipeActionRight) {
					 swap = false;
				 } else if (opened.get(downPosition) && openedRight.get(downPosition) && swapRight) {
					 swap = false;
				 } else if (opened.get(downPosition) && !openedRight.get(downPosition) && !swapRight) {
					 swap = false;
				 } else {
					 swap = true;
				 }
			 } else if (Math.abs(deltaX) > viewWidth / 2) {
				 swap = true;
				 swapRight = deltaX > 0;
			 }
			 generateAnimate(frontView, swap, swapRight, downPosition);
			 if (swipeCurrentAction == SwipeListView.SWIPE_ACTION_CHOICE) {
				 swapChoiceState(downPosition);
			 }
			 
			 velocityTracker.recycle();
			 velocityTracker = null;
			 downX = 0;
			 // change clickable front view
//                    if (swap) {
//                        frontView.setClickable(opened.get(downPosition));
//                        frontView.setLongClickable(opened.get(downPosition));
//                    }
			 swiping = false;
			 break;
		 }
		 
		 case MotionEvent.ACTION_MOVE: {
			 if (velocityTracker == null || paused || downPosition == ListView.INVALID_POSITION) {
				 break;
			 }
			 
			 velocityTracker.addMovement(motionEvent);
			 velocityTracker.computeCurrentVelocity(1000);
			 float velocityX = Math.abs(velocityTracker.getXVelocity());
			 float velocityY = Math.abs(velocityTracker.getYVelocity());
			 
			 float deltaX = motionEvent.getRawX() - downX;
			 float deltaMode = Math.abs(deltaX);
			 
			 int swipeMode = this.swipeMode;
			 int changeSwipeMode = swipeListView.changeSwipeMode(downPosition);
			 if (changeSwipeMode >= 0) {
				 swipeMode = changeSwipeMode;
			 }
			 
			 if (swipeMode == SwipeListView.SWIPE_MODE_NONE) {
				 deltaMode = 0;
			 } else if (swipeMode != SwipeListView.SWIPE_MODE_BOTH) {
				 if (opened.get(downPosition)) {
					 if (swipeMode == SwipeListView.SWIPE_MODE_LEFT && deltaX < 0) {
						 deltaMode = 0;
					 } else if (swipeMode == SwipeListView.SWIPE_MODE_RIGHT && deltaX > 0) {
						 deltaMode = 0;
					 }
				 } else {
					 if (swipeMode == SwipeListView.SWIPE_MODE_LEFT && deltaX > 0) {
						 deltaMode = 0;
					 } else if (swipeMode == SwipeListView.SWIPE_MODE_RIGHT && deltaX < 0) {
						 deltaMode = 0;
					 }
				 }
			 }
			 if (deltaMode > slop && swipeCurrentAction == SwipeListView.SWIPE_ACTION_NONE && velocityY < velocityX) {
				 swiping = true;
				 swipingRight = (deltaX > 0);
				 Log.d("SwipeListView", "deltaX: " + deltaX + " - swipingRight: " + swipingRight);
				 if (opened.get(downPosition)) {
					 swipeListView.onStartClose(downPosition, swipingRight);
					 swipeCurrentAction = SwipeListView.SWIPE_ACTION_REVEAL;
				 } else {
					 if (swipingRight && swipeActionRight == SwipeListView.SWIPE_ACTION_DISMISS) {
						 swipeCurrentAction = SwipeListView.SWIPE_ACTION_DISMISS;
					 } else if (!swipingRight && swipeActionLeft == SwipeListView.SWIPE_ACTION_DISMISS) {
						 swipeCurrentAction = SwipeListView.SWIPE_ACTION_DISMISS;
					 } else if (swipingRight && swipeActionRight == SwipeListView.SWIPE_ACTION_CHOICE) {
						 swipeCurrentAction = SwipeListView.SWIPE_ACTION_CHOICE;
					 } else if (!swipingRight && swipeActionLeft == SwipeListView.SWIPE_ACTION_CHOICE) {
						 swipeCurrentAction = SwipeListView.SWIPE_ACTION_CHOICE;
					 } else {
						 swipeCurrentAction = SwipeListView.SWIPE_ACTION_REVEAL;
					 }
					 swipeListView.onStartOpen(downPosition, swipeCurrentAction, swipingRight);
				 }
				 swipeListView.requestDisallowInterceptTouchEvent(true);
				 MotionEvent cancelEvent = MotionEvent.obtain(motionEvent);
				 cancelEvent.setAction(MotionEvent.ACTION_CANCEL |
						 (MotionEventCompat.getActionIndex(motionEvent) << MotionEventCompat.ACTION_POINTER_INDEX_SHIFT));
				 swipeListView.onTouchEvent(cancelEvent);
				 if (swipeCurrentAction == SwipeListView.SWIPE_ACTION_CHOICE) {
					 backView.setVisibility(View.GONE);
				 }
			 }
			 
			 if (swiping && downPosition != ListView.INVALID_POSITION) {
				 if (opened.get(downPosition)) {
					 deltaX += openedRight.get(downPosition) ? viewWidth - rightOffset : -viewWidth + leftOffset;
				 }
				 move(deltaX);
				 return true;
			 }
			 break;
		 }
		 }
		 return false;
	 }
	 
	 private void setActionsTo(int action) {
		 oldSwipeActionRight = swipeActionRight;
		 oldSwipeActionLeft = swipeActionLeft;
		 swipeActionRight = action;
		 swipeActionLeft = action;
	 }
	 
	 protected void returnOldActions() {
		 swipeActionRight = oldSwipeActionRight;
		 swipeActionLeft = oldSwipeActionLeft;
	 }
	 
	 /**
	  * Moves the view
	  *
	  * @param deltaX delta
	  */
	 public void move(float deltaX) {
		 swipeListView.onMove(downPosition, deltaX);
		 float posX = ViewHelper.getX(frontView);
		 if (opened.get(downPosition)) {
			 posX += openedRight.get(downPosition) ? -viewWidth + rightOffset : viewWidth - leftOffset;
		 }
		 if (posX > 0 && !swipingRight) {
			 Log.d("SwipeListView", "change to right");
			 swipingRight = !swipingRight;
			 swipeCurrentAction = swipeActionRight;
			 if (swipeCurrentAction == SwipeListView.SWIPE_ACTION_CHOICE) {
				 backView.setVisibility(View.GONE);
			 } else {
				 backView.setVisibility(View.VISIBLE);
			 }
		 }
		 if (posX < 0 && swipingRight) {
			 Log.d("SwipeListView", "change to left");
			 swipingRight = !swipingRight;
			 swipeCurrentAction = swipeActionLeft;
			 if (swipeCurrentAction == SwipeListView.SWIPE_ACTION_CHOICE) {
				 backView.setVisibility(View.GONE);
			 } else {
				 backView.setVisibility(View.VISIBLE);
			 }
		 }
		 if (swipeCurrentAction == SwipeListView.SWIPE_ACTION_DISMISS) {
			 setTranslationX(parentView, deltaX);
			 setAlpha(parentView, Math.max(0f, Math.min(1f,
					 1f - 2f * Math.abs(deltaX) / viewWidth)));
		 } else if (swipeCurrentAction == SwipeListView.SWIPE_ACTION_CHOICE) {
			 if ((swipingRight && deltaX > 0 && posX < DISPLACE_CHOICE)
					 || (!swipingRight && deltaX < 0 && posX > -DISPLACE_CHOICE)
					 || (swipingRight && deltaX < DISPLACE_CHOICE)
					 || (!swipingRight && deltaX > -DISPLACE_CHOICE)) {
				 setTranslationX(frontView, deltaX);
			 }
		 } else {
			 setTranslationX(frontView, deltaX);
		 }
	 }
	 
	 /**
	  * Class that saves pending dismiss data
	  */
     static class PendingDismissData implements Comparable<PendingDismissData> {
		 public int position;
		 public View view;
		 
		 public PendingDismissData(int position, View view) {
			 this.position = position;
			 this.view = view;
		 }
		 
		 @Override
		 public int compareTo(PendingDismissData other) {
			 // Sort by descending position
			 return other.position - position;
		 }
	 }
	 
	 /**
	  * Perform dismiss action
	  *
	  * @param dismissView     View
	  * @param dismissPosition Position of list
	  */
	 protected void performDismiss(final View dismissView, final int dismissPosition, boolean doPendingDismiss) {
		 final ViewGroup.LayoutParams lp = dismissView.getLayoutParams();
		 final int originalHeight = dismissView.getHeight();
		 
		 ValueAnimator animator = ValueAnimator.ofInt(originalHeight, 1).setDuration(animationTime);
		 
		 if (doPendingDismiss) {
			 animator.addListener(new AnimatorListenerAdapter() {
				 @Override
				 public void onAnimationEnd(Animator animation) {
					 --dismissAnimationRefCount;
					 if (dismissAnimationRefCount == 0) {
						 removePendingDismisses(originalHeight);
					 }
				 }
			 });
		 }
		 
		 animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			 @Override
			 public void onAnimationUpdate(ValueAnimator valueAnimator) {
				 lp.height = (Integer) valueAnimator.getAnimatedValue();
				 dismissView.setLayoutParams(lp);
			 }
		 });
		 
		 pendingDismisses.add(new PendingDismissData(dismissPosition, dismissView));
		 animator.start();
	 }
	 
	 protected void resetPendingDismisses() {
		 pendingDismisses.clear();
	 }
	 
	 protected void handlerPendingDismisses(final int originalHeight) {
		 Handler handler = new Handler();
		 handler.postDelayed(new Runnable() {
			 @Override
			 public void run() {
				 removePendingDismisses(originalHeight);
			 }
		 }, animationTime + 100);
	 }
	 
	 private void removePendingDismisses(int originalHeight) {
		 // No active animations, process all pending dismisses.
		 // Sort by descending position
		 Collections.sort(pendingDismisses);
		 
		 int[] dismissPositions = new int[pendingDismisses.size()];
		 for (int i = pendingDismisses.size() - 1; i >= 0; i--) {
			 dismissPositions[i] = pendingDismisses.get(i).position;
		 }
		 swipeListView.onDismiss(dismissPositions);
		 
		 ViewGroup.LayoutParams lp;
		 for (PendingDismissData pendingDismiss : pendingDismisses) {
			 // Reset view presentation
			 if (pendingDismiss.view != null) {
				 setAlpha(pendingDismiss.view, 1f);
				 setTranslationX(pendingDismiss.view, 0);
				 lp = pendingDismiss.view.getLayoutParams();
				 lp.height = originalHeight;
				 pendingDismiss.view.setLayoutParams(lp);
			 }
		 }
		 
		 resetPendingDismisses();
		 
	 }

}
