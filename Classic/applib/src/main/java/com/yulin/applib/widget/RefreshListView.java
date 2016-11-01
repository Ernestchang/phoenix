package com.yulin.applib.widget;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class RefreshListView extends ListView implements OnScrollListener, OnClickListener {
    private static final int PULL_BACK_REDUCE_STEP = 1;// 回弹时每次减少的高度
    private static final int PULL_BACK_TASK_PERIOD = 500;// 回弹时递减HeadView高度的频率, 注意以毫秒为单位
    private static final int PULL_DOWN_BACK_ACTION = 1;

    private final static int RELEASE_To_REFRESH = 0;// 释放去刷新
    private final static int PULL_To_REFRESH = 1;// 下拉去刷新
    private final static int REFRESHING = 2;// 正在刷新
    private final static int DONE = 3;// 完成
    private final static int LOADING = 4;// 正在加载

    // 实际的padding的距离与界面上偏移距离的比例
    private final static int RATIO = 3;

    private LayoutInflater inflater;

    private LinearLayout headView;

    private TextView tipsTextview;
    private TextView lastUpdatedTextView;
    private ImageView arrowImageView;
    private ProgressBar progressBar;

    private RotateAnimation animation;
    private RotateAnimation reverseAnimation;

    // 用于保证startY的值在一个完整的touch事件中只被记录一次
    private boolean isRecored;

    private int headContentHeight;

    private int startY;
    private int firstItemIndex;
    private int mCurrentScrollState;

    private int state;

    private boolean isBack;

    private boolean isRefreshable = false;// 下拉刷新的效果
    private boolean isPullDownBackAction = false;// 下拉回弹的效果

    private int mRefreshHeaderHandleImg = 0;

    // 下拉刷新ViewId
    private int mRefreshHeaderHandleImgId = 0;
    private int mRefreshHeaderProgressId = 0;
    private int mRefreshHeaderTitleId = 0;
    private int mRefreshHeaderSubTitleId = 0;

    // 下拉刷新ViewId
    public final static String REFRESHLISTVIEW_HEADER_HANDLE_IMG = "refreshlistview_header_handle_img";
    public final static String REFRESHLISTVIEW_HEADER_PROGRESS = "refreshlistview_header_progress";
    public final static String REFRESHLISTVIEW_HEADER_TITLE = "refreshlistview_header_title";
    public final static String REFRESHLISTVIEW_HEADER_SUBTITLE = "refreshlistview_header_subtitle";

    // 加载更多ViewId
    private int moreLayoutId = 0;
    private int moreTextViewId = 0;

    // 加载更多ViewId
    public final static String MORE_VIEW_LAYOUT = "moreLayout";
    public final static String MORE_VIEW_TEXT = "moreTxt";

    // 加载更多
    private boolean isLoadingMore = false;
    private boolean isLoadMoreEnable = false;
    private LinearLayout footerMoreLayout;
    private TextView footerMoreText;

    private OnRefreshListener refreshListener = null;
    private PostScrollListener mScrollListener = null;
    private RefreshListViewLoaMoreListener loadMoreListener = null;

    private ScheduledExecutorService schedulor;// 实现回弹效果的调度器

    public RefreshListView(Context context) {
        super(context);
        initResources();
    }

    public RefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initResources();
    }

    public RefreshListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initResources();
    }

    /**
     * 设置滚动的监听器
     * 
     * @param listener
     */
    public void setPostScrollListener(PostScrollListener listener) {
        mScrollListener = listener;
        setOnScrollListener(this);
    }

    /**
     * 设置下拉刷新的监听器
     * 
     * @param refreshListener
     */
    public void setOnRefreshListener(OnRefreshListener refreshListener) {
        this.refreshListener = refreshListener;
        isRefreshable = true;
    }

    /**
     * 设置加载更多的监听器
     * 
     * @param loadMoreListener
     */
    public void setOnLoadMoreListener(RefreshListViewLoaMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
    }

    /**
     * 下拉回弹的效果
     * 
     * @param isPullDownBackAction
     */
    public void setPullDownBackAction(boolean isPullDownBackAction) {
        this.isPullDownBackAction = isPullDownBackAction;
        headView = new LinearLayout(getContext());
        // 默认高度为0
        headView.setLayoutParams(new AbsListView.LayoutParams(LayoutParams.MATCH_PARENT, 0));
        addHeaderView(headView);
    }

    public void setRefreshHeaderHandleImg(int resId) {
        mRefreshHeaderHandleImg = resId;
    }

    public void initWithHeader(int headerLayoutId) {
        inflater = LayoutInflater.from(getContext());
        headView = (LinearLayout) inflater.inflate(headerLayoutId, null);
        arrowImageView = (ImageView) headView.findViewById(mRefreshHeaderHandleImgId);
        arrowImageView.setMinimumWidth(70);
        arrowImageView.setMinimumHeight(50);
        progressBar = (ProgressBar) headView.findViewById(mRefreshHeaderProgressId);
        tipsTextview = (TextView) headView.findViewById(mRefreshHeaderTitleId);
        lastUpdatedTextView = (TextView) headView.findViewById(mRefreshHeaderSubTitleId);

        measureView(headView);
        headContentHeight = headView.getMeasuredHeight();

        headView.setPadding(0, -1 * headContentHeight, 0, 0);
        headView.invalidate();

        // Log.v("size", "width:" + headContentWidth + " height:"
        // + headContentHeight);

        addHeaderView(headView, null, false);
        setOnScrollListener(this);

        animation = new RotateAnimation(0, -180, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        animation.setInterpolator(new LinearInterpolator());
        animation.setDuration(250);
        animation.setFillAfter(true);

        reverseAnimation = new RotateAnimation(-180, 0, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        reverseAnimation.setInterpolator(new LinearInterpolator());
        reverseAnimation.setDuration(200);
        reverseAnimation.setFillAfter(true);
        state = DONE;
        isRefreshable = false;
    }

    public void setAdapter(BaseAdapter adapter) {
        super.setAdapter(adapter);
    }

    public void setRefreshable(boolean isRefreshable) {
        this.isRefreshable = isRefreshable;
    }

    public void onRefreshFinished() {
        state = DONE;
        changeHeaderViewByState();
    }

    /**
     * 初始化加载更多的View
     * 
     * @param moreViewId
     */
    public void initWithMoreView(int moreViewId) {
        View moreView = inflater.inflate(moreViewId, null);
        footerMoreLayout = (LinearLayout) moreView.findViewById(moreLayoutId);
        setViewVisible(footerMoreLayout, false);
        footerMoreText = (TextView) moreView.findViewById(moreTextViewId);
        setViewVisible(footerMoreText, false);
        footerMoreText.setOnClickListener(this);

        addFooterView(moreView, null, false);
        setOnScrollListener(this);
    }

    /**
     * 是否加载更多
     * 
     * @param isLoadMoreEnable
     */
    public void setLoadMoreEnable(boolean isLoadMoreEnable) {
        this.isLoadMoreEnable = isLoadMoreEnable;
        if (isLoadMoreEnable) {
            showLoadingMore();
        } else {
            hideLoadMore();
        }
    }

    /**
     * 加载更多结束
     * 
     * @param hasMore
     */
    public void onLoadMoreComplete(boolean hasMore) {
        isLoadingMore = false;
        if (hasMore) {
            showLoadingMore();
        } else {
            isLoadMoreEnable = false;
            hideLoadMore();
        }
    }
    
    /**
     * 取消加载更多
     * */
    public void cancleLoadMore() {
        isLoadingMore = false;
        hideLoadMore();
    }

    /**
     * 显示加载失败的布局
     * 
     */
    public void onLoadMoreFail() {
        isLoadingMore = false;
        setViewVisible(footerMoreLayout, false);
        setViewVisible(footerMoreText, true);
    }

    private void initResources() {
        inflater = LayoutInflater.from(getContext());

        mRefreshHeaderHandleImgId = getContext().getResources().getIdentifier(REFRESHLISTVIEW_HEADER_HANDLE_IMG, "id", getContext().getPackageName());
        mRefreshHeaderProgressId = getContext().getResources().getIdentifier(REFRESHLISTVIEW_HEADER_PROGRESS, "id", getContext().getPackageName());
        mRefreshHeaderTitleId = getContext().getResources().getIdentifier(REFRESHLISTVIEW_HEADER_TITLE, "id", getContext().getPackageName());
        mRefreshHeaderSubTitleId = getContext().getResources().getIdentifier(REFRESHLISTVIEW_HEADER_SUBTITLE, "id", getContext().getPackageName());

        moreLayoutId = getContext().getResources().getIdentifier(MORE_VIEW_LAYOUT, "id", getContext().getPackageName());
        moreTextViewId = getContext().getResources().getIdentifier(MORE_VIEW_TEXT, "id", getContext().getPackageName());
    }

    @Override
    public void onClick(View v) {
        if (v == footerMoreText) {
            if (footerMoreText.getVisibility() == View.VISIBLE) {
                loadMore();
            }
        }
    }

    /**
     * 隐藏加载更多布局
     * */
    public void hideLoadMore() {
        setViewVisible(footerMoreLayout, false);
        setViewVisible(footerMoreText, false);
    }

    /**
     * 正在加载更多中
     * */
    public void showLoadingMore() {
        setViewVisible(footerMoreLayout, true);
        setViewVisible(footerMoreText, false);
    }

    // 加载更多
    private void loadMore() {
        isLoadingMore = true;
        showLoadingMore();
        if (loadMoreListener != null) {
            loadMoreListener.onLoadMore();
        }
    }

    public void onScroll(AbsListView view, int firstVisiableItem, int visiableItemCount, int totalItemCount) {
        if (mScrollListener != null)
            mScrollListener.postScroll(view, firstVisiableItem, visiableItemCount, totalItemCount);

        firstItemIndex = firstVisiableItem;
        
        if (visiableItemCount == totalItemCount) {
            hideLoadMore();
            return;
        }

        // 判断滚动到底部
        boolean loadMore = firstVisiableItem + visiableItemCount >= totalItemCount;
        if (isLoadMoreEnable && !isLoadingMore && loadMore && mCurrentScrollState != SCROLL_STATE_IDLE) {
            loadMore();
        }
    }

    public void onScrollStateChanged(AbsListView view, int scrollState) {
        // bug fix: listView was not clickable after scroll
        if (scrollState == OnScrollListener.SCROLL_STATE_IDLE)
            view.invalidateViews();

        mCurrentScrollState = scrollState;

        if (mScrollListener != null)
            mScrollListener.postScrollStateChanged(view, scrollState);
    }

    public void postRefresh() {
        state = REFRESHING;
        changeHeaderViewByState();
        onRefresh();
        // Log.v(TAG, "由松开刷新状态，到done状态");
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (isPullDownBackAction) {
            // 下拉回弹的效果
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    // 按下
                    break;
                case MotionEvent.ACTION_UP:
                    // 松开
                    if (isRecored) {
                        if (headView != null && headView.getPaddingTop() > 0) {
                            schedulor = Executors.newScheduledThreadPool(1);
                            schedulor.scheduleAtFixedRate(new Runnable() {

                                @Override
                                public void run() {
                                    mHandler.sendEmptyMessage(PULL_DOWN_BACK_ACTION);

                                }
                            }, 0, PULL_BACK_TASK_PERIOD, TimeUnit.MICROSECONDS);
                        }
                        isRecored = false;
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    int tempY = (int) event.getY();

                    if (!isRecored && firstItemIndex == 0) {
                        isRecored = true;
                        startY = tempY;
                    }

                    if (isRecored) {
                        int moveY = tempY - startY;
                        if (moveY < 0) {
                            isRecored = false;
                            break;
                        }

                        if (headView != null) {
                            headView.setPadding(0, (tempY - startY) / RATIO, 0, 0);
                        }
                    }
                    break;
            }
        } else {
            if (isRefreshable) {
                // 下拉刷新
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // 按下
                        if (firstItemIndex == 0 && !isRecored) {
                            isRecored = true;
                            startY = (int) event.getY();
                            // Log.v(TAG, "在down时候记录当前位置‘");
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        // 松开
                        if (state != REFRESHING && state != LOADING) {
                            if (state == DONE) {
                                // 什么都不做
                            }
                            if (state == PULL_To_REFRESH) {
                                state = DONE;
                                changeHeaderViewByState();

                                // Log.v(TAG, "由下拉刷新状态，到done状态");
                            }
                            if (state == RELEASE_To_REFRESH) {
                                postRefresh();
                            }
                        }

                        isRecored = false;
                        isBack = false;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int tempY = (int) event.getY();

                        if (!isRecored && firstItemIndex == 0) {
                            // Log.v(TAG, "在move时候记录下位置");
                            isRecored = true;
                            startY = tempY;
                        }

                        if (state != REFRESHING && isRecored && state != LOADING) {
                            // 保证在设置padding的过程中，当前的位置一直是在head，否则如果当列表超出屏幕的话，当在上推的时候，列表会同时进行滚动

                            // 可以松手去刷新了
                            if (state == RELEASE_To_REFRESH) {
                                setSelection(0);

                                // 往上推了，推到了屏幕足够掩盖head的程度，但是还没有推到全部掩盖的地步
                                if (((tempY - startY) / RATIO < headContentHeight) && (tempY - startY) > 0) {
                                    state = PULL_To_REFRESH;
                                    changeHeaderViewByState();
                                    // Log.v(TAG, "由松开刷新状态转变到下拉刷新状态");
                                }
                                // 一下子推到顶了
                                else if (tempY - startY <= 0) {
                                    state = DONE;
                                    changeHeaderViewByState();

                                    // Log.v(TAG, "由松开刷新状态转变到done状态");
                                }
                                // 往下拉了，或者还没有上推到屏幕顶部掩盖head的地步
                                else {
                                    // 不用进行特别的操作，只用更新paddingTop的值就行了
                                }
                            }
                            // 还没有到达显示松开刷新的时候,DONE或者是PULL_To_REFRESH状态
                            if (state == PULL_To_REFRESH) {

                                setSelection(0);

                                // 下拉到可以进入RELEASE_TO_REFRESH的状态
                                if ((tempY - startY) / RATIO >= headContentHeight) {
                                    state = RELEASE_To_REFRESH;
                                    isBack = true;
                                    changeHeaderViewByState();

                                    // Log.v(TAG, "由done或者下拉刷新状态转变到松开刷新");
                                }
                                // 上推到顶了
                                else if (tempY - startY <= 0) {
                                    state = DONE;
                                    changeHeaderViewByState();

                                    // Log.v(TAG, "由DOne或者下拉刷新状态转变到done状态");
                                }
                            }

                            // done状态下
                            if (state == DONE) {
                                if (tempY - startY > 0) {
                                    state = PULL_To_REFRESH;
                                    changeHeaderViewByState();
                                }
                            }

                            // 更新headView的size
                            if (state == PULL_To_REFRESH) {
                                headView.setPadding(0, -1 * headContentHeight + (tempY - startY) / RATIO, 0, 0);

                            }

                            // 更新headView的paddingTop
                            if (state == RELEASE_To_REFRESH) {
                                headView.setPadding(0, (tempY - startY) / RATIO - headContentHeight, 0, 0);
                            }
                        }
                        break;
                }
            }
        }

        return super.onTouchEvent(event);
    }

    // private static final int MAX_Y_OVERSCROLL_DISTANCE = 200;
    // private int mMaxYOverscrollDistance;
    //
    // private void initBounceListView() {
    // // get the density of the screen and do some maths with it on the max overscroll distance
    // // variable so that you get similar behaviors no matter what the screen size
    //
    // final DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
    // final float density = metrics.density;
    // mMaxYOverscrollDistance = (int) (density * MAX_Y_OVERSCROLL_DISTANCE);
    // }
    //
    // @Override
    // protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int
    // scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent)
    // {
    // // This is where the magic happens, we have replaced the incoming maxOverScrollY with our
    // // own custom variable mMaxYOverscrollDistance;
    // return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY,
    // maxOverScrollX, mMaxYOverscrollDistance, isTouchEvent);
    // }

    // 当状态改变时候，调用该方法，以更新界面
    private void changeHeaderViewByState() {
        switch (state) {
            case RELEASE_To_REFRESH:
                setViewVisible(arrowImageView, true);
                setViewVisible(progressBar, false);
                setViewVisible(tipsTextview, true);
                setViewVisible(lastUpdatedTextView, true);

                arrowImageView.clearAnimation();
                arrowImageView.startAnimation(animation);

                tipsTextview.setText("松开刷新");

                // Log.v(TAG, "当前状态，松开刷新");
                break;
            case PULL_To_REFRESH:
                setViewVisible(progressBar, false);
                setViewVisible(tipsTextview, true);
                setViewVisible(lastUpdatedTextView, true);
                arrowImageView.clearAnimation();
                setViewVisible(arrowImageView, true);
                // 是由RELEASE_To_REFRESH状态转变来的
                if (isBack) {
                    isBack = false;
                    arrowImageView.clearAnimation();
                    arrowImageView.startAnimation(reverseAnimation);

                    tipsTextview.setText("下拉刷新");
                } else {
                    tipsTextview.setText("下拉刷新");
                }
                if (refreshListener != null) {
                    refreshListener.beforeRefresh();
                }
                // Log.v(TAG, "当前状态，下拉刷新");
                break;

            case REFRESHING:

                headView.setPadding(0, 0, 0, 0);

                setViewVisible(progressBar, true);
                arrowImageView.clearAnimation();
                setViewVisible(arrowImageView, false);
                tipsTextview.setText("正在刷新...");
                setViewVisible(lastUpdatedTextView, true);

                // Log.v(TAG, "当前状态,正在刷新...");
                break;
            case DONE:
                headView.setPadding(0, -1 * headContentHeight, 0, 0);

                setViewVisible(progressBar, false);
                arrowImageView.clearAnimation();
                arrowImageView.setImageResource(mRefreshHeaderHandleImg);
                tipsTextview.setText("下拉刷新");
                setViewVisible(lastUpdatedTextView, true);
                if (refreshListener != null) {
                    refreshListener.afterRefresh();
                }
                // Log.v(TAG, "当前状态，done");
                break;
        }
    }

    private void onRefresh() {
        if (refreshListener != null) {
            refreshListener.onRefresh();
        }
    }

    // 此方法直接照搬自网络上的一个下拉刷新的demo，此处是“估计”headView的width以及height
    private void measureView(View child) {
        ViewGroup.LayoutParams p = child.getLayoutParams();
        if (p == null) {
            p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
        int lpHeight = p.height;
        int childHeightSpec;
        if (lpHeight > 0) {
            childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight, MeasureSpec.EXACTLY);
        } else {
            childHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        }
        child.measure(childWidthSpec, childHeightSpec);
    }

    public void updateRefreshDate(String dateString) {
        lastUpdatedTextView.setText(dateString);
    }

    public void updateTextColor(int color) {
        tipsTextview.setTextColor(color);
        lastUpdatedTextView.setTextColor(color);
    }

    public void updateRefreshBgColor(int color) {
        headView.setBackgroundColor(color);
    }

    public boolean isRelease2Refresh() {
        return state == RELEASE_To_REFRESH;
    }

    public boolean isPull2Refresh() {
        return state == PULL_To_REFRESH;
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case PULL_DOWN_BACK_ACTION:
                    // 递减高度
                    int padding = headView.getPaddingTop() - PULL_BACK_REDUCE_STEP;
                    // 停止回弹时递减headView高度的任务
                    if (padding <= 0) {
                        padding = 0;
                        schedulor.shutdownNow();
                    }

                    headView.setPadding(0, padding, 0, 0);
                    // 重绘
                    headView.invalidate();
                    break;
            }
        }
    };

    /**
     * 滚动监听器
     * 
     * @ClassName: PostScrollListener
     * @Description:
     * @author xiechengfa
     * @date 2016年2月14日 下午5:28:58
     *
     */
    public interface PostScrollListener {
        public void postScrollStateChanged(AbsListView view, int scrollState);

        public void postScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount);
    }

    /**
     * 下拉刷新监听器
     * 
     * @ClassName: OnRefreshListener
     * @Description:
     * @author xiechengfa
     * @date 2016年2月14日 下午5:28:36
     *
     */
    public interface OnRefreshListener {
        /**
         * 正在刷新
         */
        public void onRefresh();

        /**
         * 刷新之前
         */
        public void beforeRefresh();

        /**
         * 刷新之后
         */
        public void afterRefresh();
    }

    /**
     * @ClassName: RefreshListViewLoaMoreListener
     * @Description:加载更多监听器
     * @author xiechengfa
     * @date 2016年1月25日 下午5:40:44
     *
     */
    public interface RefreshListViewLoaMoreListener {
        /**
         * 加载更多
         */
        public void onLoadMore();
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        try {
            super.dispatchDraw(canvas);
        } catch (IndexOutOfBoundsException e) {
            // samsung error
        }
    }
    
    private void setViewVisible(View view, boolean isVisible) {
        if (view == null) return;
        
        if (isVisible)
            view.setVisibility(View.VISIBLE);
        else
            view.setVisibility(View.GONE);
    }
    
}
