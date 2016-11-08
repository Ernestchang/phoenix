package com.yulin.act.page.smenu.view;

import android.databinding.DataBindingUtil;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;

import com.yulin.act.config.DataModule;
import com.yulin.act.page.base.PageImpl;
import com.yulin.act.util.Util;
import com.yulin.applib.bar.BarMenuContainer;
import com.yulin.applib.bar.BarMenuCustomItem;
import com.yulin.applib.bar.BarMenuItem;
import com.yulin.applib.bar.BarMenuTextItem;
import com.yulin.applib.bar.TitleBar;
import com.yulin.applib.module.Module;
import com.yulin.applib.page.PageIntent;
import com.yulin.classic.R;
import com.yulin.classic.databinding.PageShortMenuBinding;

public class ShortMenuPage extends PageImpl {

    @Override
    protected void initPage() {
        super.initPage();

        PageShortMenuBinding pageBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.page_short_menu, null, false);
        setContentView(pageBinding.getRoot());

        bindPageTitleBar(R.id.page_short_menu_title_bar);
    }

    @Override
    protected boolean onCreatePageTitleBarMenu(BarMenuContainer menu) {
        View leftView = View.inflate(Util.getContext(), R.layout.layout_titlebar_left_back, null);
        BarMenuCustomItem leftMenu = new BarMenuCustomItem(0, leftView);
        leftMenu.setTag(TitleBar.Position.LEFT);
        menu.addItem(leftMenu);

        BarMenuTextItem centerMenu = new BarMenuTextItem(1, "诗经");
        centerMenu.setTag(TitleBar.Position.CENTER);
        centerMenu.setTextSize(Util.getRDimensionPixelSize(R.dimen.txt_s5));
        menu.addItem(centerMenu);

        return true;
    }

    @Override
    protected void onPageTitleBarMenuItemSelected(BarMenuItem menuItem) {
        super.onPageTitleBarMenuItemSelected(menuItem);

        int menuId = menuItem.getItemId();

        if (menuId == 0 && mPageChangeFlag == 0) {
            mPageChangeFlag = -1;
            finish();
        }
    }

    public static void startPage(Module module) {
        PageIntent pageIntent=new PageIntent(null, ShortMenuPage.class);
        pageIntent.setSupportAnimation(false);
        module.startPage(DataModule.G_CURRENT_FRAME, pageIntent);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }

        return true;
    }

}
