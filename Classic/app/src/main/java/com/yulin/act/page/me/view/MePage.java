package com.yulin.act.page.me.view;

import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;

import com.yulin.act.page.base.PageImpl;
import com.yulin.act.util.Util;
import com.yulin.applib.bar.BarMenuContainer;
import com.yulin.applib.bar.BarMenuTextItem;
import com.yulin.applib.bar.TitleBar;
import com.yulin.classic.R;
import com.yulin.classic.databinding.PageMeBinding;

public class MePage extends PageImpl {

    @Override
    protected void initPage() {
        super.initPage();

        PageMeBinding pageBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.page_me, null, false);
        setContentView(pageBinding.getRoot());

        bindPageTitleBar(R.id.page_me_title_bar);
    }

    @Override
    protected boolean onCreatePageTitleBarMenu(BarMenuContainer menu) {
        BarMenuTextItem centerMenu = new BarMenuTextItem(1, "我");
        centerMenu.setTag(TitleBar.Position.CENTER);
        centerMenu.setTextSize(Util.getRDimensionPixelSize(R.dimen.txt_s5));
        menu.addItem(centerMenu);

        return true;
    }

}
