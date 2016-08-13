package com.hc.poem.page.content.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Window;

import com.hc.poem.R;
import com.hc.poem.page.base.BaseActivity;

import java.util.ArrayList;

public class PoemContentActivity extends BaseActivity {

    public static final String EXTRA_POEM_IDS = "extra_poem_ids";
    public static final String EXTRA_POEM_CURRENT_POSITION = "extra_poem_current_position";

    private ArrayList<Integer> mListPoemIds = new ArrayList<>();
    private int mCurrentItem;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poem_content);

        // Get poemIdList from intent.
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mListPoemIds = extras.getIntegerArrayList(EXTRA_POEM_IDS);
            mCurrentItem = extras.getInt(EXTRA_POEM_CURRENT_POSITION);
        }

        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(new ScreenSlidePagerAdapter(getSupportFragmentManager()));
        viewPager.setCurrentItem(mCurrentItem);
    }

    class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return PoemContentFragment.newInstance(mListPoemIds.get(position));
        }

        @Override
        public int getCount() {
            return mListPoemIds.size();
        }

    }

}
