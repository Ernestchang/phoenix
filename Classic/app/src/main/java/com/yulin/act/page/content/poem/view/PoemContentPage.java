package com.yulin.act.page.content.poem.view;

import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;

import com.yulin.act.model.PoemContent;
import com.yulin.act.model.Result;
import com.yulin.act.page.base.PageImpl;
import com.yulin.act.page.content.poem.vm.PoemContentVm;
import com.yulin.act.util.LogUtil;
import com.yulin.classic.R;
import com.yulin.classic.databinding.PagePoemContentBinding;

import rx.Observer;

/**
 * Created by liulei0905 on 2016/11/9.
 * <p>
 * 显示诗、词、曲内容
 * <p>
 * 内容元素：标题、作者、内容、朝代
 * <p>
 * 布局设计：
 * 标题、作者、朝代显示在titlebar中，标题居中大字号显示，朝代和作者显示在标题下方，小字显示
 */

public class PoemContentPage extends PageImpl {

    private int mContentId;
    private int mPageIndex;
    private boolean mIsContentLoadComplete;

    private PoemContentVm mContentVm;
    private PoemContentHome.OnTitleBarContentChanged mOnTitleBarContentChanged;

    @Override
    protected void initPage() {
        super.initPage();

        PagePoemContentBinding pageBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.page_poem_content, null, false);
        setContentView(pageBinding.getRoot());

        mContentVm = new PoemContentVm();
        pageBinding.setModel(mContentVm.getPoemContent());
    }

    @Override
    protected void onPageResume() {
        super.onPageResume();

        if (!mIsContentLoadComplete) {
            mContentVm.queryPoemContent(new Observer<Result>() {
                @Override
                public void onCompleted() {
                    mIsContentLoadComplete = true;

                    if (mOnTitleBarContentChanged != null) {
                        mOnTitleBarContentChanged.changeTitleBarContent(
                                mContentVm.getPoemContent().getTitle(),
                                mContentVm.getPoemContent().getAuthor(), mPageIndex);
                    }

                    LogUtil.log("open " + mContentId + " success.");
                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(Result result) {

                }
            }, mContentId);
        }
    }

    @Override
    protected void onPageDestroy() {
        super.onPageDestroy();

        mContentVm.clearSubscription();
    }

    public void setContentId(int id) {
        mContentId = id;
    }

    public PoemContent getPoemContent() {
        return mContentVm.getPoemContent();
    }

    public void setOnTitlebarContentChanged(PoemContentHome.OnTitleBarContentChanged onTitlebarContentChanged) {
        mOnTitleBarContentChanged = onTitlebarContentChanged;
    }

    public void setPageIndex(int index) {
        mPageIndex = index;
    }

}
