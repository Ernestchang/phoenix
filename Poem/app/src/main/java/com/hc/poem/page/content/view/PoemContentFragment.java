package com.hc.poem.page.content.view;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hc.poem.R;
import com.hc.poem.databinding.FragmentPoemContentBinding;
import com.hc.poem.databinding.LayoutTitlebarCenterTitlesBinding;
import com.hc.poem.page.author.view.AuthorActivity;
import com.hc.poem.page.base.TitleBarFragment;
import com.hc.poem.page.content.viewmodel.PoemContentViewModel;
import com.hc.poem.util.Util;
import com.hc.poem.widget.bar.Bar;
import com.hc.poem.widget.bar.BarMenu;
import com.hc.poem.widget.bar.BarMenuCustomItem;
import com.hc.poem.widget.bar.BarMenuTextItem;
import com.hc.poem.widget.bar.TitleBar;
import com.umeng.analytics.MobclickAgent;

import java.util.zip.Inflater;

public class PoemContentFragment extends TitleBarFragment {

    private static final String EXTRA_KEY_POEM_ID = "extra_key_poem_id";

    private int mPoemId;
    private LayoutInflater mInflater;

    public static PoemContentFragment newInstance(int mPoemId) {
        Bundle extras = new Bundle();
        extras.putInt(EXTRA_KEY_POEM_ID, mPoemId);

        PoemContentFragment fragment = new PoemContentFragment();
        fragment.setArguments(extras);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mInflater = inflater;

        FragmentPoemContentBinding contentBinding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_poem_content, container, false);

        Bundle extras = getArguments();
        if (extras != null) {
            mPoemId = extras.getInt(EXTRA_KEY_POEM_ID, 0);

            final PoemContentViewModel poemContentViewModel = new PoemContentViewModel(getContext(), mPoemId);
            contentBinding.setBean(poemContentViewModel);
            contentBinding.executePendingBindings();



            View rootView = contentBinding.getRoot();
            setContentView(rootView);
            bindTitleBar(R.id.layout_title_bar);

            return rootView;
        }

        return null;
    }

    @Override
    public void onResume() {
        super.onResume();

        // 使用友盟统计fragment页面
        MobclickAgent.onPageStart(getClass().getSimpleName());
    }

    @Override
    public void onPause() {
        super.onPause();

        MobclickAgent.onPageEnd(getClass().getSimpleName());
    }

    @Override
    protected boolean onCreateTitleBarMenu(Bar bar, BarMenu menu) {
        View leftBack = mInflater.inflate(R.layout.layout_titlebar_left_back, null, false);
        BarMenuCustomItem leftMenu = new BarMenuCustomItem(0, leftBack);
        leftMenu.setTag(TitleBar.Position.LEFT);
        menu.addItem(leftMenu);

        leftBack.findViewById(R.id.img_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        LayoutTitlebarCenterTitlesBinding centerBinding = DataBindingUtil.inflate(mInflater,
                R.layout.layout_titlebar_center_titles, null, false);

        final PoemContentViewModel poemContentViewModel = new PoemContentViewModel(getContext(), mPoemId);
        centerBinding.setBean(poemContentViewModel);
        centerBinding.executePendingBindings();

        BarMenuCustomItem centerMenu = new BarMenuCustomItem(1, centerBinding.rootLayout);
        centerMenu.setTag(TitleBar.Position.CENTER);
        menu.addItem(centerMenu);

        // Show author content when click author name.
        centerBinding.tvAuthor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AuthorActivity.start(getActivity(), poemContentViewModel.getAuthor());
                }
            });

        return true;
    }

}
