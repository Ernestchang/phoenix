package com.yulin.act.page.content.poem.view;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.yulin.applib.module.Module;
import com.yulin.applib.page.Page;
import com.yulin.classic.R;

import java.util.ArrayList;

/**
 * Created by liulei0905 on 2016/11/9.
 * <p>
 * 显示诗、词、曲内容
 */

public class PoemContentActivity extends Module {

    private static final String EXTRA_CONTENT_IDS = "key_content_ids";
    private static final String EXTRA_CURRENT_INDEX = "key_current_index";

    private ArrayList<Integer> mListContentIds;
    private int mCurrentIndex;

    public static void gotoModule(Page page, ArrayList<Integer> listContentIds, int currentIndex) {
        Bundle extras = new Bundle();
        extras.putIntegerArrayList(EXTRA_CONTENT_IDS, listContentIds);
        extras.putInt(EXTRA_CURRENT_INDEX, currentIndex);

        page.startModule(extras, PoemContentActivity.class);
    }

    @Override
    protected void receiveData(Intent intent) {
        super.receiveData(intent);

        Bundle extras = intent.getExtras();
        if (extras != null) {
            if (extras.containsKey(EXTRA_CONTENT_IDS)) {
                mListContentIds = extras.getIntegerArrayList(EXTRA_CONTENT_IDS);
            }
            if (extras.containsKey(EXTRA_CURRENT_INDEX)) {
                mCurrentIndex = extras.getInt(EXTRA_CURRENT_INDEX);
            }
        }
    }

    @Override
    protected void initModule() {
        super.initModule();

        DataBindingUtil.setContentView(this, R.layout.activity_frame);

        PoemContentHome.startPage(this, mListContentIds, mCurrentIndex);
    }

}
