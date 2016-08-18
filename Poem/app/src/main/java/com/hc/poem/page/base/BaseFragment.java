package com.hc.poem.page.base;

import android.support.v4.app.Fragment;
import android.view.View;

public class BaseFragment extends Fragment {

    private View mContentView;

    protected View getContentView() {
        return mContentView;
    }

    protected void setContentView(View mContentView) {
        this.mContentView = mContentView;
    }

}
