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
import com.hc.poem.page.author.view.AuthorActivity;
import com.hc.poem.page.content.viewmodel.PoemContentViewModel;
import com.umeng.analytics.MobclickAgent;

public class PoemContentFragment extends Fragment {

    private static final String EXTRA_KEY_POEM_ID = "extra_key_poem_id";

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
        FragmentPoemContentBinding contentBinding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_poem_content, container, false);

        Bundle extras = getArguments();
        if (extras != null) {
            int poemId = extras.getInt(EXTRA_KEY_POEM_ID, 0);

            final PoemContentViewModel poemContentViewModel = new PoemContentViewModel(getContext(), poemId);
            contentBinding.setBean(poemContentViewModel);
            contentBinding.executePendingBindings();

            // Show author content when click author name.
            contentBinding.tvAuthor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AuthorActivity.start(getActivity(), poemContentViewModel.getAuthor());
                }
            });

            // close activity when click back
            contentBinding.imgBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().finish();
                }
            });

            return contentBinding.getRoot();
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

}
