package com.hc.poem.page.base;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;

import com.hc.poem.R;
import com.hc.poem.util.Util;
import com.hc.poem.widget.bar.Bar;
import com.hc.poem.widget.bar.BarMenu;
import com.hc.poem.widget.bar.BarMenuItem;

public class TitleBarActivity extends AppCompatActivity {

    private boolean bNeedUpdate;
    private int mTitlebarId = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 设置沉浸入状态栏
        setStateBarTranslucent();
    }

    @Override
    protected void onResume() {
        super.onResume();
        onUpdateTitleBars();
    }

    /**
     * 更新已绑定的bar
     */
    private void onUpdateTitleBars() {
        if (!bNeedUpdate) {
            return;
        }
        bNeedUpdate = false;
        if (mTitlebarId > 0) {
            Bar bar = (Bar) findViewById(mTitlebarId);

            // 沉浸式状态栏设置
            if (Util.isKITKATUpVersion()) {
                bar.getLayoutParams().height = (int) (Util.getStatusBarHeight() + Util.getRDimension(R.dimen.title_bar_height));
                bar.setItemPaddings(0, Util.getStatusBarHeight(), 0, 0);
            }

            if (bar != null) {
                BarMenu menu = new BarMenu();
                boolean bRet = onCreateTitleBarMenu(bar, menu);
                if (bRet) {
                    bar.clearBarMenu();
                    bar.addMenuItems(menu.getItems());
                    bar.setOnBarMenuSelectedListener(new Bar.OnBarMenuSelectedListener() {
                        @Override
                        public void onItemSelected(int index, BarMenuItem item) {
                            onTitleBarMenuItemSelected(item);
                        }
                    });
                    bar.notifyBarSetChanged();

                    onTitleBarMenuCreated(bar, menu);
                }

            }
        }
    }

    /**
     * 绑定page titebar 即拥有该bar的所有控制权
     *
     * @param barId
     */
    protected void bindTitleBar(int barId) {
        if (barId != mTitlebarId) {
            mTitlebarId = barId;
            bNeedUpdate = true;
        }
    }

    /**
     * 解除绑定bar titebar 解除对该bar的所有控制权
     *
     * @param barId
     */
    protected void unbindPageTitleBar(int barId) {
        mTitlebarId = -1;
    }

    /**
     * 获取所有被绑定的bar
     *
     * @return
     */
    protected int getBoundBarId() {
        return mTitlebarId;
    }

    /**
     * 标题菜单选中事件回调
     *
     * @param menuitem
     */
    protected void onTitleBarMenuItemSelected(BarMenuItem menuitem) {

    }

    /**
     * 创建标题菜单开始事件回调
     *
     * @param barId
     * @param menu
     */
    protected boolean onCreateTitleBarMenu(Bar bar, BarMenu menu) {
        return false;
    }

    /**
     * 创建标题菜单完成事件回调
     *
     * @param barId
     * @param menu
     */
    protected void onTitleBarMenuCreated(Bar bar, BarMenu menu) {
    }

    private void setStateBarTranslucent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 设置状态栏透明
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                getWindow().setStatusBarColor(Color.TRANSPARENT);
            } else {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
        }
    }

}
