package com.yulin.applib.module;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;

import com.yulin.applib.page.Page;
import com.yulin.applib.page.PageContext;
import com.yulin.applib.page.PageIntent;
import com.yulin.applib.page.PageManager;
import com.yulin.applib.page.i.OnPageViewListener;
import com.yulin.applib.page.i.PageAnimationI;

import java.util.ArrayList;
import java.util.List;

/**
 * 核心模块类 理论上，一个完整的应用程序只需一个module即可 弱化了Activity，从而将重点转入@Page 的构建和运用。 模块内部通过page切换视图 emoney
 *
 * @version 1.0
 */
public abstract class Module extends FragmentActivity {

    private static long lastKeyTime = 0;
    private PageManager mPageManager = null;
    private List<OnPageViewListener> mLstPageViewListeners = new ArrayList<>();

    @Override
    final protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        beforeCreate(savedInstanceState);
        // 启动前，清除所有page栈
        getSupportFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        receiveData(getIntent());
        initModule();
        initData();
    }

    protected void beforeCreate(Bundle savedInstanceState) {
    }

    protected void receiveData(Intent intent) {
    }

    protected void initModule() {
    }

    protected void initData() {
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        receiveNewData(intent);
    }

    protected void receiveNewData(Intent intent) {
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        PageIntent intent = getPageManager().getTopIntent();

        if (intent != null && intent.getTargetInstance() != null) {
            if (intent.getTargetInstance().onKeyUp(keyCode, event)) {
                return true;
            }
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        long t = System.currentTimeMillis();
        if (Math.abs(t - lastKeyTime) < 800) {
            return true;
        }
        lastKeyTime = t;

        PageIntent intent = getPageManager().getTopIntent();

        if (intent != null && intent.getTargetInstance() != null) {
            if (intent.getTargetInstance().onKeyDown(keyCode, event)) {
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * @param clz
     * @param requestCode
     */
    public void startModuleForResult(Class<? extends Module> clz, int requestCode) {
        Intent intent = new Intent(this, clz);
        startActivityForResult(intent, requestCode);
    }

    /**
     * @param bundle
     * @param clz
     * @param requestCode
     */
    public void startModuleForResult(Bundle bundle, Class<? extends Module> clz, int requestCode) {
        Intent intent = new Intent(this, clz);
        intent.putExtras(bundle);
        startActivityForResult(intent, requestCode);
    }

    /**
     * 启动模块
     *
     * @param clz 模块类
     */
    public void startModule(Class<? extends Module> clz) {
        Intent intent = new Intent(this, clz);
        startActivity(intent);
    }

    /**
     * 启动模块
     *
     * @param bundle 数据
     * @param clz    模块类
     */
    public void startModule(Bundle bundle, Class<? extends Module> clz) {
        Intent intent = new Intent(this, clz);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    /**
     * 启动新界面
     *
     * @param containerId page所在的视图id
     * @param intent      page意图
     */
    public void startPage(int containerId, PageIntent intent) {
        startPage(containerId, intent, -1);
    }

    /**
     * 启动新界面
     *
     * @param containerId page所在的视图id
     * @param intent      page意图
     * @param isRequest   是否是请求
     */
    private void startPage(int containerId, PageIntent intent, int requestCode) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        Page page = intent.getTargetInstance();
        if (page instanceof PageAnimationI) {
            PageAnimationI pageAnim = (PageAnimationI) page;
            transaction.setCustomAnimations(pageAnim.enterAnimation(), pageAnim.exitAnimation(), pageAnim.popEnterAnimation(), pageAnim.popExitAnimation());

        }
        int flag = intent.getFlags();
        Bundle bundle = intent.getArguments();
        page.setArguments(bundle);
        page.setPageIntent(intent);
        page.setRequest(requestCode);

        // 子page中触发的界面切换请求，统一由父page来处理
        PageContext linkedContext = page.mLinkedContext;
        if (linkedContext instanceof Page) {
            Page linkedPage = (Page) linkedContext;
            if (linkedPage.getParent() != null) {
                page.mLinkedContext = linkedPage.getParent();
            }
        }
        // end

        switch (flag) {
            case PageIntent.FLAG_PAGE_NO_HISTORY:
                page.mContainerId = containerId;
                // 销毁被依赖的，且无需记录的page
                if (page.mLinkedContext != null && page.mLinkedContext.getPageIntent() != null) {
                    PageIntent p = page.mLinkedContext.getPageIntent();
                    if (p.getFlags() == PageIntent.FLAG_PAGE_NO_HISTORY) {
                        if (page.mLinkedContext instanceof Fragment) {
                            transaction.remove((Fragment) page.mLinkedContext);
                        }
                    }
                }
                transaction.add(containerId, page);
                transaction.commitAllowingStateLoss();
                break;
            case PageIntent.FLAG_PAGE_REPLACE:
                page.mContainerId = containerId;
                transaction.replace(containerId, page);
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.commitAllowingStateLoss();
                break;
            case PageIntent.FLAG_PAGE_REPLACE_AND_NO_HISTORY:
                page.mContainerId = containerId;
                if (page.mLinkedContext != null && page.mLinkedContext.getPageIntent() != null) {
                    PageIntent p = page.mLinkedContext.getPageIntent();
                    if (p.getFlags() == PageIntent.FLAG_PAGE_NO_HISTORY) {
                        if (page.mLinkedContext instanceof Fragment) {
                            transaction.remove((Fragment) page.mLinkedContext);
                        }
                    }
                }
                transaction.replace(containerId, page);
                // transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.commitAllowingStateLoss();
                break;
            case PageIntent.FLAG_PAGE_CLEAR_TOP:
                page.mContainerId = containerId;
                if (!getPageManager().popPageTopOf(intent)) {
                    if (getPageManager().hasIntent(intent)) {
                        getPageManager().getTopIntent().getTargetInstance().dispatchNewIntent(intent);
                    } else {
                        if (page.mLinkedContext != null && page.mLinkedContext.getPageIntent() != null) {
                            PageIntent p = page.mLinkedContext.getPageIntent();
                            if (p.getFlags() == PageIntent.FLAG_PAGE_NO_HISTORY) {
                                if (page.mLinkedContext instanceof Fragment) {
                                    transaction.remove((Fragment) page.mLinkedContext);
                                }
                            }
                        }
                        transaction.add(containerId, page);
                        transaction.addToBackStack(page.getStackKey());
                        transaction.commitAllowingStateLoss();
                    }
                }
                break;
            case PageIntent.FLAG_PAGE_STANDARD:
            default:
                page.mContainerId = containerId;
                if (page.mLinkedContext != null && page.mLinkedContext.getPageIntent() != null) {
                    PageIntent p = page.mLinkedContext.getPageIntent();
                    if (p.getFlags() == PageIntent.FLAG_PAGE_NO_HISTORY) {
                        if (page.mLinkedContext instanceof Fragment) {
                            transaction.remove((Fragment) page.mLinkedContext);
                        }
                    }
                }
                transaction.add(containerId, page);
                transaction.addToBackStack(page.getStackKey());
                transaction.commitAllowingStateLoss();
                break;
        }
    }

    /**
     * 启动新界面
     *
     * @param containerId page所在的视图id
     * @param intent      page意图
     * @param requestCode 请求的code
     */
    public void startPageForResult(int containerId, PageIntent intent, int requestCode) {
        startPage(containerId, intent, requestCode);
    }

    /**
     * 获取page管理器
     *
     * @return
     */
    public PageManager getPageManager() {
        if (mPageManager == null) {
            mPageManager = new PageManager(getSupportFragmentManager());
        }
        return mPageManager;
    }

    /**
     * 将包含page的控件注册到该module，以此管理view中包含的page
     *
     * @param listener
     */
    public void registViewWithPage(OnPageViewListener listener) {
        if (listener == null) {
            return;
        }
        if (!mLstPageViewListeners.contains(listener)) {
            listener.registToModule(this);
            mLstPageViewListeners.add(listener);
        }
    }

    public void finishToPage(PageIntent intent) {
        if (!getPageManager().popPageTopOf(intent)) {
            if (getPageManager().hasIntent(intent)) {
                getPageManager().getTopIntent().getTargetInstance().dispatchNewIntent(intent);
            }
        }
    }

}
