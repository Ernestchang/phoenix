package com.yulin.applib.page;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;

import com.yulin.applib.bar.Bar;
import com.yulin.applib.bar.BarMenuContainer;
import com.yulin.applib.bar.BarMenuItem;
import com.yulin.applib.log.Tracer;
import com.yulin.applib.module.Module;
import com.yulin.applib.page.i.OnPageViewListener;
import com.yulin.applib.page.i.PageAnimationI;
import com.yulin.applib.page.i.PageBarI;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 基本视图 模拟了Activity部分结构 通过@PageIntent 切换界面 通过PageIntent可以设置启动类型，可以设置入参，可以设置是否打开日志，可以设置界面切换动画
 * <p>
 * 框架的核心思想是：用最高效的方式收发数据，用最灵活的流转组织界面，用最少量的代码实现业务。
 * <p>
 *
 * @version 1.0
 */
public abstract class Page extends Fragment implements PageAnimationI, PageBarI, OnTouchListener, PageContext {

    private String mPageTag = null;

    protected int mPageId = 0;
    public PageContext mLinkedContext = null;
    private PageIntent mPageIntent = null;

    private boolean mIsSupportAnimation = true;// 是否支持界面切换动画

    private List<Integer> mBoundBarIds = new ArrayList<Integer>();// 被绑定的bar列表
    private Map<Integer, BarMenuItem> mMapRegistedBars = new HashMap<Integer, BarMenuItem>();
    private boolean mNeedPrintLog = false;// 是否打印page生命周期相关日志

    /**
     * page的界面状态
     * <p>
     * emoney
     */
    private static enum PageStatus {
        UNCREATE, // 未创建状态
        CREATE, // 初始状态
        PAUSE, // 暂停状态
        RESUMED, // 恢复/运行状态
        DESTROY// 销毁状态
    }

    ;

    private PageStatus mPageStatus = PageStatus.UNCREATE;

    private List<OnPageViewListener> mLstPageViewListeners = new ArrayList<OnPageViewListener>();

    protected View mContentView = null;
    private LayoutInflater mInflater = null;
    private ViewGroup mPageContainer = null;
    public int mContainerId = 0;// page显示的容器View id

    private int mResultCode = -1;//
    private int mRequestCode = -1;
    private Bundle mResultData = null;

    private Page mParentPage = null;// 对于嵌套在视图中的page，必须有个依赖的page作为父page

    private Page mPendingPage = null;// 当前page销毁的时候，即将创建的page

    public void setPageId(int id) {
        mPageId = id;
    }

    public void setParent(Page page) {
        mParentPage = page;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        printLog("onActivityCreated");
        dispatchPageCreate();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        printLog("onViewCreated");
        view.setOnTouchListener(this);
        super.onViewCreated(view, savedInstanceState);
    }

    /**
     * 防止onTouch事件泄露到底层page
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return true;
    }

    /**
     * 更新已绑定的bar
     */
    private void onUpdateBars() {
        if (getContext() == null) {
            return;
        }
        Activity acti = (Activity) getContext();
        if (mBoundBarIds.size() > 0) {
            if (!getUserVisibleHint()) {
                return;
            }
            for (int i = 0; i < mBoundBarIds.size(); i++) {
                final int barId = mBoundBarIds.get(i);
                Bar bar = (Bar) acti.findViewById(barId);
                if (bar != null) {
                    BarMenuContainer menu = new BarMenuContainer();
                    if (dispatchPageBarMenu(barId, menu)) {
                        bar.clearBarMenu();
                        bar.addMenuItems(menu.getItems());
                        bar.setBarMenuProgress(menu.getProgress());
                        bar.setOnBarMenuSelectedListener(new Bar.OnBarMenuSelectedListener() {

                            @Override
                            public void onItemSelected(int index, BarMenuItem item) {
                                onPageBarMenuItemSelected(barId, item);
                            }
                        });
                        bar.notifyBarSetChanged();

                        onPageBarMenuCreated(barId, bar.getBarMenu());
                    }
                }
            }
        }
    }

    @Override
    public void onAttach(Activity activity) {
        if (mLinkedContext != null
        /* && mLinkedContext.isAdded() */) {
            if (mLinkedContext.needPauseAndResumeWhenSwitch()) {
                mLinkedContext.dispatchPagePause(false);
            }
        }
        super.onAttach(activity);
        printLog("onAttach");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        printLog("onCreate");
    }

    public void setContentView(int layoutId) {
        mContentView = mInflater.inflate(layoutId, mPageContainer, false);
    }

    public void setContentView(View view) {
        mContentView = view;
    }

    public void setContentView(View view, ViewGroup.LayoutParams params) {
        mContentView = view;
        mContentView.setLayoutParams(params);
    }

    public View findViewById(int id) {
        return mContentView.findViewById(id);
    }

    public View findViewByTag(Object tag) {
        return mContentView.findViewWithTag(tag);
    }

    @Override
    final public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        printLog("onCreateView");
        mInflater = inflater;
        mPageContainer = container;
        beforeCreate();
        initPageTag();
        initPage();
        dispatchData(getArguments());
        initData();

        return mContentView;
    }

    /**
     * 将page转化为普通的view 拥有与page除生命周期意外的其他各种资源
     *
     * @param parent
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    public View convertToView(Page parent, LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setParent(parent);
        return onCreateView(inflater, container, savedInstanceState);
    }

    public void beforeCreate() {

    }

    /**
     * 初始化界面 在这里调用setContentView设置界面的view，获取并初始化控件
     */
    protected abstract void initPage();

    /**
     * 初始化数据 在这里将初始化数据赋值给对应的控件
     */
    protected abstract void initData();


    private void initPageTag() {
        mPageTag = System.currentTimeMillis() + "";
    }

    protected String getPageTag() {
        return mPageTag;
    }

    public void dispatchData(Bundle bundle) {
        // for(int i = 0; i < mLstPageViewListeners.size(); i++)
        // {
        // mLstPageViewListeners.get(i).dispatchData(bundle);
        // }
        receiveData(bundle);
    }

    /**
     * 接收onCreate传入的数据，只在创建page时调用一次
     *
     * @param intent
     */
    protected void receiveData(Bundle arguments) {

    }

    /**
     * 接收onNewIntent传入的数据 page已存在在栈中时，收到PageIntent，执行该方法
     *
     * @param intent
     */
    protected void receiveNewData(PageIntent intent) {

    }

    public void dispatchPageCreate() {
        mPageStatus = PageStatus.CREATE;
        onPageCreate();
    }

    /**
     * page界面构建完毕时触发
     */
    protected void onPageCreate() {
        printLog("onPageCreate");
        getPageManager().registIntent(mPageIntent);
    }

    @Override
    final public void onDestroy() {
        super.onDestroy();
        printLog("onDestroy");
        dispatchPageDestroy();
    }

    /**
     * 销毁界面 清除数据，停止网络请求，停止线程等操作
     */
    public void dispatchPageDestroy() {
        mPageStatus = PageStatus.DESTROY;
        onPageDestroy();
    }

    /**
     * page被销毁时触发
     */
    protected void onPageDestroy() {
        printLog("onPageDestroy");
        destroyPagesInView();
        PageManager pageManager = getPageManager();
        if (pageManager != null && mPageIntent != null) {
            pageManager.unregistIntent(mPageIntent);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        printLog("onDestroyView");
    }

    @Override
    public void onDetach() {
        super.onDetach();

        // 防止出现No Activity的错误
        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        printLog("onDetach");
        if (mLinkedContext != null && mLinkedContext.isAdded2Stack()) {
            if (mPendingPage != null) {
                // 将当前page依赖的PageContext赋值到即将启动的page上
                mPendingPage.mLinkedContext = mLinkedContext;
            } else {
                if (mRequestCode >= 0 && mResultCode >= 0) {
                    mLinkedContext.dispatchPageResult(mRequestCode, mResultCode, mResultData);
                }
                if (mLinkedContext.needPauseAndResumeWhenSwitch()) {
                    mLinkedContext.dispatchPageResume(false);
                } else {
                    mLinkedContext.dispatchUpdateBars();
                }
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        printLog("onPause");
        if (mParentPage != null) {
            if (mParentPage.getUserVisibleHint() && getUserVisibleHint()) {
                dispatchPagePause(true);
            }
        } else {
            if (getUserVisibleHint()) {
                dispatchPagePause(true);
            }
        }
    }

    public void dispatchPagePause(boolean isFromSystem) {
        mPageStatus = PageStatus.PAUSE;
        onPagePause();
        if (/* getUserVisibleHint() && */!isFromSystem) {
            for (int i = 0; i < mLstPageViewListeners.size(); i++) {
                mLstPageViewListeners.get(i).onViewPause();
            }
        }
    }

    /**
     * page暂停时触发
     */
    protected void onPagePause() {
        printLog("onPagePause");
        if (getUserVisibleHint()) {
            // for(int i = 0; i < mLstPageViewListeners.size(); i++)
            // {
            // mLstPageViewListeners.get(i).onViewPause();
            // }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // 初始化屏幕宽高相关的数据
        printLog("onResume");

        if (mParentPage != null) {
            if (mParentPage.getUserVisibleHint() && getUserVisibleHint()) {
                dispatchPageResume(true);
            }
        } else {
            if (getUserVisibleHint()) {
                dispatchPageResume(true);
            }
        }
    }

    public void dispatchPageResume(boolean isFromSystem) {
        mPageStatus = PageStatus.RESUMED;
        onPageResume();
        if (getUserVisibleHint() && !isFromSystem) {
            for (int i = 0; i < mLstPageViewListeners.size(); i++) {
                mLstPageViewListeners.get(i).onViewResume();
            }
        }
    }

    /**
     * page恢复状态时触发
     */
    protected void onPageResume() {
        printLog("onPageResume");
        if (getUserVisibleHint()) {
            dispatchUpdateBars();
            // for(int i = 0; i < mLstPageViewListeners.size(); i++)
            // {
            // mLstPageViewListeners.get(i).onViewResume();
            // }
        }
    }

    /**
     * 派发更新bar的事件
     */
    public void dispatchUpdateBars() {
        onUpdateBars();
    }

    public boolean needPauseAndResumeWhenSwitch() {
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();
        printLog("onStart");
    }

    @Override
    public void onStop() {
        super.onStop();
        printLog("onStop");
    }

    /**
     * 标题菜单选中事件回调
     *
     * @param menuitem
     */
    public void onPageBarMenuItemSelected(int barId, BarMenuItem menuitem) {

    }

    /**
     * 创建标题菜单事件回调
     *
     * @param menu
     * @return
     */
    public boolean onCreatePageBarMenu(int barId, BarMenuContainer menu) {
        return false;
    }

    /**
     * 创建标题菜单完成事件回调
     *
     * @param barId
     * @param menu
     */
    protected void onPageBarMenuCreated(int barId, BarMenuContainer menu) {

    }

    /**
     * 创建标题菜单 获取进度条回调
     */
    protected View getPageBarMenuProgress() {
        return null;
    }

    /**
     * 派发给page创建标题菜单
     *
     * @param menu
     * @return
     */
    public boolean dispatchPageBarMenu(int barId, BarMenuContainer menu) {

        boolean bRet = onCreatePageBarMenu(barId, menu);
        if (bRet) {
            View pb = getPageBarMenuProgress();
            if (pb != null) {
                menu.setProgress(pb);
            }
        }
        return bRet;
    }

    /**
     * 设置是否支持动画
     *
     * @param isSupported
     */
    public void setSupportAnimation(boolean isSupported) {
        mIsSupportAnimation = isSupported;
    }

    /**
     * 是否支持动画
     *
     * @return
     */
    public boolean isSupportAnimation() {
        return mIsSupportAnimation;
    }

    /**
     * 默认没有动画
     * */
    public int enterAnimation() {
        return 0;
    }

    /**
     * 默认没有动画
     * */
    public int exitAnimation() {
        return 0;
    }

    /**
     * 默认没有动画
     * */
    public int popEnterAnimation() {
        return 0;
    }

    /**
     * 默认没有动画
     * */
    public int popExitAnimation() {
        return 0;
    }

    /**
     * 绑定bar 即拥有该bar的所有控制权
     *
     * @param barId
     */
    public void bindBar(int barId) {
        if (!mBoundBarIds.contains(barId)) {
            mBoundBarIds.add(barId);
        }
    }

    /**
     * 解除绑定bar 解除对该bar的所有控制权
     *
     * @param barId
     */
    public void unbindBar(int barId) {
        if (mBoundBarIds.contains(barId)) {
            mBoundBarIds.remove(barId);
        }
    }

    /**
     * 注册bar 即拥有该bar的部分使用权
     *
     * @param bar
     * @param menuItem
     */
    public void registBar(Bar bar, BarMenuItem menuItem) {
        if (bar != null) {
            int barId = bar.getId();
            if (!mMapRegistedBars.containsKey(barId)) {
                mMapRegistedBars.put(barId, menuItem);
            }
            bar.addMenuItem(menuItem);
            bar.addMenuItemNow(menuItem);
        }
    }

    /**
     * 注册bar 即拥有该bar的部分使用权
     *
     * @param bar
     * @param menuItem
     */
    public void replaceBarItem(int index, Bar bar, BarMenuItem menuItem) {
        if (bar != null) {
            int barId = bar.getId();
            if (!mMapRegistedBars.containsKey(barId)) {
                mMapRegistedBars.put(barId, menuItem);
            }
            bar.replaceMenuItem(index, menuItem);
        }
    }

    /**
     * 注销bar 解除对该bar的使用控制
     *
     * @param barId
     */
    public void unregistBar(int barId) {
        if (mMapRegistedBars.containsKey(barId)) {
            mMapRegistedBars.remove(Integer.valueOf(barId));
        }
    }

    /**
     * 获取当前page依赖的模块
     *
     * @return
     */
    public Module getModule() {
        Activity acti = getActivity();
        if (acti == null) {
            if (mParentPage != null) {
                acti = mParentPage.getActivity();
            }
        }
        if (acti instanceof Module) {
            return (Module) acti;
        }

        return null;
    }

    /**
     * 启动模块
     *
     * @param clz 模块的类别
     */
    public void startModule(Class<? extends Module> clz) {
        Module module = getModule();
        if (module != null) {
            module.startModule(clz);
        }
    }

    /**
     * 启动模块
     *
     * @param bundle 需要传递数据
     * @param clz    模块的类别
     */
    public void startModule(Bundle bundle, Class<? extends Module> clz) {
        Module module = getModule();
        if (module != null) {
            module.startModule(bundle, clz);
        }
    }

    /**
     * 启动模块 要求返回结果
     *
     * @param bundle  需要传递数据
     * @param clz     模块的类别
     * @param reqCode 启动页面的code
     */
    public void startModuleForResult(Bundle bundle, Class<? extends Module> clz, int reqCode) {
        Module module = getModule();
        if (module != null) {
            module.startModuleForResult(bundle, clz, reqCode);
        }
    }

    /**
     * 启动page
     *
     * @param containerId page所依赖的view id
     * @param intent      page意图
     */
    public void startPage(int containerId, PageIntent intent) {
        Module module = getModule();
        if (module != null) {
            module.startPage(containerId, intent);
        }
    }

    /**
     * 启动page
     *
     * @param intent page意图
     */
    public void startPage(PageIntent intent) {
        int containerId = mContainerId;
        if (mContainerId == 0) {
            containerId = mParentPage.getContainerId();
        }
        startPage(containerId, intent);
    }

    /**
     * 启动page，要求返回结果
     *
     * @param containerId page所依赖的view id
     * @param intent      page意图
     * @param requestCode 请求code
     */
    public void startPageForResult(int containerId, PageIntent intent, int requestCode) {
        Module module = getModule();
        if (module != null) {
            module.startPageForResult(containerId, intent, requestCode);
        }
    }

    /**
     * 启动page，要求返回结果
     *
     * @param intent      page意图
     * @param requestCode 请求code
     */
    public void startPageForResult(PageIntent intent, int requestCode) {
        int containerId = mContainerId;
        if (mParentPage != null) {
            containerId = mParentPage.getContainerId();
        }
        startPageForResult(containerId, intent, requestCode);
    }

    /**
     * 结束当前page
     */
    public void finish() {
        if (mParentPage != null) {
            mParentPage.finish();
        } else {
            if (getFragmentManager().getBackStackEntryCount() <= 1) {
                if (mLinkedContext == null) {
                    finishModule();
                } else {
                    if (mLinkedContext.isAdded2Stack()) {
                        getFragmentManager().popBackStack(getStackKey(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    } else {
                        finishModule();
                    }
                }
            } else {
                getFragmentManager().popBackStack(getStackKey(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        }
    }

    /**
     * 立即结束当前page
     */
    public void finishImmediate(Page pendingPage) {
        if (mParentPage != null) {
            mParentPage.finishImmediate(pendingPage);
        } else {
            mPendingPage = pendingPage;
            if (getFragmentManager().getBackStackEntryCount() <= 1) {
                if (mLinkedContext == null) {
                    finishModule();
                } else {
                    if (mLinkedContext.isAdded2Stack()) {
                        getFragmentManager().popBackStackImmediate(getStackKey(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    } else {
                        finishModule();
                    }
                }
            } else {
                getFragmentManager().popBackStackImmediate(getStackKey(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        }
    }

    public void finishModule() {
        Module module = getModule();
        if (module != null) {
            module.finish();
            //module.overridePendingTransition(0,0);
        }
    }


    public void finishToPage(PageIntent intent) {
        Module module = getModule();
        if (module != null) {
            module.finishToPage(intent);
        }
    }

    /**
     * 获取所有被绑定的bar
     *
     * @return
     */
    public List<Integer> getBoundBarIds() {
        return mBoundBarIds;
    }

    /**
     * 获取所有被注册的bar
     *
     * @return
     */
    public List<Integer> getRegistedBarIds() {
        List<Integer> keys = new ArrayList<Integer>();
        for (Entry<Integer, BarMenuItem> entry : mMapRegistedBars.entrySet()) {
            int barId = entry.getKey();
            keys.add(barId);
        }
        return keys;
    }

    private void printLog(String msg) {
        if (mNeedPrintLog)
            Tracer.V(getClass().getSimpleName(), msg);
    }

    public void needPringLog(boolean flag) {
        mNeedPrintLog = flag;
    }

    public void setPageIntent(PageIntent intent) {
        mPageIntent = intent;
    }

    public PageIntent getPageIntent() {
        return mPageIntent;
    }

    public void dispatchNewIntent(PageIntent intent) {
        onNewIntent(intent);
    }

    protected void onNewIntent(PageIntent intent) {
        receiveNewData(intent);
    }

    public void dispatchPageResult(int requestCode, int resultCode, Bundle data) {
        onPageResult(requestCode, resultCode, data);
    }

    /**
     * 获取上一个page提交的数据
     *
     * @param resultCode 结果code
     * @param data       数据
     */
    protected void onPageResult(int requestCode, int resultCode, Bundle data) {
        if (getUserVisibleHint()) {
            for (int i = 0; i < mLstPageViewListeners.size(); i++) {
                mLstPageViewListeners.get(i).onViewResult(requestCode, resultCode, data);
            }
        }
    }

    public int getContainerId() {
        return mContainerId;
    }

    /**
     * 设置requestCode
     *
     * @param requestCode
     */
    public void setRequest(int requestCode) {
        mRequestCode = requestCode;
    }

    /**
     * 取得requestCode
     *
     * @return 上个page传送的reauestCode
     */
    public int getRequest() {
        return mRequestCode;
    }

    /**
     * 设置结果code
     *
     * @param resultCode
     */
    public void setResult(int resultCode) {
        mResultCode = resultCode;
    }

    /**
     * 设置结果code与数据
     *
     * @param resultCode 结果code
     * @param data       数据
     */
    public void setResult(int resultCode, Bundle data) {
        mResultCode = resultCode;
        mResultData = data;
    }

    /**
     * 对用户来说是否可见
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        try {
            super.setUserVisibleHint(isVisibleToUser);
            printLog("isVisibleToUser:" + isVisibleToUser);

            if (mPageStatus == PageStatus.UNCREATE) {
                return;
            }
            if (isVisibleToUser) {
                dispatchPageResume(false);
            } else {
                dispatchPagePause(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 将包含Page的view类型的视图控件(@PageSwitcher, @PageFlipper等)注册到当前page， 让当前page管理view中包含的所有Page，包括生命周期
     *
     * @param listener
     */
    public void registViewWithPage(OnPageViewListener listener) {
        if (listener == null) {
            return;
        }
        if (!mLstPageViewListeners.contains(listener)) {
            listener.registToPage(this);
            mLstPageViewListeners.add(listener);
        }
    }

    protected void destroyPagesInView() {
        // if(getUserVisibleHint())
        // {
        // for(int i = 0; i < mLstPageViewListeners.size(); i++)
        // {
        // mLstPageViewListeners.get(i).onViewDestroy();
        // }
        // }
        mLstPageViewListeners.clear();
    }

    /**
     * 唯一的栈标记
     *
     * @return
     */
    public String getStackKey() {
        return String.valueOf(hashCode());
    }

    public PageManager getPageManager() {
        Module module = getModule();
        if (module != null) {
            return module.getPageManager();
        }
        return null;
    }

    public SharedPreferences getSharedPreferences(String name, int mode) {
        Module module = getModule();
        if (module != null) {
            return module.getSharedPreferences(name, mode);
        }
        return null;
    }

    public Page getParent() {
        return mParentPage;
    }

    public Context getContext() {
        if (mInflater == null) {
            return getActivity();
        }
        return mInflater.getContext();
    }

    public View getContentView() {
        return mContentView;
    }

    /**
     * 获取缓存文件夹
     *
     * @return
     */
    public File getCacheDir() {
        Module module = getModule();
        if (module != null) {
            return module.getCacheDir();
        }
        return null;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        for (int i = 0; i < mLstPageViewListeners.size(); i++) {
            if (mLstPageViewListeners.get(i).onKeyDown(keyCode, event)) {
                return true;
            }
        }
        return false;
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        for (int i = 0; i < mLstPageViewListeners.size(); i++) {
            if (mLstPageViewListeners.get(i).onKeyUp(keyCode, event)) {
                return true;
            }
        }
        return false;
    }

    public boolean isAdded2Stack() {
        return isAdded();
    }
}
